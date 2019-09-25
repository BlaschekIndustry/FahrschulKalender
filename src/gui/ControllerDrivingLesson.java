package gui;

import fileControl.SettingsFileManager;
import general.DrivingTeacher;
import general.LicenceType;
import googleCalendar.CalendarHelper;
import googleCalendar.GoogleCalendar;
import googleCalendar.GoogleEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import static general.LicenceType.TypeOfXMLName;

public class ControllerDrivingLesson implements Initializable {
    @FXML TextField student;

    @FXML Button cancelButton;
    @FXML TableView<TableRow> weekTable;
    @FXML TableColumn tableMo;
    @FXML TableColumn tableTu;
    @FXML TableColumn tableWe;
    @FXML TableColumn tableTh;
    @FXML TableColumn tableFr;
    @FXML TableColumn tableSa;
    private String moColor;


    @FXML ComboBox<String> driverLicence;
    @FXML ComboBox<String> drivingTeacher;

    @FXML ComboBox<String> weekCombo;
    @FXML Button weekForward;
    @FXML Button weekBack;

    private Stage stage;
    SettingsFileManager fileManager;
    Date mondayOfCurrentDate;
    Date mondayOfCurrentSelectedWeek;

    ArrayList<GoogleEvent> curEvents = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileManager = new SettingsFileManager();

        student.textProperty().addListener((observable, oldValue, newValue) -> {
            handleEditStudent();
        });

        weekTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        tableMo.setCellValueFactory(new PropertyValueFactory<>("mo"));
        tableTu.setCellValueFactory(new PropertyValueFactory<>("tu"));
        tableWe.setCellValueFactory(new PropertyValueFactory<>("we"));
        tableTh.setCellValueFactory(new PropertyValueFactory<>("th"));
        tableFr.setCellValueFactory(new PropertyValueFactory<>("fr"));
        tableSa.setCellValueFactory(new PropertyValueFactory<>("sa"));

        tableMo.setCellFactory(e -> new TableCell<ObservableList<String>, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                // Always invoke super constructor.
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    if(!moColor.equals(""))
                        this.setStyle("-fx-background-color:" + moColor +";");
                }
            }
        });

        //Fill ComboBox with driverlicences
        driverLicence.getItems().add(LicenceType.XMLNameOfType(LicenceType.LICENCE_TYPE_AM));
        driverLicence.getItems().add(LicenceType.XMLNameOfType(LicenceType.LICENCE_TYPE_A1));
        driverLicence.getItems().add(LicenceType.XMLNameOfType(LicenceType.LICENCE_TYPE_A2));
        driverLicence.getItems().add(LicenceType.XMLNameOfType(LicenceType.LICENCE_TYPE_A));
        driverLicence.getItems().add(LicenceType.XMLNameOfType(LicenceType.LICENCE_TYPE_B));
        driverLicence.getItems().add(LicenceType.XMLNameOfType(LicenceType.LICENCE_TYPE_B_A));
        driverLicence.getItems().add(LicenceType.XMLNameOfType(LicenceType.LICENCE_TYPE_BE));

        driverLicence.getSelectionModel().select(4);

        //Fill driving teachers
        fileManager.read();
        for(DrivingTeacher curTeacher : fileManager.getDrivingTeachers()) {
            drivingTeacher.getItems().add(curTeacher.getName());
        }

        //Fill Weaks
        Date curDate = CalendarHelper.getCurrentDate();
        curDate = CalendarHelper.getLastMonday(curDate);
        curDate = CalendarHelper.getStartOfTheDay(curDate);


        Calendar weekCalendar = Calendar.getInstance();

        curDate = CalendarHelper.getDateAfterDayPeriode(curDate, -4 * 7);
        mondayOfCurrentDate = curDate;

        for(int i = 0; i < 12; i++){
            weekCalendar.setTime(curDate);
            String comboText = "KW: " + weekCalendar.get(Calendar.WEEK_OF_YEAR) + " ("  + weekCalendar.get(Calendar.DAY_OF_MONTH) + "." + (weekCalendar.get(Calendar.MONTH) +1) + " - ";
            curDate = CalendarHelper.getDateAfterDayPeriode(curDate, 5);
            weekCalendar.setTime(curDate);
            comboText += weekCalendar.get(Calendar.DAY_OF_MONTH) + "." + (weekCalendar.get(Calendar.MONTH) + 1) + ")";

            curDate = CalendarHelper.getDateAfterDayPeriode(curDate, 2);
            weekCombo.getItems().add(comboText);
        }
        weekCombo.getSelectionModel().select(4);
        hanldeEditWeek();
    }

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;
        stage.showingProperty().addListener((observableValue, aBoolean, t1) -> handleShowStage());
        stage. setOnCloseRequest(windowEvent -> handleCancel());
        stage.setTitle("Neue Fahrstunde");
    }

    private void handleShowStage(){
        stage.setMaxWidth(stage.getWidth());
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        stage.setMaxHeight(stage.getHeight());
    }

    @FXML
    private void handleEditStudent(){
        updateDataFromCalendar();
    }

    private void updateDataFromCalendar() {
        weekTable.getItems().removeAll();
        String curStudent = student.getText();

        ArrayList<GoogleEvent> studentEvents = new ArrayList<>();

        //Insert all existing events of the student
        for (GoogleEvent curEvent : curEvents) {
            if (curEvent.getStudent().equals(curStudent)) {
                studentEvents.add(curEvent);
                weekTable.getItems().add(new TableRow(curEvent.createTableString(), "", "", "", "", ""));
            }
        }
        LicenceType licence = TypeOfXMLName(driverLicence.getSelectionModel().getSelectedItem());

        Date curDay = mondayOfCurrentSelectedWeek;
        for(int day = 0; day < 6; day++) {
            for (int hour = 0; hour < 13; hour++) {
                getEventsAtTime(curDay, hour + 8);
            }
            curDay = CalendarHelper.getDateAfterDayPeriode(curDay, 1);
        }

    }

    private ArrayList<GoogleEvent> getEventsAtTime(Date day, int hour){
        Calendar calendar = Calendar.getInstance();
        Calendar calDay = Calendar.getInstance();

        calDay.setTime(day);
        ArrayList<GoogleEvent> returnList = new ArrayList<>();
        for(GoogleEvent curEvent : curEvents){
            calendar.setTime(curEvent.getStartDate());

            int year = calDay.get(Calendar.YEAR);
            year = calendar.get(Calendar.YEAR);
            year = calDay.get(Calendar.DAY_OF_YEAR);
            year = calendar.get(Calendar.DAY_OF_YEAR);
            if(calDay.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && calDay.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                if (calendar.get(Calendar.HOUR) == hour){
                    returnList.add(curEvent);
                }
            }
        }
        return returnList;
    }

    @FXML
    private void hanldeEditWeek() {
        int curSel = weekCombo.getSelectionModel().getSelectedIndex();
        int size = weekCombo.getItems().size();
        if(curSel == size-1){
            weekForward.setDisable(true);
        }else
            weekForward.setDisable(false);

        if(curSel == 0){
            weekBack.setDisable(true);
        }else
            weekBack.setDisable(false);

        Date today = CalendarHelper.getCurrentDate();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(today);

        mondayOfCurrentSelectedWeek = CalendarHelper.getDateAfterDayPeriode(mondayOfCurrentDate, curSel * 7);
        Date curDate = mondayOfCurrentSelectedWeek;
        Calendar weekCalendar = Calendar.getInstance();

        GoogleCalendar calendar = new GoogleCalendar();
        try {
            curEvents = calendar.getEvents(curDate, 6);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        weekCalendar.setTime(curDate);
        if(weekCalendar.before(todayCalendar))
            moColor = "lightgray";
        else {
            moColor = "";
        }

        tableMo.setText("Mo (" + weekCalendar.get(Calendar.DAY_OF_MONTH) + "." + (weekCalendar.get(Calendar.MONTH)+1) + ")");

        curDate = CalendarHelper.getDateAfterDayPeriode(curDate, 1);
        weekCalendar.setTime(curDate);
        tableTu.setText("Di (" + weekCalendar.get(Calendar.DAY_OF_MONTH) + "." + (weekCalendar.get(Calendar.MONTH)+1) + ")");

        curDate = CalendarHelper.getDateAfterDayPeriode(curDate, 1);
        weekCalendar.setTime(curDate);
        tableWe.setText("Mi (" + weekCalendar.get(Calendar.DAY_OF_MONTH) + "." + (weekCalendar.get(Calendar.MONTH)+1) + ")");

        curDate = CalendarHelper.getDateAfterDayPeriode(curDate, 1);
        weekCalendar.setTime(curDate);
        tableTh.setText("Do (" + weekCalendar.get(Calendar.DAY_OF_MONTH) + "." + (weekCalendar.get(Calendar.MONTH)+1) + ")");

        curDate = CalendarHelper.getDateAfterDayPeriode(curDate, 1);
        weekCalendar.setTime(curDate);
        tableFr.setText("Fr (" + weekCalendar.get(Calendar.DAY_OF_MONTH) + "." + (weekCalendar.get(Calendar.MONTH)+1) + ")");

        curDate = CalendarHelper.getDateAfterDayPeriode(curDate, 1);
        weekCalendar.setTime(curDate);
        tableSa.setText("Sa (" + weekCalendar.get(Calendar.DAY_OF_MONTH) + "." + (weekCalendar.get(Calendar.MONTH)+1) + ")");
        updateDataFromCalendar();
    }

    @FXML
    private void hanldeWeakForward(){
        int curSel = weekCombo.getSelectionModel().getSelectedIndex();
        int size = weekCombo.getItems().size();
        if(curSel == size-1){
            return;
        }
        weekCombo.getSelectionModel().select(curSel+1);
        hanldeEditWeek();
    }

    @FXML
    private void hanldeWeakBackward(){
        int curSel = weekCombo.getSelectionModel().getSelectedIndex();
        int size = weekCombo.getItems().size();
        if(curSel == 0){
            return;
        }
        weekCombo.getSelectionModel().select(curSel-1);
        hanldeEditWeek();
    }

    @FXML
    private void handleCancel(){
        ((Stage)stage.getOwner()).show();
        stage.close();
    }

}
