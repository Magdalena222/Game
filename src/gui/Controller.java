package gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;



public class Controller {

    @FXML
    private TextField nick;

    @FXML
    private Label nickVeri;

    @FXML
    private void signinbtnclick(ActionEvent event) throws Exception{

        String nickText = nick.getText();
        nickVeri.setText("OK");
        nickVeri.setTextFill(Color.web("#004d08"));
        nickVeri.setVisible(true);
    }
}