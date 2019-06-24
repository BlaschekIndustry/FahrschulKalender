package fileControl;

import general.*;
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
    private ArrayList<DrivingTeacher> drivingTeachers = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();;


    public SettingsFileManager(String fileName) {
        super(fileName);
    }

    @Override
    public void read() {
        try {
            File inputFile = new File(fileName);
            if (!inputFile.exists()) {
                ErrorDialogs.showErrorMessage("Das Einstellungsdokument: \"" + fileName + "\" existiert nicht!");

            }else {
                document = dBuilder.parse(inputFile);
                document.getDocumentElement().normalize();
            }

        } catch (SAXException e) {
            ErrorDialogs.showErrorMessage("Fehler beim Lesen der Datei");
        } catch (IOException e) {
            ErrorDialogs.showErrorMessage("Fehler beim Lesen der Datei");
        }

        readDrivingTeachers();
        readUsers();
        readVehicles();
    }

    public void readVehicles(){
        vehicles.clear();
        NodeList nList = document.getElementsByTagName(Vehicle.XML_VEHICLES_IDENT);
        if(nList == null)
            return;
        for(int i = 0; i < nList.getLength(); i++){
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Node nodeName = eElement.getElementsByTagName(Vehicle.XML_VEHICLES_NAME_IDENT).item(0);
                Node nodeIsExamPermit = eElement.getElementsByTagName(Vehicle.XML_VEHICLES_IS_EXAM_PERMITT).item(0);
                Node nodeIsTrailer = eElement.getElementsByTagName(Vehicle.XML_VEHICLES_IS_TRAILER).item(0);

                if(nodeName == null || nodeIsExamPermit == null || nodeIsTrailer == null){
                    ErrorDialogs.showErrorMessage("Fehler beim Lesen der Fahrzeuge!");
                    continue;
                }

                //Read name
                String name                 = nodeName.getTextContent();
                String isExamPermitText     = nodeIsExamPermit.getTextContent();
                String isTrailerText        = nodeIsTrailer.getTextContent();

                boolean isExamPermit = false;
                boolean isTrailer = false;

                if(isExamPermitText.equals("1"))
                    isExamPermit = true;
                if(isTrailerText.equals("1"))
                    isTrailer = true;

                //Read the Licences
                ArrayList<LicenceType> licenceTypes = new ArrayList<>();

                NodeList nLicenceList = eElement.getElementsByTagName(Vehicle.XML_VEHICLES_LICENCETYPE_IDENT);
                for(int a = 0; a < nLicenceList.getLength(); a++){
                    Node nLicenceNode = nLicenceList.item(a);
                    Element eLicenceElement = (Element) nLicenceNode;
                    String strLicence = eLicenceElement.getTextContent();
                    LicenceType type = LicenceType.TypeOfXMLName(strLicence);
                    if(type == null) {
                        ErrorDialogs.showErrorMessage("Fehler beim Lesen der Lizenzen des Fahrzeuges: " + name);
                        continue;
                    }
                    licenceTypes.add(type);
                }

                Vehicle newVehicle = new Vehicle(name, isExamPermit, isTrailer, licenceTypes);
                vehicles.add(newVehicle);
            }
        }
    }


    public void readDrivingTeachers(){
        drivingTeachers.clear();
        NodeList nList = document.getElementsByTagName(DrivingTeacher.XML_TEACHER_IDENT);
        if(nList == null)
            return;
        for(int i = 0; i < nList.getLength(); i++){
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Node nodeName = eElement.getElementsByTagName(DrivingTeacher.XML_TEACHER_NAME_IDENT).item(0);
                Node nodeWorkingHours = eElement.getElementsByTagName(DrivingTeacher.XML_TEACHER_WORKINGHOURS_IDENT).item(0);

                if(nodeName == null || nodeWorkingHours == null){
                    ErrorDialogs.showErrorMessage("Fehler beim Lesen der Fahrlehrer!");
                    continue;
                }

                //Read name
                String name = nodeName.getTextContent();

                //Read the weakly working hours
                String workingHoursString = nodeWorkingHours.getTextContent();
                int workingHours = 0;
                try {
                    workingHours = Integer.parseInt(nodeWorkingHours.getTextContent());
                }catch (Exception e){
                    ErrorDialogs.showErrorMessage("Fehler beim Lesen der Wochenstunden des Fahrlehrers: " + name);
                    continue;
                }
                //Read the Licences
                ArrayList<LicenceType> licenceTypes = new ArrayList<>();

                NodeList nLicenceList = eElement.getElementsByTagName(DrivingTeacher.XML_TEACHER_LICENCETYPE_IDENT);
                for(int a = 0; a < nLicenceList.getLength(); a++){
                    Node nLicenceNode = nLicenceList.item(a);
                    Element eLicenceElement = (Element) nLicenceNode;
                    String strLicence = eLicenceElement.getTextContent();
                    LicenceType type = LicenceType.TypeOfXMLName(strLicence);
                    if(type == null) {
                        ErrorDialogs.showErrorMessage("Fehler beim Lesen der Lizenzen des Fahrlehrers: " + name);
                        continue;
                    }
                    licenceTypes.add(type);
                }

                DrivingTeacher newTeacher = new DrivingTeacher(name, workingHours, licenceTypes);
                drivingTeachers.add(newTeacher);
            }
        }
    }

    public void readUsers(){
        users.clear();
        NodeList nList = document.getElementsByTagName(User.XML_USERR_IDENT);
        if(nList == null)
            return;
        for(int i = 0; i < nList.getLength(); i++){
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Node nodeName = eElement.getElementsByTagName(User.XML_USER_NAME_IDENT).item(0);
                Node nodeEditVehicles = eElement.getElementsByTagName(User.XML_USER_ALLOW_EDIT_VEHICLES).item(0);
                Node nodeEditTeachers = eElement.getElementsByTagName(User.XML_USER_ALLOW_EDIT_TEACHERS).item(0);
                Node nodeEditUsers = eElement.getElementsByTagName(User.XML_USER_ALLOW_EDIT_USERS).item(0);
                Node nodeInsertOtherEvents = eElement.getElementsByTagName(User.XML_USER_ALLOW_INSERT_OTHER_EVENTS).item(0);

                if(nodeName == null || nodeEditVehicles == null || nodeEditTeachers == null || nodeEditUsers == null || nodeInsertOtherEvents == null){
                    ErrorDialogs.showErrorMessage("Fehler beim Lesen der Benutzer");
                    continue;
                }

                //Read Strings from Nodes
                String name = nodeName.getTextContent();
                String editVehiclesText = nodeEditVehicles.getTextContent();
                String editTeachersText = nodeEditTeachers.getTextContent();
                String editUsersText    = nodeEditUsers.getTextContent();
                String insertOtherEventsText  = nodeInsertOtherEvents.getTextContent();

                boolean editVehicles = false;
                boolean editTeachers = false;
                boolean editUsers = false;
                boolean insertOtherEvents = false;

                if(editVehiclesText.equals("1"))
                    editVehicles = true;
                if(editTeachersText.equals("1"))
                    editTeachers = true;
                if(editUsersText.equals("1"))
                    editUsers = true;
                if(insertOtherEventsText.equals("1"))
                    insertOtherEvents = true;


                User newUser = new User(name, editVehicles, editTeachers, editUsers, insertOtherEvents);
                users.add(newUser);
            }
        }
    }

    @Override
    public void write() {
        document = dBuilder.newDocument();

        // root element
        Element rootElement = document.createElement("settings");
        document.appendChild(rootElement);

        writeVehicles(rootElement);
        writeDrivingTeachers(rootElement);
        writeUsers(rootElement);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

        }catch (TransformerConfigurationException e) {
            ErrorDialogs.showErrorMessage("Fehler beim schreiben der Datei:\"" + fileName + "\"");
            e.printStackTrace();
        } catch (TransformerException e) {
            ErrorDialogs.showErrorMessage("Fehler beim schreiben der Datei:\"" + fileName + "\"");
            e.printStackTrace();
        }
    }

    private void writeVehicles(Element rootElement){
        // supercars element
        Element vehiclesGroup = document.createElement(Vehicle.XML_VEHICLES_GROUP_IDENT);
        rootElement.appendChild(vehiclesGroup);

        for(int i = 0; i < vehicles.size(); i++){
            Vehicle vehicle = vehicles.get(i);

            Element vehicleElement = document.createElement(Vehicle.XML_VEHICLES_IDENT);
            vehiclesGroup.appendChild(vehicleElement);

            Element vehicleName = document.createElement(Vehicle.XML_VEHICLES_NAME_IDENT);
            vehicleName.appendChild(document.createTextNode(vehicle.getName()));
            vehicleElement.appendChild(vehicleName);

            Element isExamPermit = document.createElement(Vehicle.XML_VEHICLES_IS_EXAM_PERMITT);
            isExamPermit.appendChild(document.createTextNode(vehicle.isExamPermit() ? "1" : "0"));
            vehicleElement.appendChild(isExamPermit);

            Element isTrailer = document.createElement(Vehicle.XML_VEHICLES_IS_TRAILER);
            isTrailer.appendChild(document.createTextNode(vehicle.isTrailer() ? "1" : "0"));
            vehicleElement.appendChild(isTrailer);

            for(int a = 0; a < vehicle.getLicenceTypes().size(); a++){
                Element drivingTeacherLicence = document.createElement(Vehicle.XML_VEHICLES_LICENCETYPE_IDENT);
                LicenceType licenceType = vehicle.getLicenceTypes().get(a);
                drivingTeacherLicence.appendChild(document.createTextNode(LicenceType.XMLNameOfType(licenceType)));
                vehicleElement.appendChild(drivingTeacherLicence);
            }

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

            for(int a = 0; a < teacher.getLicenceTypes().size(); a++){
                Element drivingTeacherLicence = document.createElement(DrivingTeacher.XML_TEACHER_LICENCETYPE_IDENT);
                LicenceType licenceType = teacher.getLicenceTypes().get(a);
                drivingTeacherLicence.appendChild(document.createTextNode(LicenceType.XMLNameOfType(licenceType)));
                drivingTeacher.appendChild(drivingTeacherLicence);
            }

        }
    }

    private void writeUsers(Element rootElement){
        // supercars element
        Element userTeachersGroup = document.createElement(User.XML_USER_GROUP_IDENT);
        rootElement.appendChild(userTeachersGroup);

        for(int i = 0; i < users.size(); i++){
            User user = users.get(i);

            Element userEl = document.createElement(User.XML_USERR_IDENT);
            userTeachersGroup.appendChild(userEl);

            Element userName = document.createElement(User.XML_USER_NAME_IDENT);
            userName.appendChild(document.createTextNode(user.getName()));
            userEl.appendChild(userName);

            Element userAllowEditVehicles = document.createElement(User.XML_USER_ALLOW_EDIT_VEHICLES);
            userAllowEditVehicles.appendChild(document.createTextNode(user.canEditVehicles() ? "1" : "0"));
            userEl.appendChild(userAllowEditVehicles);

            Element userAllowEditTeachers = document.createElement(User.XML_USER_ALLOW_EDIT_TEACHERS);
            userAllowEditTeachers.appendChild(document.createTextNode(user.canEditTeachers() ? "1" : "0"));
            userEl.appendChild(userAllowEditTeachers);

            Element userAllowEditUsers = document.createElement(User.XML_USER_ALLOW_EDIT_USERS);
            userAllowEditUsers.appendChild(document.createTextNode(user.canEditUsers() ? "1" : "0"));
            userEl.appendChild(userAllowEditUsers);

            Element userAllowInsertOtherEvents = document.createElement(User.XML_USER_ALLOW_INSERT_OTHER_EVENTS);
            userAllowInsertOtherEvents.appendChild(document.createTextNode(user.canInsertOtherEvents() ? "1" : "0"));
            userEl.appendChild(userAllowInsertOtherEvents);
        }
    }


    public ArrayList<DrivingTeacher> getDrivingTeachers() {
        return drivingTeachers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Vehicle> getVehicles() {
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
