package ru.smartbics.statistics.api;

import java.time.LocalDateTime;

public interface IDataSourceParser {

    String getLogEntry(IStatisticDataSource dataSource);

    LocalDateTime parsePeriodStartTime(String statisticEntry);

    boolean shouldCollect(String statisticEntry);
}
