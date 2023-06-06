package lv.vladislavs.ewallet.util;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class BusinessDaysUtil {
    public static ZonedDateTime getNextBusinessDay() {
        ZonedDateTime tomorrow = ZonedDateTime.now(ZoneOffset.UTC).plusDays(1);
        DayOfWeek tomorrowDayOfWeek = tomorrow.getDayOfWeek();

        if (tomorrowDayOfWeek == DayOfWeek.SATURDAY) {
            return tomorrow.plusDays(2);
        }
        if (tomorrowDayOfWeek == DayOfWeek.SUNDAY) {
            return tomorrow.plusDays(1);
        }

        return tomorrow;
    }
}
