package gui;

import fileControl.SettingsFileManager;
import general.DrivingTeacher;
import general.LicenceType;
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

    //DrivingTeacher Dialog Fields
    @FXML Tab drivingTeacherTab;
    @FXML ListView drivingTeacherListView;
    @FXML TextField drivingTeacherName;
    @FXML Spinner<Integer> drivingTeacherMaxWorkingHours;
    @FXML CheckBox drivingTeacherLicence_A;
    @FXML CheckBox drivingTeacherLicence_B;
    @FXML CheckBox drivingTeacherLicence_C;
    @FXML CheckBox drivingTeacherLicence_D;
    @FXML MenuItem drivingTeacherMenuItemEdit;
    @FXML MenuItem drivingTeacherMenuItemDelete;

    //User Dialog Fields
    @FXML Tab userTab;

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
        if(result.isEmpty())
            return 2;
        if (result.get() == buttonTypeYes) {
            return 0;
        } else if (result.get() == buttonTypeNo) {
            return 1;
        } else {
            return 2;
        }
    }

    //Displays an error-message( errorMessage)
    private static void showErrorMessage(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Folgender Fehler ist Aufgetreten");
        alert.setContentText(errorMessage);

        alert.showAndWait();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO echten Pfad anhand der Registry abfragen
        fileManager = new SettingsFileManager("C:\\Users\\blaschek\\IdeaProjects\\FahrschulKalendar\\src\\fileControl\\XmlVorlage.xml");
        fileManager.read();

        endEditButton.setVisible(false);
        saveButton.setDisable(true);
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

        //Disable all DrivingTeachers Settings
        }else if(currentSettingPage == 1){
            drivingTeacherMaxWorkingHours.setDisable(disable);
            drivingTeacherLicence_A.setDisable(disable);
            drivingTeacherLicence_B.setDisable(disable);
            drivingTeacherLicence_C.setDisable(disable);
            drivingTeacherLicence_D.setDisable(disable);
            if(disable)
                drivingTeacherName.setDisable(disable);
            //Disable all User Settings
        }else if(currentSettingPage == 2) {

        }
    }

    private void refreshCurrentListView(){
        boolean dontDeaktivateEventCanceler = false;
        if(cancelListViewSelChangeHandler == true)
            dontDeaktivateEventCanceler = true;
        cancelListViewSelChangeHandler = true;

        if(currentSettingPage == 0){
        }else if(currentSettingPage == 1){
            refreshDrivingTeacherListView();
        }else if(currentSettingPage == 2) {

        }

        if(!dontDeaktivateEventCanceler)
            cancelListViewSelChangeHandler = false;
    }

    private void handleChangeListViewSell(){
        if(cancelListViewSelChangeHandler)
            return;

        if(currentSettingPage == 0){
        }else if(currentSettingPage == 1){
            handleChangeDrivingTeacherSell();
        }else if(currentSettingPage == 2) {

        }

    }

    //Takes the Data from the fileManager and write them in the Dialog
    //if reloadFile true the file will be reloaded first
    private void readDataToDialog(boolean reloadFile){
        if(currentSettingPage == 0){
        }else if(currentSettingPage == 1){
            readDrivingTeacherData(reloadFile);
        }else if(currentSettingPage == 2) {

        }

    }

    //Methods only for the Page Vehicles
//-----------------------------------------//
    //Initialize all Dialog Listeners and Dialog items
    private void initVehiclesTab(){

    }

    @FXML
    private void handleChangeToVehiclesTab(){
        initVehiclesTab();
        currentSettingPage = 0;
        refreshCurrentListView();
        disableCurrentPage(true);

    }

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

        drivingTeacherLicence_A.setSelected(false);
        drivingTeacherLicence_B.setSelected(false);
        drivingTeacherLicence_C.setSelected(false);
        drivingTeacherLicence_D.setSelected(false);
        for(int i = 0; i < currentTeacher.getLicenceTypes().size(); i++){
            LicenceType licenceType = currentTeacher.getLicenceTypes().get(i);
            switch (licenceType){
                case LICENCE_TYPE_A:    drivingTeacherLicence_A.setSelected(true); break;
                case LICENCE_TYPE_B:    drivingTeacherLicence_B.setSelected(true); break;
                case LICENCE_TYPE_C:    drivingTeacherLicence_C.setSelected(true); break;
                case LICENCE_TYPE_D:    drivingTeacherLicence_D.setSelected(true); break;
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
            showErrorMessage("Der Name darf nicht lehr sein!");
            return false;
        }
        ArrayList<DrivingTeacher> drivingTeacherList = fileManager.getDrivingTeachers();

        int curSelIndex = drivingTeacherListView.getSelectionModel().getSelectedIndex();
        for(int i = 0; i < drivingTeacherList.size(); i++){
            if(i == curSelIndex)
                continue;
            if(drivingTeacherList.get(i).getName().equals(drivingTeacherName.getText())){
                showErrorMessage("Dieser Name existiert bereits!");
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
            if(drivingTeacherLicence_A.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_A);
            if(drivingTeacherLicence_B.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_B);
            if(drivingTeacherLicence_C.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_C);
            if(drivingTeacherLicence_D.isSelected())
                licenceTypes.add(LicenceType.LICENCE_TYPE_D);

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
        if(result.isEmpty())
            return;
        if (result.get() == buttonTypeYes) {
            fileManager.getDrivingTeachers().remove(drivingTeacherListView.getSelectionModel().getSelectedIndex());
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
        }else if(currentSettingPage == 1){
            if(!saveDrivingTeachers())
                return false;
        }else if(currentSettingPage == 2) {

        }

        endEditMode(true);
        return true;
    }

    @FXML
    private void handleEndEdit(){
        endEditMode(false);
        fileManager.read();
        refreshCurrentListView();
    }
}

