package gui;

import fileControl.SettingsFileManager;
import general.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class ControllerSettings implements Initializable {
    //General Dialog Fields
    @FXML Button cancelButton;
    @FXML Button saveButton;
    @FXML TabPane settingTabPane;
    @FXML Button endEditButton;

    //Vehicles Dialog Fields
    @FXML Tab vehiclesTab;
    @FXML ListView vehicleListView;
    @FXML TextField vehicleName;
    @FXML CheckBox vehicleIsExamPermit;
    @FXML CheckBox vehicleIsTrailer;
    @FXML CheckBox vehicleIsAutomatic;
    @FXML MenuItem vehicleMenuItemEdit;
    @FXML MenuItem vehicleMenuItemDelete;
    @FXML CheckBox vehicleLicence_AM;
    @FXML CheckBox vehicleLicence_A1;
    @FXML CheckBox vehicleLicence_A2;
    @FXML CheckBox vehicleLicence_A;
    @FXML CheckBox vehicleLicence_B;
    @FXML CheckBox vehicleLicence_BE;

    //DrivingTeacher Dialog Fields
    @FXML Tab drivingTeacherTab;
    @FXML ListView drivingTeacherListView;
    @FXML TextField drivingTeacherName;
    @FXML Spinner<Integer> drivingTeacherMaxWorkingHours;
    @FXML CheckBox drivingTeacherLicence_AM;
    @FXML CheckBox drivingTeacherLicence_A1;
    @FXML CheckBox drivingTeacherLicence_A2;
    @FXML CheckBox drivingTeacherLicence_A;
    @FXML CheckBox drivingTeacherLicence_B;
    @FXML CheckBox drivingTeacherLicence_BE;
    @FXML MenuItem drivingTeacherMenuItemEdit;
    @FXML MenuItem drivingTeacherMenuItemDelete;

    //User Dialog Fields
    @FXML Tab userTab;
    @FXML ListView userListView;
    @FXML TextField userName;
    @FXML CheckBox userAllowEditVehicles;
    @FXML CheckBox userAllowEditTeachers;
    @FXML CheckBox userAllowEditUsers;
    @FXML CheckBox userAllowInsertOtherEvent;
    @FXML MenuItem userMenuItemEdit;
    @FXML MenuItem userMenuItemDelete;

    //General Dialog Fields
    @FXML Tab generalTab;

    //General Attributes
    private int currentSettingPage; // 0 = Vehicles, 1 = DrivingTeachers, 2 = Users, 3 = General
    private int lastSelectionOfListView;
    private boolean cancelListViewSelChangeHandler; // if true deaktivate the Handler for the ListView ItemChange
    private boolean editMode;
    private Stage stage;
    private SettingsFileManager fileManager;

    //Displays an Confirmation Dialog where the user Can select to Save (returnValue: 0),
    // not to Save(returnValue: 1),
    // or Cancel(returnValue: 2)
    private static int showSaveDialoge(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Speichern");
        alert.setHeaderText("Wollen Sie Ihre Einstellungen Speichern?");
        alert.setContentText("Bei \"Nein\" gehen all Ihre letzten Einstellungen verloren");

        ButtonType buttonTypeYes = new ButtonType("Ja");
        ButtonType buttonTypeNo = new ButtonType("Nein");
        ButtonType buttonTypeCancel = new ButtonType("Abbruch", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            return 0;
        } else if (result.get() == buttonTypeNo) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO echten Pfad anhand der Registry abfragen
        fileManager = new SettingsFileManager("D:\\Java Projects\\FahrschulKalender\\src\\fileControl\\XmlVorlage.xml ");
        fileManager = new SettingsFileManager();
        fileManager.read();

        endEditButton.setVisible(false);
        saveButton.setDisable(true);
        handleChangeToVehiclesTab();
    }

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;
        stage.showingProperty().addListener((observableValue, aBoolean, t1) -> handleShowStage());
        stage. setOnCloseRequest(windowEvent -> handleCancel());
        stage.setTitle("Einstellungen");
    }

    private void handleShowStage(){
        stage.setMaxWidth(stage.getWidth());
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        stage.setMaxHeight(stage.getHeight());
    }

    private void disableCurrentPage(boolean disable){
        //Disable all Vehicle Settings
        if(currentSettingPage == 0){
            vehicleIsExamPermit.setDisable(disable);
            vehicleIsTrailer.setDisable(disable);
            vehicleIsAutomatic.setDisable(disable);
            vehicleLicence_AM.setDisable(disable);
            vehicleLicence_A1.setDisable(disable);
            vehicleLicence_A2.setDisable(disable);
            vehicleLicence_A.setDisable(disable);
            vehicleLicence_B.setDisable(disable);
            vehicleLicence_BE.setDisable(disable);
            if(disable)
                vehicleName.setDisable(disable);
        //Disable all DrivingTeachers Settings
        }else if(currentSettingPage == 1){
            drivingTeacherMaxWorkingHours.setDisable(disable);
            drivingTeacherLicence_AM.setDisable(disable);
            drivingTeacherLicence_A1.setDisable(disable);
            drivingTeacherLicence_A2.setDisable(disable);
            drivingTeacherLicence_A.setDisable(disable);
            drivingTeacherLicence_B.setDisable(disable);
            drivingTeacherLicence_BE.setDisable(disable);
            if(disable)
                drivingTeacherName.setDisable(disable);
            //Disable all User Settings
        }else if(currentSettingPage == 2) {
            userAllowEditTeachers.setDisable(disable);
            userAllowEditUsers.setDisable(disable);
            userAllowEditVehicles.setDisable(disable);
            userAllowInsertOtherEvent.setDisable(disable);
            if(disable)
                userName.setDisable(disable);
        }
    }

    private void refreshCurrentListView(){
        boolean dontDeaktivateEventCanceler = false;
        if(cancelListViewSelChangeHandler == true)
            dontDeaktivateEventCanceler = true;
        cancelListViewSelChangeHandler = true;

        if(currentSettingPage == 0){
            refreshVehicleListView();
        }else if(currentSettingPage == 1){
            refreshDrivingTeacherListView();
        }else if(currentSettingPage == 2) {
            refreshUserListView();
        }

        if(!dontDeaktivateEventCanceler)
            cancelListViewSelChangeHandler = false;
    }

    private void handleChangeListViewSell(){
        if(cancelListViewSelChangeHandler)
            return;

        if(currentSettingPage == 0){
            handleChangeVehicleSell();
        }else if(currentSettingPage == 1){
            handleChangeDrivingTeacherSell();
        }else if(currentSettingPage == 2) {
            handleChangeUsersSell();
        }

    }

    //Takes the Data from the fileManager and write them in the Dialog
    //if reloadFile true the file will be reloaded first
    private void readDataToDialog(boolean reloadFile){
        if(currentSettingPage == 0){
            readVehicleData(reloadFile);
        }else if(currentSettingPage == 1){
            readDrivingTeacherData(reloadFile);
        }else if(currentSettingPage == 2) {
            readUserData(reloadFile);
        }

    }

    //Methods only for the Page Vehicles
//-----------------------------------------//
    //Initialize all Dialog Listeners and Dialog items
    private void initVehiclesTab(){
        ArrayList<Vehicle> vehicles = fileManager.getVehicles();

        //Vehicle Listeners
        vehicleListView.getSelectionModel().selectedItemProperty().addListener(observable -> handleChangeListViewSell());

        //Disable Dialog Items
        vehicleMenuItemEdit.setVisible(false);
        vehicleMenuItemDelete.setVisible(false);
    }

    @FXML
    private void handleChangeToVehiclesTab(){
        if(fileManager == null)
            return;
        endEditMode(false);
        initVehiclesTab();
        currentSettingPage = 0;
        refreshCurrentListView();
        disableCurrentPage(true);

        vehicleName.setDisable(true);

    }

    //Refreshs the ListView of the Vehicle Tab
    private void refreshVehicleListView(){
        vehicleListView.getItems().clear();
        ArrayList<Vehicle> vehicles = fileManager.getVehicles();
        IntStream.range(0, vehicles.size()).forEach(i -> vehicleListView.getItems().add(vehicles.get(i).getName()));

        //Edit and Delete Menu of the ComboBox Enablen/Disablen
        if(vehicleListView.getItems().size() > 0){
            vehicleMenuItemEdit.setVisible(true);
            vehicleMenuItemDelete.setVisible(true);
        }else {
            vehicleMenuItemEdit.setVisible(false);
            vehicleMenuItemDelete.setVisible(false);
        }
    }

    private void readVehicleData(boolean reloadFile){
        if(reloadFile)
            fileManager.readVehicles();

        ArrayList<Vehicle> vehicles = fileManager.getVehicles();
        Vehicle currentVehicle = vehicles.get(vehicleListView.getSelectionModel().getSelectedIndex());

        //Write data in the Dialoge
        vehicleName.setText(currentVehicle.getName());
        vehicleIsExamPermit.setSelected(currentVehicle.isExamPermit());
        vehicleIsTrailer.setSelected(currentVehicle.isTrailer());
        vehicleIsAutomatic.setSelected(currentVehicle.isAutomatic());

        vehicleLicence_AM.setSelected(false);
        vehicleLicence_A1.setSelected(false);
        vehicleLicence_A2.setSelected(false);
        vehicleLicence_A.setSelected(false);
        vehicleLicence_B.setSelected(false);
        vehicleLicence_BE.setSelected(false);

        for(int i = 0; i < currentVehicle.getLicenceTypes().size(); i++){
            LicenceType licenceType = currentVehicle.getLicenceTypes().get(i);
            switch (licenceType){
                case LICENCE_TYPE_AM:    vehicleLicence_AM.setSelected(true); break;
                case LICENCE_TYPE_A1:    vehicleLicence_A1.setSelected(true); break;
                case LICENCE_TYPE_A2:    vehicleLicence_A2.setSelected(true); break;
                case LICENCE_TYPE_A:     vehicleLicence_A.setSelected(true); break;
                case LICENCE_TYPE_B:     vehicleLicence_B.setSelected(true); break;
                case LICENCE_TYPE_BE:    vehicleLicence_BE.setSelected(true); break;
            }
        }
    }


    @FXML
    private void handleChangeVehicleSell(){
        if(editMode){
            boolean dontDeaktivateEventCanceler = false;
            if(cancelListViewSelChangeHandler == true)
                dontDeaktivateEventCanceler = true;
            cancelListViewSelChangeHandler = true;

            int newSelection = vehicleListView.getSelectionModel().getSelectedIndex();
            vehicleListView.getSelectionModel().select(lastSelectionOfListView);
            if(!endEditMode(false)){
                if(!dontDeaktivateEventCanceler)
                    cancelListViewSelChangeHandler = false;
                return;
            }
            fileManager.readVehicles();
            refreshCurrentListView();
            vehicleListView.getSelectionModel().select(newSelection);
            if(!dontDeaktivateEventCanceler)
                cancelListViewSelChangeHandler = false;
        }

        readVehicleData(true);
        lastSelectionOfListView = vehicleListView.getSelectionModel().getSelectedIndex();
    }

    private boolean checkVehiclesInputData(){
        if(vehicleName.getText().isEmpty()){
            ErrorDialogs.showErrorMessage("Der Name darf nicht lehr sein!");
            return false;
        }
        ArrayList<Vehicle> vehicles = fileManager.getVehicles();

        int curSelIndex = vehicleListView.getSelectionModel().getSelectedIndex();
        for(int i = 0; i < vehicles.size(); i++){
            if(i == curSelIndex)
                continue;
            if(vehicles.get(i).getName().equals(vehicleName.getText())){
                ErrorDialogs.showErrorMessage("Dieser Name existiert bereits!");
                return false;
            }
        }

        return true;
    }

    private boolean saveVehicles(){
        if(currentSettingPage == 0) {
            if(!checkVehiclesInputData()) {
                return false;
            }
            ArrayList<Vehicle> vehicles = fileManager.getVehicles();
            int selIndex = vehicleListView.getSelectionModel().getSelectedIndex();
            Vehicle currentVehicle = vehicles.get(selIndex);

            currentVehicle.setName(vehicleName.getText());
            currentVehicle.setExamPermit(vehicleIsExamPermit.isSelected());
            currentVehicle.setTrailer(vehicleIsTrailer.isSelected());
            currentVehicle.setAutomatic(vehicleIsAutomatic.isSelected());

            ArrayList<LicenceType> licenceTypes = new ArrayList<>();
            if(vehicleLicence_AM.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_AM);
            if(vehicleLicence_A1.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_A1);
            if(vehicleLicence_A2.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_A2);
            if(vehicleLicence_A.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_A);
            if(vehicleLicence_B.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_B);
            if(vehicleLicence_BE.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_BE);

            currentVehicle.setLicenceTypes(licenceTypes);
            fileManager.write();
            fileManager.readVehicles();
            refreshCurrentListView();
        }
        return true;
    }

    @FXML
    private void handleNewVehicle(){
        boolean dontDeaktivateEventCanceler = false;
        if(cancelListViewSelChangeHandler == true)
            dontDeaktivateEventCanceler = true;
        cancelListViewSelChangeHandler = true;

        if(!endEditMode(false)){
            return;
        }

        fileManager.readVehicles();
        refreshCurrentListView();

        ArrayList<Vehicle> vehicles = fileManager.getVehicles();
        Vehicle newVehicle = new Vehicle();
        vehicles.add(newVehicle);

        vehicleListView.getItems().add(vehicleListView.getItems().size(), newVehicle.getName());
        vehicleListView.getSelectionModel().select(vehicles.size()-1);
        lastSelectionOfListView = vehicles.size()-1;

        readVehicleData(false);
        vehicleName.setDisable(false);
        startEditMode();

        if(!dontDeaktivateEventCanceler)
            cancelListViewSelChangeHandler = false;
    }

    @FXML
    private void handleEditVehicles(){
        startEditMode();
    }

    @FXML
    private void handleDeleteVehicles(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Löschen");
        alert.setHeaderText("Wollen Sie dieses Fahrzeug wirklich löschen?");

        ButtonType buttonTypeYes = new ButtonType("Ja");
        ButtonType buttonTypeNo = new ButtonType("Nein", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeYes) {
            fileManager.getVehicles().remove(vehicleListView.getSelectionModel().getSelectedIndex());
            fileManager.write();
            endEditMode(true);
            refreshCurrentListView();
        }
    }
//-----------------------------------------//


//Methods only for the Page Driving Teacher
//-----------------------------------------//
    //Initialize all Dialog Listeners and Dialog items
    private void initDrivingTeacherTab(){
        ArrayList<DrivingTeacher> drivingTeachers = fileManager.getDrivingTeachers();

        //DrivingTeacher Listeners
        drivingTeacherListView.getSelectionModel().selectedItemProperty().addListener(observable -> handleChangeListViewSell());

        //Disable Dialog Items
        drivingTeacherMenuItemEdit.setVisible(false);
        drivingTeacherMenuItemDelete.setVisible(false);
    }

    @FXML
    private void handleChangeToDrivingTeacherTab(){
        endEditMode(false);
        initDrivingTeacherTab();
        currentSettingPage = 1;
        refreshCurrentListView();
        disableCurrentPage(true);

        drivingTeacherName.setDisable(true);
    }

    //Refreshs the ListView of the Driving Teachers Tab
    private void refreshDrivingTeacherListView(){
        drivingTeacherListView.getItems().clear();
        ArrayList<DrivingTeacher> drivingTeachers = fileManager.getDrivingTeachers();
        IntStream.range(0, drivingTeachers.size()).forEach(i -> drivingTeacherListView.getItems().add(drivingTeachers.get(i).getName()));

        //Edit and Delete Menu of the ComboBox Enablen/Disablen
        if(drivingTeacherListView.getItems().size() > 0){
            drivingTeacherMenuItemEdit.setVisible(true);
            drivingTeacherMenuItemDelete.setVisible(true);
        }else {
            drivingTeacherMenuItemEdit.setVisible(false);
            drivingTeacherMenuItemDelete.setVisible(false);
        }
    }

    private void readDrivingTeacherData(boolean reloadFile){
        if(reloadFile)
            fileManager.readDrivingTeachers();

        ArrayList<DrivingTeacher> drivingTeachers = fileManager.getDrivingTeachers();
        DrivingTeacher currentTeacher = drivingTeachers.get(drivingTeacherListView.getSelectionModel().getSelectedIndex());

        //Write data in the Dialoge
        drivingTeacherName.setText(currentTeacher.getName());
        drivingTeacherMaxWorkingHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,60, currentTeacher.getWorkingHours()));

        drivingTeacherLicence_AM.setSelected(false);
        drivingTeacherLicence_A1.setSelected(false);
        drivingTeacherLicence_A2.setSelected(false);
        drivingTeacherLicence_A.setSelected(false);
        drivingTeacherLicence_B.setSelected(false);
        drivingTeacherLicence_BE.setSelected(false);
        for(int i = 0; i < currentTeacher.getLicenceTypes().size(); i++){
            LicenceType licenceType = currentTeacher.getLicenceTypes().get(i);
            switch (licenceType){
                case LICENCE_TYPE_AM:    drivingTeacherLicence_AM.setSelected(true); break;
                case LICENCE_TYPE_A1:    drivingTeacherLicence_A1.setSelected(true); break;
                case LICENCE_TYPE_A2:    drivingTeacherLicence_A2.setSelected(true); break;
                case LICENCE_TYPE_A:     drivingTeacherLicence_A.setSelected(true); break;
                case LICENCE_TYPE_B:     drivingTeacherLicence_B.setSelected(true); break;
                case LICENCE_TYPE_BE:    drivingTeacherLicence_BE.setSelected(true); break;
            }
        }
    }


    @FXML
    private void handleChangeDrivingTeacherSell(){
          if(editMode){
              boolean dontDeaktivateEventCanceler = false;
              if(cancelListViewSelChangeHandler == true)
                  dontDeaktivateEventCanceler = true;
              cancelListViewSelChangeHandler = true;

            int newSelection = drivingTeacherListView.getSelectionModel().getSelectedIndex();
            drivingTeacherListView.getSelectionModel().select(lastSelectionOfListView);
            if(!endEditMode(false)){
                if(!dontDeaktivateEventCanceler)
                    cancelListViewSelChangeHandler = false;
                return;
            }
            fileManager.readDrivingTeachers();
            refreshCurrentListView();
            drivingTeacherListView.getSelectionModel().select(newSelection);
            if(!dontDeaktivateEventCanceler)
                cancelListViewSelChangeHandler = false;
        }

        readDrivingTeacherData(true);
        lastSelectionOfListView = drivingTeacherListView.getSelectionModel().getSelectedIndex();
    }

    private boolean checkDrivingTeachersInputData(){
        if(drivingTeacherName.getText().isEmpty()){
            ErrorDialogs.showErrorMessage("Der Name darf nicht lehr sein!");
            return false;
        }
        ArrayList<DrivingTeacher> drivingTeacherList = fileManager.getDrivingTeachers();

        int curSelIndex = drivingTeacherListView.getSelectionModel().getSelectedIndex();
        for(int i = 0; i < drivingTeacherList.size(); i++){
            if(i == curSelIndex)
                continue;
            if(drivingTeacherList.get(i).getName().equals(drivingTeacherName.getText())){
                ErrorDialogs.showErrorMessage("Dieser Name existiert bereits!");
                return false;
            }
        }

        return true;
    }

    private boolean saveDrivingTeachers(){
        if(currentSettingPage == 1) {
            if(!checkDrivingTeachersInputData()) {
                return false;
            }
            ArrayList<DrivingTeacher> drivingTeachers = fileManager.getDrivingTeachers();
            int selIndex = drivingTeacherListView.getSelectionModel().getSelectedIndex();
            DrivingTeacher currentTeacher = drivingTeachers.get(selIndex);

            currentTeacher.setName(drivingTeacherName.getText());
            currentTeacher.setWorkingHours(drivingTeacherMaxWorkingHours.getValue());

            ArrayList<LicenceType> licenceTypes = new ArrayList<>();
            if(drivingTeacherLicence_AM.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_AM);
            if(drivingTeacherLicence_A1.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_A1);
            if(drivingTeacherLicence_A2.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_A2);
            if(drivingTeacherLicence_A.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_A);
            if(drivingTeacherLicence_B.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_B);
            if(drivingTeacherLicence_BE.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_BE);

            currentTeacher.setLicenceTypes(licenceTypes);
            fileManager.write();
            fileManager.readDrivingTeachers();
            refreshCurrentListView();
        }
        return true;
    }

    @FXML
    private void handleNewDrivingTeacher(){
        boolean dontDeaktivateEventCanceler = false;
        if(cancelListViewSelChangeHandler == true)
            dontDeaktivateEventCanceler = true;
        cancelListViewSelChangeHandler = true;

        if(!endEditMode(false)){
            return;
        }

        fileManager.readDrivingTeachers();
        refreshCurrentListView();

        ArrayList<DrivingTeacher> drivingTeachers = fileManager.getDrivingTeachers();
        DrivingTeacher newDrivingTeacher = new DrivingTeacher();
        drivingTeachers.add(newDrivingTeacher);

        drivingTeacherListView.getItems().add(drivingTeacherListView.getItems().size(), newDrivingTeacher.getName());
        drivingTeacherListView.getSelectionModel().select(drivingTeachers.size()-1);
        lastSelectionOfListView = drivingTeachers.size()-1;

        readDrivingTeacherData(false);
        drivingTeacherName.setDisable(false);
        startEditMode();

        drivingTeacherName.selectAll();

        if(!dontDeaktivateEventCanceler)
            cancelListViewSelChangeHandler = false;
    }

    @FXML
    private void handleEditDrivingTeacher(){
        startEditMode();
    }

    @FXML
    private void handleDeleteDrivingTeacher(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Löschen");
        alert.setHeaderText("Wollen Sie diesen Fahrlehrer wirklich löschen?");

        ButtonType buttonTypeYes = new ButtonType("Ja");
        ButtonType buttonTypeNo = new ButtonType("Nein", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            fileManager.getDrivingTeachers().remove(drivingTeacherListView.getSelectionModel().getSelectedIndex());
            fileManager.write();
            endEditMode(true);
            refreshCurrentListView();
        }
    }
//-----------------------------------------//


    //Methods only for the Page Users
//-----------------------------------------//
    //Initialize all Dialog Listeners and Dialog items
    private void initUsersTab(){
        ArrayList<User> users = fileManager.getUsers();

        //DrivingTeacher Listeners
        userListView.getSelectionModel().selectedItemProperty().addListener(observable -> handleChangeListViewSell());

        //Disable Dialog Items
        userMenuItemEdit.setVisible(false);
        userMenuItemDelete.setVisible(false);
    }

    @FXML
    private void handleChangeToUserTab(){
        endEditMode(false);
        initUsersTab();
        currentSettingPage = 2;
        refreshCurrentListView();
        disableCurrentPage(true);

        userName.setDisable(true);
    }

    //Refreshs the ListView of the Users Tab
    private void refreshUserListView(){
        userListView.getItems().clear();
        ArrayList<User> users = fileManager.getUsers();
        IntStream.range(0, users.size()).forEach(i -> userListView.getItems().add(users.get(i).getName()));

        //Edit and Delete Menu of the ComboBox Enablen/Disablen
        if(userListView.getItems().size() > 0){
            userMenuItemEdit.setVisible(true);
            userMenuItemDelete.setVisible(true);
        }else {
            userMenuItemEdit.setVisible(false);
            userMenuItemDelete.setVisible(false);
        }
    }

    private void readUserData(boolean reloadFile){
        if(reloadFile)
            fileManager.readUsers();

        ArrayList<User> users = fileManager.getUsers();
        User currentUser = users.get(userListView.getSelectionModel().getSelectedIndex());

        //Write data in the Dialoge
        userName.setText(currentUser.getName());
        userAllowEditVehicles.setSelected(currentUser.canEditVehicles());
        userAllowEditTeachers.setSelected(currentUser.canEditTeachers());
        userAllowEditUsers.setSelected(currentUser.canEditUsers());
        userAllowInsertOtherEvent.setSelected(currentUser.canInsertOtherEvents());

    }


    @FXML
    private void handleChangeUsersSell(){
        if(editMode){
            boolean dontDeaktivateEventCanceler = false;
            if(cancelListViewSelChangeHandler == true)
                dontDeaktivateEventCanceler = true;
            cancelListViewSelChangeHandler = true;

            int newSelection = userListView.getSelectionModel().getSelectedIndex();
            userListView.getSelectionModel().select(lastSelectionOfListView);
            if(!endEditMode(false)){
                if(!dontDeaktivateEventCanceler)
                    cancelListViewSelChangeHandler = false;
                return;
            }
            fileManager.readUsers();
            refreshCurrentListView();
            userListView.getSelectionModel().select(newSelection);
            if(!dontDeaktivateEventCanceler)
                cancelListViewSelChangeHandler = false;
        }

        readUserData(true);
        lastSelectionOfListView = userListView.getSelectionModel().getSelectedIndex();
    }

    private boolean checkUsersInputData(){
        if(userName.getText().isEmpty()){
            ErrorDialogs.showErrorMessage("Der Name darf nicht lehr sein!");
            return false;
        }
        ArrayList<User> userList = fileManager.getUsers();

        int curSelIndex = userListView.getSelectionModel().getSelectedIndex();
        for(int i = 0; i < userList.size(); i++){
            if(i == curSelIndex)
                continue;
            if(userList.get(i).getName().equals(userName.getText())){
                ErrorDialogs.showErrorMessage("Dieser Name existiert bereits!");
                return false;
            }
        }

        return true;
    }

    private boolean saveUsers(){
        if(currentSettingPage == 2) {
            if(!checkUsersInputData()) {
                return false;
            }
            ArrayList<User> users = fileManager.getUsers();
            int selIndex = userListView.getSelectionModel().getSelectedIndex();
            User currentUser = users.get(selIndex);

            currentUser.setName(userName.getText());
            currentUser.setEditTeachers(userAllowEditTeachers.isSelected());
            currentUser.setEditVehicles(userAllowEditVehicles.isSelected());
            currentUser.setEditUsers(userAllowEditUsers.isSelected());
            currentUser.setInsertOtherEvents(userAllowInsertOtherEvent.isSelected());

            fileManager.write();
            fileManager.readUsers();
            refreshCurrentListView();
        }
        return true;
    }

    @FXML
    private void handleNewUser(){
        boolean dontDeaktivateEventCanceler = false;
        if(cancelListViewSelChangeHandler == true)
            dontDeaktivateEventCanceler = true;
        cancelListViewSelChangeHandler = true;

        if(!endEditMode(false)){
            return;
        }

        fileManager.readUsers();
        refreshCurrentListView();

        ArrayList<User> users = fileManager.getUsers();
        User newUser = new User();
        users.add(newUser);

        userListView.getItems().add(userListView.getItems().size(), newUser.getName());
        userListView.getSelectionModel().select(users.size()-1);
        lastSelectionOfListView = users.size()-1;

        readUserData(false);
        userName.setDisable(false);
        startEditMode();

        if(!dontDeaktivateEventCanceler)
            cancelListViewSelChangeHandler = false;
    }

    @FXML
    private void handleEditUser(){
        startEditMode();
    }

    @FXML
    private void handleDeleteUser(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Löschen");
        alert.setHeaderText("Wollen Sie diesen Benutzer wirklich löschen?");

        ButtonType buttonTypeYes = new ButtonType("Ja");
        ButtonType buttonTypeNo = new ButtonType("Nein", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            fileManager.getUsers().remove(userListView.getSelectionModel().getSelectedIndex());
            fileManager.write();
            endEditMode(true);
            refreshCurrentListView();
        }
    }
//-----------------------------------------//

    @FXML
    private void handleCancel(){
        endEditMode(false);
        ((Stage)stage.getOwner()).show();
        stage.close();
    }

    //Starts the editMode
    private void startEditMode(){
        editMode = true;
        saveButton.setDisable(false);
        disableCurrentPage(false);
        endEditButton.setVisible(true);

        vehiclesTab.setDisable(true);
        drivingTeacherTab.setDisable(true);
        userTab.setDisable(true);
        generalTab.setDisable(true);
        if(currentSettingPage == 0){
            vehiclesTab.setDisable(false);
        }else if(currentSettingPage == 1){
            drivingTeacherTab.setDisable(false);
        }else if(currentSettingPage == 2) {
            userTab.setDisable(false);
        }else
            generalTab.setDisable(false);
    }

    //Ends the EditMode
    private boolean endEditMode(boolean hasSaved){
        if(editMode) {
            if (!hasSaved) {
                int saveDialogeResult = showSaveDialoge();
                if (saveDialogeResult == 0) {
                    if(!handleSave())
                        return false;
                } else if (saveDialogeResult == 2) {
                    return false;
                }
            }
            editMode = false;
            saveButton.setDisable(true);
            endEditButton.setVisible(false);
            disableCurrentPage(true);

            vehiclesTab.setDisable(false);
            drivingTeacherTab.setDisable(false);
            userTab.setDisable(false);
            generalTab.setDisable(false);
        }
        return true;
    }

    //Saves the Changes
    @FXML
    private boolean handleSave(){
        if(currentSettingPage == 0){
            if(!saveVehicles()){
                return false;
            }
        }else if(currentSettingPage == 1){
            if(!saveDrivingTeachers())
                return false;
        }else if(currentSettingPage == 2) {
            if(!saveUsers())
                return false;
        }

        endEditMode(true);
        return true;
    }

    @FXML
    private void handleEndEdit(){
        if(!endEditMode(false))
            return;
        fileManager.read();
        refreshCurrentListView();
    }
}

