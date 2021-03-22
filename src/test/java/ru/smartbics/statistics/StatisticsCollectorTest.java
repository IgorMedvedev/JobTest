package ru.smartbics.statistics;

import org.junit.Assert;
import org.junit.Test;
import ru.smartbics.statistics.api.IStatisticDataSource;
import ru.smartbics.statistics.api.IStatisticsCollector;
import ru.smartbics.statistics.impl.Period;
import ru.smartbics.statistics.impl.collector.SimpleStatisticsCollector;
import ru.smartbics.statistics.impl.datasource.PlainTextStatisticDataSource;
import ru.smartbics.statistics.impl.parser.SingleLineLogParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

public class StatisticsCollectorTest {

    @Test
    public void minutesStatisticsTest() {
        String text = "2019-01-01T00:12:01.001;ERROR; Ошибка 1\n" +
                "2019-01-01T00:12:01.004;ERROR; Ошибка 2\n" +
                "2019-01-01T00:12:01.006;ERROR; Ошибка 3\n" +
                "2019-01-02T00:13:02.000;WARN; Предупреждение 1\n" +
                "2019-01-02T00:14:02.002;ERROR; Ошибка 5\n" +
                "2019-01-03T00:14:03.003;ERROR; Ошибка 6\n" +
                "2020-01-03T00:14:03.003;ERROR; Ошибка 7";
        Map<Period, Integer> result = getStatistics(text, Period.Type.MINUTE);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(new Integer(3), result.get(new Period(LocalDateTime.of(2019, 1, 1, 0, 12),
                Period.Type.MINUTE)));
        Assert.assertEquals(null, result.get(new Period(LocalDateTime.of(2019, 1, 2, 0, 13),
                Period.Type.MINUTE)));
        Assert.assertEquals(new Integer(1), result.get(new Period(LocalDateTime.of(2019, 1, 3, 0, 14),
                Period.Type.MINUTE)));
    }

    @Test
    public void hourStatisticsTest() {
        String text = "2019-01-01T00:12:01.001;ERROR; Ошибка 1\n" +
                "2019-01-01T00:12:01.004;ERROR; Ошибка 2\n" +
                "2019-01-01T00:12:01.006;ERROR; Ошибка 3\n" +
                "2019-01-02T00:13:02.000;WARN; Предупреждение 1\n" +
                "2019-01-02T00:14:02.002;ERROR; Ошибка 5\n" +
                "2019-01-03T00:14:03.003;ERROR; Ошибка 6\n" +
                "2020-01-03T00:14:03.003;ERROR; Ошибка 7\n" +
                "2020-01-03T01:00:00.000;ERROR; Ошибка 8";
        Map<Period, Integer> result = getStatistics(text, Period.Type.HOUR);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals(new Integer(3), result.get(new Period(LocalDateTime.of(2019, 1, 1, 0, 12),
                Period.Type.HOUR)));
        Assert.assertEquals(new Integer(1), result.get(new Period(LocalDateTime.of(2019, 1, 2, 0, 13),
                Period.Type.HOUR)));
        Assert.assertEquals(new Integer(1), result.get(new Period(LocalDateTime.of(2019, 1, 3, 0, 14),
                Period.Type.HOUR)));
    }

    private Map<Period, Integer> getStatistics(String testData, Period.Type period) {
        IStatisticDataSource dataSource = new PlainTextStatisticDataSource(testData);
        IStatisticsCollector collector = new SimpleStatisticsCollector(period);
        return collector.getStatistic(new SingleLineLogParser(DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                        Pattern.compile("ERROR"), Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})")),
                Collections.singletonList(dataSource));
    }
}
