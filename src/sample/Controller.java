package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class Controller {

    public Button button1;

    @FXML
    public void buttonClicked(Event e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Koko");
        alert.setContentText("Spoko");
        alert.setTitle("OK");
        alert.show();
    }
}
