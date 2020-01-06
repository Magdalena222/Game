package gui.game;

import backend.logic.Room;
import frontend.Sender;
import gui.chat.RoomChatController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.Main;

import java.io.IOException;
import java.util.Optional;

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
            roomChat.<RoomChatController>getController().setName(parent.getLogin());
            roomChat.<RoomChatController>getController().setRoomName(name);
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
        parent.getGameController().setRoom(room);
        mainPane.setCenter(parent.getGamePane());
    }

    public void initGame(String password) {
        parent.getGameController().setPassword(password);
    }

    public void leaveRoom(String name) {
        if(room.getPlayer1().trim().equals(name.trim())) {
            room.setPlayer1("Wolny");
            parent.getGameController().p1Label.setText("Wolny");
        }
        if(room.getPlayer2().trim().equals(name.trim())) {
            room.setPlayer1("Wolny");
            parent.getGameController().p2Label.setText("Wolny");
        }
    }

    public void guessChar(String player, String ch, String priceP1, String priceP2, String newPrice) {
        parent.getGameController().guess(ch.charAt(0));
        parent.getGameController().p1Points.setText(priceP1);
        parent.getGameController().p2Points.setText(priceP2);
        parent.getGameController().myTurn(player.trim().equals(parent.getLogin().trim()));
        parent.getGameController().myTurn(player.trim().equals(parent.getLogin().trim()));
        parent.getGameController().price.setText(newPrice);
    }

    public void guessPass(String player, String status, String p1Points, String p2Points) {
        if(status.trim().toUpperCase().equals("OK")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Koniec gry");
            alert.setContentText(room.getPlayer1() + ": " + p1Points+"\n"+room.getPlayer2() + ": " + p2Points+"\n");
            if(parent.getLogin().trim().toUpperCase().equals(player.toUpperCase().trim()))
                alert.setHeaderText("Wygrałeś!");
            else
                alert.setHeaderText("Przegrałeś :(");
            alert.showAndWait();
        }else{
            if(player.trim().equals(parent.getLogin())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ogdadywanie hasła");
                alert.setContentText(null);
                alert.setHeaderText("Nie zgadłeś :(");
                alert.showAndWait();
            }
        }
    }

    public void roomMessage(String roomName, String player, String message) {
        roomChat.<RoomChatController>getController().message(player, message);
    }
}
