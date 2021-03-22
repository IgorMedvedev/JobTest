package ru.smartbics.statistics.impl.parser;

import ru.smartbics.statistics.api.IDataSourceParser;
import ru.smartbics.statistics.api.IStatisticDataSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleLineLogParser implements IDataSourceParser {

    private DateTimeFormatter dateTimeFormat;
    private Pattern errorPattern;
    private Pattern dateTimePattern;

    public SingleLineLogParser(DateTimeFormatter dateTimeFormat, Pattern errorPattern, Pattern dateTimePattern) {
        this.dateTimeFormat = dateTimeFormat;
        this.errorPattern = errorPattern;
        this.dateTimePattern = dateTimePattern;
    }

    @Override
    public String getLogEntry(IStatisticDataSource dataSource) {
        return dataSource.next();
    }

    @Override
    public LocalDateTime parsePeriodStartTime(String statisticEntry) {
        Matcher dateTimeMatcher = dateTimePattern.matcher(statisticEntry);
        if (dateTimeMatcher.find()) {
            String dateTimeString = dateTimeMatcher.group();
            return LocalDateTime.parse(dateTimeString, dateTimeFormat);
        } else {
            return null;
        }
    }

    @Override
    public boolean shouldCollect(String statisticEntry) {
        return errorPattern.matcher(statisticEntry).find();
    }
}
