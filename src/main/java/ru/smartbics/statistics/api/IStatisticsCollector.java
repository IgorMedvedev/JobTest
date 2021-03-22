package ru.smartbics.statistics.api;

import ru.smartbics.statistics.impl.Period;

import java.util.List;
import java.util.Map;

/**
 * Используется для подсчета количества ошибок за определенный период
 */
public interface IStatisticsCollector {

    Map<Period, Integer> getStatistic(IDataSourceParser logParser, List<IStatisticDataSource> dataSourceList);
}
