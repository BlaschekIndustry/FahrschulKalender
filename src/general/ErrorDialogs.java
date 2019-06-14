package general;

import javafx.scene.control.Alert;

public class ErrorDialogs {

    //Displays an error-message( errorMessage)
    public static void showErrorMessage(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Folgender Fehler ist Aufgetreten");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    public static void showWarningMessage(String warningMessage){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warnung");
        alert.setHeaderText("Folgende Warnung ist Aufgetreten");
        alert.setContentText(warningMessage);

        alert.showAndWait();
    }
}
