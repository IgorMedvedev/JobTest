package ru.smartbics.statistics.impl.collector;

import ru.smartbics.statistics.api.IStatisticsCollector;
import ru.smartbics.statistics.api.IDataSourceParser;
import ru.smartbics.statistics.api.IStatisticDataSource;
import ru.smartbics.statistics.impl.Period;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Многопоточный сбор статистики
 */
public class MultiThreadingStatisticsCollector extends SimpleStatisticsCollector implements IStatisticsCollector {

    private volatile Map<Period, Integer> summaryResult = new ConcurrentHashMap<>();

    public MultiThreadingStatisticsCollector(Period.Type periodType) {
        super(periodType);
    }

    @Override
    public Map<Period, Integer> getStatistic(IDataSourceParser logParser, List<IStatisticDataSource> dataSourceList) {
        ExecutorService executorService = Executors.newWorkStealingPool();
        CountDownLatch doneSignal = new CountDownLatch(dataSourceList.size());
        for (IStatisticDataSource dataSource : dataSourceList) {
            executorService.execute(() -> {
                Map<Period, Integer> result = MultiThreadingStatisticsCollector
                        .super.getStatistic(logParser, Collections.singletonList(dataSource));
                result.forEach((v1, v2) -> summaryResult.merge(v1, v2, Integer::sum));
                doneSignal.countDown();
            });
        }
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        return summaryResult;
    }
}
