package general;

import java.util.ArrayList;

public class DrivingTeacher {
    public static final String XML_TEACHER_GROUP_IDENT = "drivingTeachers";
    public static final String XML_TEACHER_IDENT = "teacher";
    public static final String XML_TEACHER_NAME_IDENT = "tName";
    public static final String XML_TEACHER_WORKINGHOURS_IDENT = "tWorkingHours";
    public static final String XML_TEACHER_LICENCETYPE_IDENT = "tLicence";
    private String name;
    private int workingHours;
    private ArrayList<LicenceType> licenceTypes;

    public DrivingTeacher(){
        this("Neuer Fahrlehrer", 38, null);
        licenceTypes = new ArrayList<>();
        licenceTypes.add(LicenceType.LICENCE_TYPE_B);
    }

    public DrivingTeacher( DrivingTeacher teacher){
        this(teacher.getName(), teacher.getWorkingHours(), teacher.getLicenceTypes());
    }

    public DrivingTeacher(String name, int workingHours, ArrayList<LicenceType> licenceTypes) {
        this.name = name;
        this.workingHours = workingHours;
        if(licenceTypes != null)
            this.licenceTypes = licenceTypes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public void setLicenceTypes(ArrayList<LicenceType> licenceTypes) {
        this.licenceTypes = licenceTypes;
    }

    public String getName() {
        return name;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public ArrayList<LicenceType> getLicenceTypes() {
        return licenceTypes;
    }
}
