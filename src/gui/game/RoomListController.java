package gui.game;

import backend.logic.Room;
import gui.MainWindowController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;

public class RoomListController {

    @FXML public ListView list;
    protected MainWindowController parent;

    public void setParent(MainWindowController parent) {
        this.parent = parent;
    }

    @FXML
    public void initialize() {
        list.getItems().add(new Room("Koko"));
        list.setCellFactory(new Callback<ListView<Room>, ListCell<Room>>() {

            @Override
            public ListCell<Room> call(ListView<Room> list) {

                ListCell<Room> cell = new ListCell<Room>() {
                    @Override
                    public void updateItem(Room item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            try {
                                FXMLLoader load = new FXMLLoader(this.getClass().getResource("RoomListItem.fxml"));
                                HBox box = load.load();
                                RoomListItemController c = load.<RoomListItemController>getController();
                                c.setName(item.getName());
                                setGraphic(box);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                return cell;
            }
        });
    }

    @FXML
    public void join(ActionEvent e){
        System.out.println(((Room)list.getSelectionModel().getSelectedItem()).getName());
    }

    @FXML
    public void add(ActionEvent e){
        list.getItems().add(new Room("jojo"));
    }
}
