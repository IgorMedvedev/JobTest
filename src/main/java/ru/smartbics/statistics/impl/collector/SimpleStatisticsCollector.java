package ru.smartbics.statistics.impl.collector;

import ru.smartbics.statistics.api.IStatisticsCollector;
import ru.smartbics.statistics.api.IDataSourceParser;
import ru.smartbics.statistics.api.IStatisticDataSource;
import ru.smartbics.statistics.impl.Period;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Простой сбор статистики по периоду
 */
public class SimpleStatisticsCollector implements IStatisticsCollector {

    private Period.Type statisticPeriodType;

    public SimpleStatisticsCollector(Period.Type statisticPeriodType) {
        this.statisticPeriodType = statisticPeriodType;
    }

    @Override
    public Map<Period, Integer> getStatistic(IDataSourceParser logParser, List<IStatisticDataSource> dataSourceList) {
        Map<Period, Integer> result = new HashMap<>();
        for (IStatisticDataSource dataSource : dataSourceList) {
            try {
                while(dataSource.hasNext()) {
                    String logEntry = logParser.getLogEntry(dataSource);
                    if (logParser.shouldCollect(logEntry)) {
                        Period period = new Period(logParser.parsePeriodStartTime(logEntry), statisticPeriodType);
                        result.merge(period, 1, (v1, v2) -> v1 + v2);
                    }
                }
            } finally {
                try {
                    dataSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
