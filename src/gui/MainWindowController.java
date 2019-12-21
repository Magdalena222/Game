package gui;

import backend.logic.Room;
import gui.chat.GeneralChatController;
import gui.game.GameController;
import gui.game.RoomListController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.Main;

import java.io.IOException;

public class MainWindowController {

    protected String name;
    protected String roomName;
    private Main parent;

    @FXML
    public BorderPane mainPane;

    Pane roomPane;
    RoomListController roomListController;

    Pane gamePane;
    GameController gameController;

    FXMLLoader generalChat;

    @FXML
    public void initialize() {
        try {
            name = "Anonim";
            FXMLLoader rl = new FXMLLoader(getClass().getResource("game/roomList.fxml"));
            roomPane = rl.load();
            mainPane.setCenter(roomPane);

            roomListController = rl.getController();
            roomListController.setParent(this);

            FXMLLoader gc = new FXMLLoader(getClass().getResource("game/game.fxml"));
            gamePane = gc.load();
            gameController = gc.getController();
            gameController.setParent(parent);

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
        this.name = name;
        generalChat.<GeneralChatController>getController().setName(name);
        gameController.setName(name);
        roomListController.setName(name);
    }

    public void setParent(Main parent) {
        this.parent = parent;
    }

    public void enterRoom(String roomName, boolean p1, String login){
        roomListController.enterRoom(roomName, p1, login);
    }

    public void roomListReceived(Room[] rooms) {
        roomListController.roomListReceived(rooms);
    }

    public String getName() {
        return name;
    }

    public void setRoomName(String name) {
        roomName = name;
    }

    public void enterGameRoom(String name) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPane.setCenter(gamePane);
            }
        });
    }

    public void createRoom(String newRoomName) {
        roomListController.createRoom(newRoomName);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPane.setCenter(gamePane);
            }
        });
    }
}
