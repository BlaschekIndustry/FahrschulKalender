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

public class ControllerSettings implements Initializable {
    @FXML Button cancelButton;
    @FXML Button saveButton;

    //DrivingTeacher Dialog Fields
    @FXML ListView drivingTeacherListView;
    @FXML TextField drivingTeacherName;
    @FXML Spinner<Integer> drivingTeacherMaxWorkingHours;
    @FXML CheckBox drivingTeacherLicence_A;
    @FXML CheckBox drivingTeacherLicence_B;
    @FXML CheckBox drivingTeacherLicence_C;
    @FXML CheckBox drivingTeacherLicence_D;

    private boolean valueInitializing;
    private int currentSettingPage; // 0 = Vehicles, 1 = DrivingTeachers, 2 = Users, 3 = General
    private int lastSelectionOfListView;
    private boolean listViewSelChangeCancelHandler;
    private boolean editMode;
    private Stage stage;
    SettingsFileManager fileManager;

    private int showSaveDialoge(){
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

    private void showErrorMessage(String errorMessage){
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

        saveButton.setDisable(true);
    }

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;
        stage.showingProperty().addListener((observableValue, aBoolean, t1) -> handleShowStage());
        stage. setOnCloseRequest(windowEvent -> handleCancel());
        stage.setTitle("Einstellungen");

        //DrivingTeacher Listeners
        drivingTeacherListView.getSelectionModel().selectedItemProperty().addListener(observable -> handleChangeDrivingTeacherCell());
        drivingTeacherMaxWorkingHours.valueProperty().addListener(observable -> startEditMode());

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

            //Disable all User Settings
        }else if(currentSettingPage == 2) {

        }
    }



//Methods only for the Page Driving Teacher
//-----------------------------------------//

    private void refreshDrivingTeacherListView(){
        ArrayList<DrivingTeacher> drivingTeacherList = fileManager.getDrivingTeachers();
        int selectionIndex = drivingTeacherListView.getSelectionModel().getSelectedIndex();
        drivingTeacherListView.getItems().clear();
        for(int i = 0; i < drivingTeacherList.size(); i++) {
            drivingTeacherListView.getItems().add(i, drivingTeacherList.get(i).getName());
        }
        if(drivingTeacherList.size()>selectionIndex)
            drivingTeacherListView.getSelectionModel().select(selectionIndex);
    }

    private void readDrivingTeacherData(boolean refreshListView){
        fileManager.readDrivingTeachers();
        if(refreshListView)
            refreshDrivingTeacherListView();
        valueInitializing = true;
        int selIndex = drivingTeacherListView.getSelectionModel().getSelectedIndex();
        ArrayList<DrivingTeacher> drivingTeacherList = fileManager.getDrivingTeachers();
        DrivingTeacher currentTeacher = drivingTeacherList.get(selIndex);

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
        valueInitializing = false;
    }

    @FXML
    private void handleChangeToDrivingTeacherTab(){
        currentSettingPage = 1;
        refreshDrivingTeacherListView();
        disableCurrentPage(true);
        drivingTeacherName.setDisable(true);
    }

    @FXML
    private void handleChangeDrivingTeacherCell(){
        if(listViewSelChangeCancelHandler)
            return;
        if(!endEditMode(false)){
            listViewSelChangeCancelHandler = true;
            drivingTeacherListView.getSelectionModel().select(lastSelectionOfListView);
            listViewSelChangeCancelHandler = false;
        }

        readDrivingTeacherData(false);
        endEditMode(true);
        lastSelectionOfListView = drivingTeacherListView.getSelectionModel().getSelectedIndex();
    }

    private boolean checkDrivingTeachersInputData(){
        if(drivingTeacherName.getText().isEmpty()){
            showErrorMessage("Der Name darf nicht lehr sein!");
            return false;
        }

        return true;
    }

    private boolean saveDrivingTeachers(){
        if(currentSettingPage == 1) {
            if(!checkDrivingTeachersInputData())
                return false;
            int selIndex = drivingTeacherListView.getSelectionModel().getSelectedIndex();
            ArrayList<DrivingTeacher> drivingTeacherList = fileManager.getDrivingTeachers();
            DrivingTeacher currentTeacher = drivingTeacherList.get(selIndex);

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
        }
        return true;
    }

    @FXML
    private void handleNewDrivingTeacher(){
        ArrayList<DrivingTeacher> drivingTeacherList = fileManager.getDrivingTeachers();
        DrivingTeacher newDrivingTeacher = new DrivingTeacher();
        drivingTeacherList.add(newDrivingTeacher);
        refreshDrivingTeacherListView();
        drivingTeacherListView.getSelectionModel().select(drivingTeacherList.size()-1);

        drivingTeacherName.setDisable(false);
        startEditMode();
    }
//-----------------------------------------//

    @FXML
    private void handleCancel(){
        if(editMode) {
            int saveDialogeResult = showSaveDialoge();
            if (saveDialogeResult == 0) {
                handleSave();
            } else if (saveDialogeResult == 2) {
                return;
            }
            editMode = false;
        }
        ((Stage)stage.getOwner()).show();
        stage.close();
    }

    @FXML
    private void startEditMode(){
        if(valueInitializing)
            return;
        editMode = true;
        saveButton.setDisable(false);
        disableCurrentPage(false);
    }

    private boolean endEditMode(boolean hasSaved){
        if(editMode) {
            if (!hasSaved) {
                int saveDialogeResult = showSaveDialoge();
                if (saveDialogeResult == 0) {
                    handleSave();
                } else if (saveDialogeResult == 2) {
                    return false;
                }
            }
            editMode = false;
            saveButton.setDisable(true);
            disableCurrentPage(true);
            readDrivingTeacherData(true);
        }
        return true;
    }

    @FXML
    private void handleSave(){
        if(!saveDrivingTeachers())
            return;

        endEditMode(true);
        saveButton.setDisable(true);

    }
}

