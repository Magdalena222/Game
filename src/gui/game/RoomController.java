package gui.game;

import backend.logic.Room;
import gui.chat.RoomChatController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.Main;

import java.io.IOException;

public class RoomController {


    FXMLLoader roomChat;
    GameController gameController;
    AnchorPane gamePane;
    Main parent;
    Room room;

    @FXML
    BorderPane mainPane;

    public void setName(String name){
        roomChat = new FXMLLoader(getClass().getResource("../chat/roomChat.fxml"));
        try {
            mainPane.setRight(roomChat.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){

    }

    public void setParent(Main main) {
        parent = main;
    }

    public void setRoomName(String name) {
        roomChat.<RoomChatController>getController().setRoomName(name);
    }

    public void setRoom(Room room){
        this.room = room;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        try {
            gamePane = loader.<AnchorPane>load();
            gameController = loader.<GameController>getController();
            gameController.setRoom(room);
            mainPane.setCenter(gamePane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
