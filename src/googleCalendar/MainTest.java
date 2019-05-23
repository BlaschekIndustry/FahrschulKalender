package googleCalendar;

import com.google.api.client.util.DateTime;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Date;

public class MainTest {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        GoogleCalendar test = new GoogleCalendar();

        Date date = new Date(System.currentTimeMillis());
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        test.getEventOnDay(date);
    }
}
