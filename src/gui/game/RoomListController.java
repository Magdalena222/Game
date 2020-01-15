package gui.game;

import backend.logic.Room;
import frontend.*;
import gui.MainWindowController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;
import java.util.Optional;

public class RoomListController{

    @FXML public ListView list;
    protected MainWindowController parent;
    protected String name;
    Room activeRoom;

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
    public synchronized void join(ActionEvent e) {
        Room item = ((Room) list.getSelectionModel().getSelectedItem());
        if (null != item) {
            try {
                Sender.getInstance().send(this.name.trim() + ";game;joinRoom;" + item.getName().trim());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void add(ActionEvent e){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tworzenie nowego pokoju");
        dialog.setHeaderText(null);
        dialog.setContentText("Podaj nazwę pokoju:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                Sender.getInstance().send(this.name.trim() + ";game;roomList;create;" + name.trim());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public synchronized void roomListReceived(Room[] rooms) {
        list.getItems().clear();
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

    public void enterRoom(String roomName, boolean p1, String login) {
        RoomListController _this = this;
        for (Room room : (ObservableList<Room>) list.<Room>getItems()) {
            if (room.getName().trim().equals(roomName.trim())) {
                if(parent.getName().trim().equals(login.trim()))
                    _this.activeRoom = room;
                if (room.getPlayer1().equals("Wolny") || room.getPlayer1().trim().equals(login.trim())) room.setPlayer1(login);
                else room.setPlayer2(login);
                list.refresh();

                break;
            }
        }
    }

    public void createRoom(String newRoomName, String login) {
        Room r = new Room(newRoomName);
        r.setPlayer1(login);
        list.getItems().add(r);
    }

    public Room getActiveRoom(){
        return activeRoom;
    }

    public void leaveRoom(String name, String roomName) {
        Room r = null;
        ObservableList<Room> rlist = (ObservableList<Room>) list.getItems();
        for (Room room : rlist) {
            if (room.getName().trim().equals(roomName.trim())) {
                r = room;
                if (room.getPlayer1().trim().equals(name.trim())) {
                    room.setPlayer1("Wolny");
                }
                if (room.getPlayer2().trim().equals(name.trim())) {
                    room.setPlayer2("Wolny");
                }
                break;
            }
        }
        if(r!=null && r.getPlayer1().trim().equals("Wolny") && r.getPlayer2().trim().equals("Wolny")) list.getItems().remove(r);
        initialize();
    }

    public void deleteRoom(String roomName) {
        System.out.println("Usuwam pokój " + roomName);
        Room r = null;
        ObservableList<Room> rlist = (ObservableList<Room>) list.getItems();
        for (Room room : rlist) {
            System.out.println("szukam "+ room.getName().trim() + " = " + roomName.trim());
            if (room.getName().trim().equals(roomName.trim())) {
                r = room;
                break;
            }
        }
        if(r != null){
            System.out.println("Znalazlem do usuniecia");
            System.out.println(list.getItems().size());
            list.getItems().remove(r);
            System.out.println(list.getItems().size());
        }
        initialize();
    }

    public Room getRoom(String roomName) {
        for(Room room : (ObservableList<Room>) list.getItems()){
            if(room.getName().trim().equals(roomName.trim()))
                return room;
        }
        return null;
    }
}
