package fileControl;

import general.DrivingTeacher;
import general.LicenceType;

import java.util.ArrayList;

public class MainTest {
    public static void main(String[] args){
        SettingsFileManager fileManager = new SettingsFileManager("D:\\Java Projects\\FahrschulKalender\\src\\fileControl\\XmlVorlage.xml");
        fileManager.read();
        ArrayList<DrivingTeacher> list = fileManager.getDrivingTeachers();
        ArrayList<LicenceType> licenceTypes = new ArrayList<>();
        licenceTypes.add(LicenceType.LICENCE_TYPE_A);
        licenceTypes.add(LicenceType.LICENCE_TYPE_B);
        list.add(new DrivingTeacher("Test", 39,licenceTypes ));
//        for(DrivingTeacher e : list){
//            System.out.println(e.getName());
//            System.out.println(e.getWorkingHours());
//            System.out.println(e.getLicenceTypes().size());
//            e.getLicenceTypes().add(LicenceType.LICENCE_TYPE_A);
//        }
        fileManager.write();
    }
}
