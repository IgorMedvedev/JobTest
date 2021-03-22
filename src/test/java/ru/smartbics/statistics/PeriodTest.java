package ru.smartbics.statistics;

import org.junit.Assert;
import org.junit.Test;
import ru.smartbics.statistics.impl.Period;

import java.time.LocalDateTime;

public class PeriodTest {

    @Test
    public void hashCodeTest() {
        LocalDateTime now = LocalDateTime.now();
        Period pMinute1 = new Period(now, Period.Type.MINUTE);
        Period pMinute2 = new Period(now, Period.Type.MINUTE);
        Period pHour = new Period(now, Period.Type.HOUR);
        LocalDateTime anotherYear = now.plusYears(1);
        Period pMinute3 = new Period(anotherYear, Period.Type.MINUTE);
        Assert.assertEquals(pMinute1.hashCode(), pMinute1.hashCode());
        Assert.assertEquals(pMinute1.hashCode(), pMinute2.hashCode());
        Assert.assertNotEquals(pMinute1.hashCode(), pHour.hashCode());
        Assert.assertNotEquals(pMinute1.hashCode(), pMinute3.hashCode());
    }

    @Test
    public void equalsTest() {
        LocalDateTime now = LocalDateTime.now();
        Period pMinute1 = new Period(now, Period.Type.MINUTE);
        Period pMinute2 = new Period(now, Period.Type.MINUTE);
        Period pHour = new Period(now, Period.Type.HOUR);
        Period pMinute3 = new Period(now.plusMinutes(2), Period.Type.MINUTE);

        Assert.assertEquals(pMinute1, pMinute2);
        Assert.assertNotEquals(pMinute1, pHour);
        Assert.assertNotEquals(pMinute1, pMinute3);
    }
}
