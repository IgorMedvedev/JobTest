package ru.smartbics;

import ru.smartbics.statistics.api.IStatisticsCollector;
import ru.smartbics.statistics.api.IStatisticDataSource;
import ru.smartbics.statistics.impl.*;
import ru.smartbics.statistics.impl.collector.MultiThreadingStatisticsCollector;
import ru.smartbics.statistics.impl.datasource.LogFileDataSource;
import ru.smartbics.statistics.impl.parser.SingleLineLogParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ErrorsCounter
{
    private String sourceDirectory;
    private Period.Type periodType;
    private DateTimeFormatter dateTimeFormatter;
    private Pattern errorPattern;
    private Pattern dateTimePattern;

    public ErrorsCounter(String sourceDirectory, Period.Type periodType, DateTimeFormatter dateTimeFormatter,
                         Pattern errorPattern, Pattern dateTimePattern) {
        this.sourceDirectory = sourceDirectory;
        this.periodType = periodType;
        this.dateTimeFormatter = dateTimeFormatter;
        this.errorPattern = errorPattern;
        this.dateTimePattern = dateTimePattern;
    }

    public void printErrorsCount() {
        Map<Period, Integer> result = getErrorStatistics();
        Map<Period, Integer> sortedResult = result.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        FileStatisticsPrinter fileStatisticsPrinter = new FileStatisticsPrinter("Statistics");
        fileStatisticsPrinter.printStatistic(sortedResult, "%1$s Количество ошибок: %2$s");
    }

    public Integer getTotalErrorsCount() {
        Map<Period, Integer> result = getErrorStatistics();
        return result.values().stream().reduce(0, (v1, v2) -> v1 + v2);
    }

    private Map<Period, Integer> getErrorStatistics() {
        IStatisticsCollector statisticsCollector = new MultiThreadingStatisticsCollector(periodType);
        List<IStatisticDataSource> dataSourceList = new ArrayList<>();
        try (Stream<Path> path = Files.walk(Paths.get(sourceDirectory))) {
            List<Path> files = path.filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path file : files) {
                dataSourceList.add(new LogFileDataSource(file));
            }
            return statisticsCollector.getStatistic(new SingleLineLogParser(dateTimeFormatter,
                    errorPattern, dateTimePattern), dataSourceList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_MAP;
    }

    public static void main( String[] args )
    {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: ErrorsCounter <directory_name> <MINUTE|HOUR>");
        }
        ErrorsCounter errorsCounter = new ErrorsCounter(args[0], Period.Type.valueOf(args[1]), DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                Pattern.compile("ERROR"), Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})"));
        errorsCounter.printErrorsCount();
    }
}
