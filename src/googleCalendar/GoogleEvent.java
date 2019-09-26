package googleCalendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import general.DrivingTeacher;
import general.LicenceType;
import general.Vehicle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static general.LicenceType.XMLNameOfType;

public class GoogleEvent {
    private static final String START_SEQUENZ = "#StartIntern";
    private static final String END_SEQUENZ = "#EndIntern";

    private String header;
    private String description;
    private Date startDate;
    private Date endDate;

    //Drivinglesson Params
    private String student = "";
    private LicenceType licence = null;
    private ArrayList<String> teachers = new ArrayList<>();
    private ArrayList<String> vehicles = new ArrayList<>();

    public GoogleEvent(String header, String description, Date startDate, Date endDate){
        this.header = header;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

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
        fillDrivingLessonParamsWithDescription();
    }

    private void fillDrivingLessonParamsWithDescription(){
        int startSequenzIndex = description.indexOf(START_SEQUENZ);

        String curSubStr = description.substring(startSequenzIndex+START_SEQUENZ.length()+1);
        curSubStr = curSubStr.substring(0, curSubStr.indexOf(END_SEQUENZ));

        String[] partsOfCurSub = curSubStr.split("#");

        if(partsOfCurSub.length != 4) {
            System.out.println("Fehler lesen");
            //TODO FEhler lesen
        }

        student = partsOfCurSub[0];

        String licenceText = partsOfCurSub[1];
        licence = LicenceType.TypeOfXMLName(licenceText);

        String drivingTeacherText  = partsOfCurSub[2];
        String[] drivingTeachersArray = drivingTeacherText.split(";");
        for(String curDrivingTeacher : drivingTeachersArray){
            teachers.add(curDrivingTeacher);
        }

        String cehicleText  = partsOfCurSub[3];
        String[] vehiclesArray = cehicleText.split(";");
        for(String curVehicle : vehiclesArray){
            vehicles.add(curVehicle);
        }

    }

    public String createTableString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        String tableString = "";
        tableString += calendar.get(Calendar.HOUR) + ":";
        tableString += calendar.get(Calendar.MINUTE) + "-";

        calendar.setTime(endDate);

        tableString += calendar.get(Calendar.HOUR) + ":";
        tableString += calendar.get(Calendar.MINUTE);

        tableString += "\n";
        for(String curTeacher : teachers){
            tableString += curTeacher + ",";
        }
        tableString += XMLNameOfType(licence);
        return tableString;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public LicenceType getLicence() {
        return licence;
    }

    public void setLicence(LicenceType licence) {
        this.licence = licence;
    }

    public ArrayList<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(ArrayList<String> teachers) {
        this.teachers = teachers;
    }

    public ArrayList<String> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<String> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public String toString(){
        return "header: " + header + "\n" +
                "von:" + startDate.toString() + " bis: " +endDate.toString() + "\n" +
                "Beschreibung: " + description;
    }
}
