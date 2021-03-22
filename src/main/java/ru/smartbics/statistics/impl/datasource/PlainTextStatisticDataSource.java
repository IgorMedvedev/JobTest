package ru.smartbics.statistics.impl.datasource;

import ru.smartbics.statistics.api.IStatisticDataSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class PlainTextStatisticDataSource implements IStatisticDataSource {

    private Iterator<String> iterator;

    public PlainTextStatisticDataSource(String text) {
        iterator = Arrays.asList(text.split("\\n")).iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public String next() {
        return iterator.next();
    }

    @Override
    public void close() throws IOException {

    }
}
