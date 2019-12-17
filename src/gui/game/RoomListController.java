package gui.game;

import backend.logic.Room;
import frontend.*;
import gui.MainWindowController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Optional;

public class RoomListController implements IServerRoomListListener, IBroadcastListener {

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
        BroadcastReceiver.getInstance().addListener(this);
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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tworzenie nowego pokoju");
        dialog.setHeaderText(null);
        dialog.setContentText("Podaj nazwę pokoju:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                Sender.getInstance().send(this.name.trim() + ";game;roomList;create;" + name);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
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

    @Override
    public void listen(byte[] msg) {
        RoomListController _this = this;
        Platform.runLater(new Runnable() {
            public void run() {
                String[] s = new String(msg).split(";");
                if(s[1].equals("game")) {
                    switch(s[2]){
                        case "roomList":
                            switch (s[3]){
                                case "create":
                                    if(s[4].equals("ok")){
                                        System.out.println("Tworzymy pokój " + s[5].trim());
                                        Room r = new Room(s[5]);
                                        r.setPlayer1(_this.name);
                                        list.getItems().add(r);
                                    }
                                    break;
                            }
                            break;
                    }

                }
            }
        });
    }
}
