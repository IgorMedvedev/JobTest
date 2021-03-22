package ru.smartbics.statistics;

import static org.junit.Assert.assertTrue;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import ru.smartbics.ErrorsCounter;
import ru.smartbics.statistics.impl.Period;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;


public class ErrorsCounterTest
{
    private static final Integer TOTAL_ERRORS = ThreadLocalRandom.current().nextInt(500, 5000);
    private static final Integer TOTAL_FILES = 5;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void generateData() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (int i=0; i < TOTAL_FILES; i++) {
            String name = "log_" + i + ".log";
            File newLogFile = folder.newFile(name);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(newLogFile))){
                int errorCount = 0;
                while (errorCount < TOTAL_ERRORS) {
                    writer.append(formatter.format(getRandomDateTime())).append(";");
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        writer.append("ERROR; Ошибка ").append(String.valueOf(errorCount));
                        errorCount++;
                    } else {
                        writer.append("WARNING; Предупреждение ").append(String.valueOf(ThreadLocalRandom.current()
                                .nextInt(0, TOTAL_ERRORS)));
                    }
                    writer.newLine();
                    writer.flush();
                }
            }
        }
    }

    @Test
    public void errorsCounterTest() {
        ErrorsCounter errorsCounter = new ErrorsCounter(folder.getRoot().getAbsolutePath(), Period.Type.HOUR,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME, Pattern.compile("ERROR"),
                Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})"));
        Assert.assertEquals((Integer)(TOTAL_ERRORS * TOTAL_FILES), errorsCounter.getTotalErrorsCount());
    }

    @After
    public void clearData() {
        folder.delete();
    }

    private LocalDateTime getRandomDateTime() {
        int days = 20;
        return LocalDateTime.of(LocalDate.ofEpochDay(ThreadLocalRandom
                        .current().nextInt(-days, days)),
                LocalTime.ofSecondOfDay(ThreadLocalRandom
                        .current().nextInt(LocalTime.MIN.toSecondOfDay(), LocalTime.MAX.toSecondOfDay())));
    }
}
