package gui;

import gui.chat.ChatController;
import gui.game.RoomListController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainWindowController {

    protected String name;

    @FXML
    public BorderPane mainPane;

    FXMLLoader roomList;
    FXMLLoader generalChat;

    @FXML
    public void initialize() {
        try {
            name = "Anonim";
            roomList = new FXMLLoader(getClass().getResource("game/roomList.fxml"));
            mainPane.setCenter(roomList.load());
            roomList.<RoomListController>getController().setParent(this);
            generalChat = new FXMLLoader(getClass().getResource("chat/chat.fxml"));
            mainPane.setLeft(generalChat.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideList(){
        mainPane.setCenter(null);
    }

    public void setName(String name) {
        System.out.println("Setting name");
        generalChat.<ChatController>getController().setName(name);
    }
}
