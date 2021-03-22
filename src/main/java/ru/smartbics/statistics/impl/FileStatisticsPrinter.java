package ru.smartbics.statistics.impl;

import ru.smartbics.statistics.api.IStatisticsPrinter;

import java.io.*;
import java.util.Map;

/**
 * Вывод статистики в файл
 */
public class FileStatisticsPrinter implements IStatisticsPrinter {

    private String fileName;

    public FileStatisticsPrinter(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void printStatistic(Map<Period, Integer> statistics, String messageTemplate) {
        File file = new File(fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            PrintWriter printWriter = new PrintWriter(outputStream);
            for (Map.Entry<Period, Integer> entry : statistics.entrySet()) {
                printWriter.println(String.format(messageTemplate, entry.getKey().toString(), entry.getValue()));
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
