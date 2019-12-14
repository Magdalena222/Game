package gui.game;

import backend.logic.Room;
import frontend.IServerRoomListListener;
import frontend.Sender;
import frontend.ServerRoomListReceiver;
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

public class RoomListController implements IServerRoomListListener {

    @FXML public ListView list;
    protected MainWindowController parent;
    protected String name;
    protected ServerRoomListReceiver receiver;

    public RoomListController() {
        try {
            receiver = new ServerRoomListReceiver(this);
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setParent(MainWindowController parent) {
        this.parent = parent;
    }

    @FXML
    public void initialize() {
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
                                c.setPlayer1(item.getPlayer1());
                                c.setPlayer2(item.getPlayer2());
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
        Room item = ((Room)list.getSelectionModel().getSelectedItem());
        if(null!=item){
            System.out.println(item.getName());
        }
    }

    @FXML
    public void add(ActionEvent e){

    }

    @Override
    public void roomListReceived(Room[] rooms) {
        list.getItems().addAll(rooms);
    }

    public void setName(String name) {
        this.name = name;
        try {
            Sender.getInstance().send(this.name.trim() + ";game;roomList;getAll");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
