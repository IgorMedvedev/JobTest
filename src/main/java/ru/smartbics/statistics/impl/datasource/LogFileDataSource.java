package ru.smartbics.statistics.impl.datasource;

import ru.smartbics.statistics.api.IStatisticDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class LogFileDataSource implements IStatisticDataSource {

    private Scanner scanner;

    public LogFileDataSource(Path file) throws IOException {
        this.scanner = new Scanner(Files.newBufferedReader(file, StandardCharsets.UTF_8));
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNext();
    }

    @Override
    public String next() {
        return scanner.next();
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }
}
