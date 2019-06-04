package fileControl;

import general.DrivingTeacher;
import general.LicenceType;

import java.util.ArrayList;

public class MainTest {
    public static void main(String[] args){
        SettingsFileManager fileManager = new SettingsFileManager("C:\\Users\\blaschek\\IdeaProjects\\FahrschulKalendar\\src\\fileControl\\XmlVorlage.xml");
        fileManager.read();
        ArrayList<DrivingTeacher> list = fileManager.getDrivingTeachers();
        ArrayList<LicenceType> licenceTypes = new ArrayList<>();
        System.out.println(list.get(0).getWorkingHours());
//        for(DrivingTeacher e : list){
//            System.out.println(e.getName());
//            System.out.println(e.getWorkingHours());
//            System.out.println(e.getLicenceTypes().size());
//            e.getLicenceTypes().add(LicenceType.LICENCE_TYPE_A);
//        }
        //fileManager.write();
    }
}
