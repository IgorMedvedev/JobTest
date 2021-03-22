package ru.smartbics.statistics.api;

import java.io.Closeable;
import java.util.Iterator;

/**
 * Источник данных
 */
public interface IStatisticDataSource extends Iterator<String>, Closeable {
}
