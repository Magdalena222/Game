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

import java.io.IOException;
import java.util.Optional;

public class RoomListController{

    @FXML public ListView list;
    protected MainWindowController parent;
    protected String name;
    protected ServerRoomListReceiver receiver;
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
    public synchronized void join(ActionEvent e){
        RoomListController _this = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Room item = ((Room)list.getSelectionModel().getSelectedItem());
                if(null!=item){
                    try {
                        Sender.getInstance().send(_this.name.trim() + ";game;joinRoom;" + item.getName().trim());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                list.getItems().addAll(rooms);
            }
        });
    }

    public void setName(String name) {
        this.name = name;
        try {
            Sender.getInstance().send(this.name.trim() + ";game;roomList;getAll");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void listen(byte[] msg) {
//        RoomListController _this = this;
//        Platform.runLater(new Runnable() {
//            public void run() {
//                System.out.println("Roomlsit controller listen to " + new String(msg));
//                String[] s = new String(msg).split(";");
//                if(s[1].equals("game")) {
//                    switch(s[2].trim()){
//                        case "roomList":
//                            switch (s[3].trim()){
//                                case "create":
//                                    if(s[4].equals("ok")){
//                                        System.out.println("Tworzymy pokój " + s[5].trim());
//                                        Room r = new Room(s[5]);
//                                        r.setPlayer1(_this.name);
//                                        list.getItems().add(r);
//                                    }
//                                    break;
//                            }
//                            break;
//                        case "joinRoom":
//                            for(Object room : list.getItems()){
//                                Room r = (Room) room;
//                                if(r.getName().equals(s[4].trim())){
//                                    if(s[3].equals("p1")) {
//                                        r.setPlayer1(s[5]);
//                                        System.out.println("Room " + s[4].trim() + ": joining " + s[5].trim() + " as Player1");
//                                    }
//                                    else {
//                                        r.setPlayer2(s[5]);
//                                        System.out.println("Room " + s[4].trim() + ": joining " + s[5].trim() + " as Player2");
//                                    }
//                                    receiver.interrupt();
//                                    list.refresh();
//                                    parent.enterRoom(s[4].trim());
//                                }
//                            }
//                            break;
//                    }
//
//                }
//            }
//        });
//    }

    public void enterRoom(String roomName, boolean p1, String login) {
        RoomListController _this = this;
        for (Room room : (ObservableList<Room>) list.<Room>getItems()) {
            if (room.getName().equals(roomName.trim())) {
                System.out.println("Setting active Room " + room.getName());
                _this.activeRoom = room;
                if (p1) room.setPlayer1(login);
                else room.setPlayer2(login);
                list.refresh();
                break;
            }
        }
    }

    public void createRoom(String newRoomName) {
        RoomListController _this = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Room r = new Room(newRoomName);
                r.setPlayer1(_this.name);
                list.getItems().add(r);
            }
        });
    }

    public Room getActiveRoom(){
        return activeRoom;
    }
}
