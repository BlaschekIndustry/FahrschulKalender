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
        test.getEvents(date, 2);

    }
}
