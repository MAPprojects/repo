package Utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class CurrentWeek {
    public static long getCurrentWeek() {
        return getWeek(LocalDateTime.now());
    }

    public static long getWeek(LocalDateTime localDateTime) {
        LocalDateTime t1 = LocalDateTime.of(2017,10,2,0,0);
        LocalDateTime t2 = localDateTime;

        final int hoursOfDay=24;
        final int weekDays=7;

        long week =  Duration.between(t1, t2).toHours() / (hoursOfDay * weekDays) +1;
        if (week>12)
            week = week-2;
        return week;
    }
}
