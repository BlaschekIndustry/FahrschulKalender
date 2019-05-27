package googleCalendar;

import java.util.Calendar;
import java.util.Date;

public class CalendarHelper {

    //Changes the Time of the Date to the start of the Day
    public static Date getStartOfTheDay(Date day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    //Changes the Time of the Date to the end of the Day
    public static Date getEndOfTheDay(Date day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return calendar.getTime();
    }

    public static Date getDateAfterDayPeriode(Date startDate, int dayPeriod){
        if(dayPeriod == 0)
            return startDate;

        long timeMillis = startDate.getTime();
        long addTime = dayPeriod * 1000 * 60 * 60 * 24;
        timeMillis += addTime;

        return new Date(timeMillis);
    }
}
