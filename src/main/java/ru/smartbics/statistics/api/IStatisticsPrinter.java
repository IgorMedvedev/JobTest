package ru.smartbics.statistics.api;

import ru.smartbics.statistics.impl.Period;

import java.util.Map;

/**
 * Печать статистики в форматированном виде
 */
public interface IStatisticsPrinter {

    void printStatistic(Map<Period, Integer> statistics, String messageTemplate);
}
