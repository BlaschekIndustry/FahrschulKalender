package googleCalendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.util.Date;

public class GoogleEvent {
    private String header;
    private String description;
    private Date startDate;
    private Date endDate;

    public GoogleEvent(Event googleEvent){
        this.header = googleEvent.getSummary();
        this.description = googleEvent.getDescription();

        DateTime start = googleEvent.getStart().getDateTime();
        if (start == null) {
            start = googleEvent.getStart().getDate();
        }
        this.startDate = new Date((start.getValue()));

        DateTime end = googleEvent.getEnd().getDateTime();
        if (end == null) {
            end = googleEvent.getEnd().getDate();
        }
        this.endDate = new Date((end.getValue()));
    }
}
