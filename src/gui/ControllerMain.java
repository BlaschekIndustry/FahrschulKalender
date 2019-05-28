package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMain implements Initializable {
    @FXML private BorderPane borderPane;
    @FXML private Button newDrivingLesson;
    @FXML private Button otherTermin;
    @FXML private Button settings;
    @FXML Button cancelButton;
    @FXML private ComboBox<String> userComboBox;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;
        stage.showingProperty().addListener((observableValue, aBoolean, t1) -> handleShowStage());

        stage.setTitle("Fahrschul Kalendar");
    }

    private void handleShowStage(){
        stage.setMaxWidth(stage.getWidth());
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    @FXML
    private void handleOpenNewDrivingLesson() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fahrstunde.fxml"));
        Parent root = (Parent)loader.load();
        ControllerDrivingLesson controller = (ControllerDrivingLesson) loader.getController();
        Stage drivingLessonStage = new Stage();
        controller.setStageAndSetupListeners(drivingLessonStage); // or what you want to do
        drivingLessonStage.setScene(new Scene(root));
        drivingLessonStage.initOwner(stage);
        //hide current window
        stage.hide();
        drivingLessonStage.show();
    }

    @FXML
    public void handleCreateOtherTermin() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SonstigeTermine.fxml"));
        Parent root = (Parent)loader.load();
        ControllerMain controller = (ControllerMain)loader.getController();
        Stage otherTermin = new Stage();
        controller.setStageAndSetupListeners(otherTermin); // or what you want to do
        otherTermin.setScene(new Scene(root));
        otherTermin.initOwner(stage);

        //hide current window
        stage.hide();
        otherTermin.show();
    }

    @FXML
    public void handleOpenSettings() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Einstellungen.fxml"));
        Parent root = (Parent)loader.load();
        ControllerSettings controller = (ControllerSettings)loader.getController();
        Stage settingStage = new Stage();
        controller.setStageAndSetupListeners(settingStage); // or what you want to do
        settingStage.setScene(new Scene(root));
        settingStage.initOwner(stage);

        //hide current window
        stage.hide();
        settingStage.show();
    }

    @FXML
    private void handleCancel(){
        stage.close();
    }

}
