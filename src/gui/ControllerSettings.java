package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerSettings implements Initializable {
    @FXML Button cancelButton;
    @FXML Button saveButton;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    @FXML
    private void handleCancel(){
        ((Stage)stage.getOwner()).show();
        stage.close();
    }
}

