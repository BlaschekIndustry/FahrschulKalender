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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerOtherEvent implements Initializable {
    @FXML Button cancelButton;
    @FXML Button insertButton;
    @FXML ComboBox typeComboBox;
    @FXML ListView<String> listView;

    private Stage stage;
    SettingsFileManager fileManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}
