package gui.chat;

import frontend.BroadcastReceiver;
import frontend.IBroadcastListener;
import frontend.Sender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Main;

import java.io.IOException;

public class RoomChatController{

    @FXML public TextArea textArea;
    @FXML public TextField textField;
    protected String name;
    protected String roomName;


    @FXML
    public void initialize() {

    }

    public synchronized void message(String from, String msg) {
        textArea.setText(from + ": " + msg + "\n" + textArea.getText());
    }


    public void onEnter(ActionEvent ae){
        try {
            Sender.getInstance().send(roomName.trim() + ";rchat;" + Main.getInstance().getLogin().trim() + ";" + textField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textField.setText("");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
