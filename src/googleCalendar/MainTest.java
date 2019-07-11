package googleCalendar;

import com.google.api.client.util.DateTime;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainTest {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        GoogleCalendar test = new GoogleCalendar();
        Date date = new Date(System.currentTimeMillis());
        ArrayList<GoogleEvent> eventList = test.getEvents(date, 3);
        for(GoogleEvent event : eventList){
            System.out.println(event.toString());
        }
    }


}
