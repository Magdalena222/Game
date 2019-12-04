package gui.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController{

    @FXML public TextArea textArea;
    @FXML public TextField textField;

    public void onEnter(ActionEvent ae){
        textArea.setText(textField.getText() + "\n" + textArea.getText());
        textField.setText("");
    }
}
