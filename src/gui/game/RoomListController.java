package gui.game;

import gui.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;

public class RoomListController {

    @FXML public ListView list;
    protected MainWindowController parent;

    public void setParent(MainWindowController parent) {
        this.parent = parent;
    }

    @FXML
    public void join(ActionEvent e){
        System.out.println("ACTION!");
    }
}
