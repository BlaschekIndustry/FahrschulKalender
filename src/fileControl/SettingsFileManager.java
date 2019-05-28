package fileControl;

import general.DrivingTeacher;
import general.LicenceType;
import general.User;
import general.Vehicle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsFileManager extends FileReadWriteManager{
    private ArrayList<DrivingTeacher> drivingTeachers;
    private ArrayList<User> users;
    private ArrayList<Vehicle> vehicles;

    public SettingsFileManager(String fileName) {
        super(fileName);
    }

    @Override
    public void read() {
        try {
            File inputFile = new File(fileName);
            if (!inputFile.exists())
                return;
            document = dBuilder.parse(inputFile);
            document.getDocumentElement().normalize();

        } catch (SAXException e) {
            System.out.println("Fehler beim Lesen der Datei");
            return;
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei");
            return;
        }

        drivingTeachers = new ArrayList<>();
        users = new ArrayList<>();
        vehicles = new ArrayList<>();
        readDrivingTeachers();
        //TODO read Users
        //TODO read Vehicles
    }


    public void readDrivingTeachers(){

        NodeList nList = document.getElementsByTagName(DrivingTeacher.XML_TEACHER_IDENT);
        if(nList == null)
            return;
        for(int i = 0; i < nList.getLength(); i++){
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                //Read Name
                String name = eElement.getElementsByTagName(DrivingTeacher.XML_TEACHER_NAME_IDENT).item(0).getTextContent();
                //Read the weakly working hours
                int workingHours = Integer.parseInt(eElement.getElementsByTagName(DrivingTeacher.XML_TEACHER_WORKINGHOURS_IDENT).item(0).getTextContent());

                //Read the Licences
                NodeList nLicenceList = eElement.getElementsByTagName(DrivingTeacher.XML_TEACHER_LICENCETYPE_IDENT);
                ArrayList<LicenceType> licenceTypes = new ArrayList<>();
                for(int a = 0; a < nLicenceList.getLength(); a++){
                    //TODO add all the licence types
                    Node nLicenceNode = nLicenceList.item(a);
                    Element eLicenceElement = (Element) nLicenceNode;

                }
                DrivingTeacher newTeacher = new DrivingTeacher(name, workingHours, licenceTypes);
                drivingTeachers.add(newTeacher);
            }
        }
    }

    @Override
    public void write() {
        if(drivingTeachers == null)
            drivingTeachers = new ArrayList<>();
        if(users == null)
            users = new ArrayList<>();
        if(vehicles == null)
            vehicles = new ArrayList<>();

        document = dBuilder.newDocument();

        // root element
        Element rootElement = document.createElement("settings");
        document.appendChild(rootElement);

        writeDrivingTeachers(rootElement);
        //TODO write Users
        //TODO write Vehicles
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

        }catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void writeDrivingTeachers(Element rootElement){
        // supercars element
        Element drivingTeachersGroup = document.createElement(DrivingTeacher.XML_TEACHER_GROUP_IDENT);
        rootElement.appendChild(drivingTeachersGroup);

        for(int i = 0; i < drivingTeachers.size(); i++){
            DrivingTeacher teacher = drivingTeachers.get(i);

            Element drivingTeacher = document.createElement(DrivingTeacher.XML_TEACHER_IDENT);
            drivingTeachersGroup.appendChild(drivingTeacher);

            Element drivingTeacherName = document.createElement(DrivingTeacher.XML_TEACHER_NAME_IDENT);
            drivingTeacherName.appendChild(document.createTextNode(teacher.getName()));
            drivingTeacher.appendChild(drivingTeacherName);

            Element drivingTeacherWorkingHours = document.createElement(DrivingTeacher.XML_TEACHER_WORKINGHOURS_IDENT);
            drivingTeacherWorkingHours.appendChild(document.createTextNode(Integer.toString(teacher.getWorkingHours())));
            drivingTeacher.appendChild(drivingTeacherWorkingHours);

            Element drivingTeacherLicence = document.createElement(DrivingTeacher.XML_TEACHER_LICENCETYPE_IDENT);
            for(int a = 0; a < teacher.getLicenceTypes().size(); a++){
                LicenceType licenceType = teacher.getLicenceTypes().get(a);
                drivingTeacherLicence.appendChild(document.createTextNode(LicenceType.XMLNameOfType(licenceType)));
            }
            drivingTeacher.appendChild(drivingTeacherLicence);
        }
    }

    public ArrayList<DrivingTeacher> getDrivingTeachers() {
        if(drivingTeachers == null)
            drivingTeachers = new ArrayList<>();

        return drivingTeachers;
    }

    public ArrayList<User> getUsers() {
        if(users == null)
            users = new ArrayList<>();
        return users;
    }

    public ArrayList<Vehicle> getVehicles() {
        if(vehicles == null)
            vehicles = new ArrayList<>();
        return vehicles;
    }


    public void setDrivingTeachers(ArrayList<DrivingTeacher> drivingTeachers) {
        this.drivingTeachers = drivingTeachers;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
