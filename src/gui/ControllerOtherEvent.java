package gui;

import fileControl.SettingsFileManager;
import general.DrivingTeacher;
import general.Vehicle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerOtherEvent implements Initializable {
    @FXML Button cancelButton;
    @FXML Button insertButton;
    @FXML ComboBox typeComboBox;
    @FXML ListView<String> listView;
    @FXML Spinner<String> timeFrom;
    @FXML Spinner<String> timeTo;
    private Stage stage;
    SettingsFileManager fileManager;
    private String timeFromTime;
    private String timeToTime;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeToTime = "20:00";
        timeFromTime = "08:00";

        timerFromInitialize();
        timerToInitialize();




        fileManager = new SettingsFileManager();
        fileManager.read();

        insertButton.setDisable(true);

        typeComboBox.getItems().add("Fahrzeuge");
        typeComboBox.getItems().add("Fahrlehrer");
        typeComboBox.getSelectionModel().select(0);

//        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
//                                                                 @Override
//                                                                 public ObservableValue<Boolean> call(String item) {
//                                                                     BooleanProperty observable = new SimpleBooleanProperty();
//                                                                     observable.addListener((obs, wasSelected, isNowSelected) ->
//
//                                                                             System.out.println("Check box for " + item + " changed from " + wasSelected + " to " + isNowSelected)
//                                                                     );
//                                                                     return observable;
//                                                                 }
//                                                             }));
        onComboSelChange();
    }

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;
        stage.showingProperty().addListener((observableValue, aBoolean, t1) -> handleShowStage());
        stage. setOnCloseRequest(windowEvent -> handleCancel());
        stage.setTitle("Sonstigen Termin einfügen");
    }

    @FXML
    private void onComboSelChange(){
        listView.getItems().clear();
        int sel = typeComboBox.getSelectionModel().getSelectedIndex();
        if(sel == 0){
            ArrayList<Vehicle> vehicles = fileManager.getVehicles();
            for(Vehicle cur : vehicles){
                listView.getItems().add(cur.getName());
            }
        }else{
            ArrayList<DrivingTeacher> vehicles = fileManager.getDrivingTeachers();
            for(DrivingTeacher cur : vehicles){
                listView.getItems().add(cur.getName());
            }
        }
    }

    private void handleShowStage(){
        stage.setMaxWidth(stage.getWidth());
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        stage.setMaxHeight(stage.getHeight());
    }

    @FXML
    private void handleCancel(){
        ((Stage)stage.getOwner()).show();
        stage.close();
    }


    private String handleTimeFromUp(){
        return "testup";
    }

    private String handleTimeFromDown(){
        return "testdown";
    }

    private String handleTimeToUp(){
        return"testUp";
    }

    private String handleTimeToDown(){
        return"testDown";
    }

    private String timeToIncrease(String tTime){
        Integer min;
        Integer std;
        DecimalFormat nmin = new DecimalFormat("00");

        DecimalFormat nstd = new DecimalFormat("00");

        min = Integer.parseInt(tTime.substring(3));
        std = Integer.parseInt(tTime.substring(0,2));

        if(min < 45){
            min += 15;
        }else{

            std += 1;
            min = 00;

            if (std == 24){
                std = 00;
            }
        }


        tTime = nstd.format(std) + ":" + nmin.format(min);
        timeToTime = tTime;

        return tTime;
    }

    private String timeToDecrease(String tTime){
        Integer min;
        Integer std;
        DecimalFormat nmin = new DecimalFormat("00");
        DecimalFormat nstd = new DecimalFormat("00");

        min = Integer.parseInt(tTime.substring(3));
        std = Integer.parseInt(tTime.substring(0,2));

        if(min > 0){
            min -= 15;
        }else{
            if (std == 0){
                std = 23;
            }else{
                std -= 1;
            }

            min = 45;

        }


        tTime = nstd.format(std) + ":" + nmin.format(min);
        timeToTime = tTime;
        return tTime;
    }

    private String timeFromIncrease(String tTime){
        Integer min;
        Integer std;
        DecimalFormat nmin = new DecimalFormat("00");
        DecimalFormat nstd = new DecimalFormat("00");

        min = Integer.parseInt(tTime.substring(3));
        std = Integer.parseInt(tTime.substring(0,2));

        if(min < 45){
            min += 15;
        }else{

            std += 1;
            min = 0;

            if (std == 24){
                std = 0;
            }
        }


        tTime = nstd.format(std) + ":" + nmin.format(min);
        timeFromTime = tTime;

        return tTime;
    }

    private String timeFromDecrease(String tTime){
        Integer min;
        Integer std;
        DecimalFormat nmin = new DecimalFormat("00");
        DecimalFormat nstd = new DecimalFormat("00");

        min = Integer.parseInt(tTime.substring(3));
        std = Integer.parseInt(tTime.substring(0,2));

        if(min > 0){
            min -= 15;
        }else{
            if (std == 00){
                std = 23;
            }else{
                std -= 1;
            }

            min = 45;


        }


        tTime = nstd.format(std) + ":" + nmin.format(min);
        timeFromTime = tTime;
        return tTime;
    }

    private void timerFromInitialize(){
        timeFrom.setPromptText("08:00");

        // Value TimerFromFactory.
        SpinnerValueFactory<String> valueTimerFromFactory = //
                new SpinnerValueFactory<String>() {

                    @Override
                    public void decrement(int steps) {
                        this.setValue(timeFromDecrease(timeFromTime));
                    }

                    @Override
                    public void increment(int steps) {
                        this.setValue(timeFromIncrease(timeFromTime));
                    }
                };

        timeFrom.setValueFactory(valueTimerFromFactory);
    }

    private void timerToInitialize(){

        timeTo.setPromptText(timeToTime);
        // Value TimerToFactory.
        SpinnerValueFactory<String> valueTimeToFactory = //
                new SpinnerValueFactory<String>() {

                    @Override
                    public void decrement(int steps) {
                        this.setValue(timeToDecrease(timeToTime));
                    }

                    @Override
                    public void increment(int steps) {
                        this.setValue(timeToIncrease(timeToTime));
                    }
                };

        timeTo.setValueFactory(valueTimeToFactory);
        //TetsKommentar
    }


}
