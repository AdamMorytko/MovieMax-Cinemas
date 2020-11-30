package pl.morytko.moviemax.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    public static List<LocalDate> getTwoWeeks(){
        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        dates.add(today);
        for (int i = 1; i < 14; i++) {
            dates.add(today.plusDays(i));
        }
        return dates;
    }
}
