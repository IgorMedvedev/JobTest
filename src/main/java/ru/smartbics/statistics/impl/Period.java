package ru.smartbics.statistics.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Промежуток времени заданного типа
 */
public class Period implements Comparable<Period> {

    private final LocalDateTime startDate;
    private final Type type;

    public Period(LocalDateTime startDate, Type type) {
        this.startDate = startDate;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Period) {
            Period other = (Period) obj;
            if (startDate.getYear() != other.startDate.getYear()) {
                return false;
            }
            if (startDate.getDayOfYear() != other.startDate.getDayOfYear()) {
                return false;
            }
            if (!type.equals(other.type)) {
                return false;
            }
            switch (type) {
                case HOUR:
                    return startDate.getHour() == other.startDate.getHour();
                case MINUTE:
                    return startDate.getMinute() == other.startDate.getMinute();
                default:
                    throw new IllegalArgumentException("Unsupported period type");
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + startDate.getYear();
        hash = hash * 31 + startDate.getDayOfYear();
        hash = hash * 31 + startDate.getHour();
        if (type == Type.HOUR) {
            return hash;
        }
        hash = hash * 31 + startDate.getMinute();
        return hash;
    }

    @Override
    public String toString() {
        LocalDateTime periodStartDate;
        LocalDateTime periodFinishDate;
        String timeFormatter;
        switch (type) {
            case HOUR:
                periodStartDate = startDate.truncatedTo(ChronoUnit.HOURS);
                periodFinishDate = periodStartDate.plusHours(1);
                timeFormatter = "%1$tH:00";
                break;
            case MINUTE:
                periodStartDate = startDate.truncatedTo(ChronoUnit.MINUTES);
                periodFinishDate = periodStartDate.plusMinutes(1);
                timeFormatter = "%1$tH:%1$tM";
                break;
            default:
                throw new IllegalArgumentException("Unsupported period type");
        }
        return periodStartDate.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + ", " +
                String.format(timeFormatter, periodStartDate) + "-" + String.format(timeFormatter, periodFinishDate);
    }

    @Override
    public int compareTo(Period o) {
        return startDate.compareTo(o.startDate);
    }

    public enum Type {
        HOUR, MINUTE;
    }
}
