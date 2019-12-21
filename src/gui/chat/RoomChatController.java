package gui.chat;

import frontend.BroadcastReceiver;
import frontend.IBroadcastListener;
import frontend.Sender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RoomChatController implements IBroadcastListener {

    @FXML public TextArea textArea;
    @FXML public TextField textField;
    protected String name;


    @FXML
    public void initialize() {

    }

    @Override
    public synchronized void listen(byte[] msg) {

//        Platform.runLater(new Runnable() {
//            public void run() {
//                String[] s = new String(msg).split(";");
//                if(s[1].equals("chat")) {
//                    textArea.setText(s[0] + ": " + s[2] + "\n" + textArea.getText());
//                }
//            }
//        });
    }

    public void setName(String name) {
        this.name = name;
    }
}
