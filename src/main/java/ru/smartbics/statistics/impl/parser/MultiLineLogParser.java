package ru.smartbics.statistics.impl.parser;

import ru.smartbics.statistics.api.IStatisticDataSource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class MultiLineLogParser extends SingleLineLogParser {

    public MultiLineLogParser(DateTimeFormatter dateTimeFormat, Pattern errorPattern, Pattern dateTimePattern) {
        super(dateTimeFormat, errorPattern, dateTimePattern);
    }

    @Override
    //TODO
    public String getLogEntry(IStatisticDataSource dataSource) {
        throw new NotImplementedException();
    }
}
