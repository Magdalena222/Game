package gui;

import gui.game.RoomListController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainWindowController {

    @FXML
    public BorderPane mainPane;

    @FXML
    public void initialize() {
        try {
            FXMLLoader load = new FXMLLoader(getClass().getResource("game/roomList.fxml"));
//            RoomListController c = new RoomListController();
//            c.setParent(this);
//            load.setController(c);
            mainPane.setCenter(load.load());
            load.<RoomListController>getController().setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideList(){
        mainPane.setCenter(null);
    }
}
