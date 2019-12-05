package gui.game;

import backend.logic.Room;
import gui.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;

public class RoomListItemController {

    @FXML public Label nameLabel;
    @FXML public Label player1Label;
    @FXML public Label player2Label;

    public void setName(String name) {
        this.nameLabel.setText(name);
    }

    public void setPlayer1(String name) {
        this.player1Label.setText(name);
    }

    public void setPlayer2(String name) {
        this.player2Label.setText(name);
    }
}
