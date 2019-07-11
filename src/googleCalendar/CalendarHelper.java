package googleCalendar;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class CalendarHelper {
    public static Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }

    public static Date getLastMonday(Date day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

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
        long addTime = 0;
        int counts = dayPeriod;
        if(dayPeriod < 0)
            counts *= -1;
        for(int i = 0; i < counts; i++) {
            if(dayPeriod > 0)
                addTime += 1000 * 60 * 60 * 24;
            else
                addTime -= 1000 * 60 * 60 * 24;
        }
        timeMillis += addTime;

        return new Date(timeMillis);
    }
}
