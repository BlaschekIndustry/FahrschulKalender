package main;

import fileControl.SettingsFileManager;
import gui.ControllerMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..\\gui\\main.fxml"));
        Parent root = (Parent)loader.load();
        ControllerMain controller = (ControllerMain)loader.getController();
        controller.setStageAndSetupListeners(primaryStage); // or what you want to do

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
