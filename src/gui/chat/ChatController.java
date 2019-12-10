package gui.chat;

import frontend.IBroadcastListener;
import frontend.BroadcastReceiver;
import frontend.Sender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ChatController implements IBroadcastListener {

    @FXML public TextArea textArea;
    @FXML public TextField textField;
    protected BroadcastReceiver broadcastReceiver;



    @FXML
    public void initialize() {
        try {
            broadcastReceiver = new BroadcastReceiver(this);
            broadcastReceiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEnter(ActionEvent ae){
        try {
            Sender.getInstance().send("client;chat;"+textField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textField.setText("");
    }

    @Override
    public synchronized void listen(byte[] msg) {
        Platform.runLater(new Runnable() {
            public void run() {
                String[] s = new String(msg).split(";");
                textArea.setText(s[0] + ": " + s[2] + "\n" + textArea.getText());
            }
        });
    }
}
