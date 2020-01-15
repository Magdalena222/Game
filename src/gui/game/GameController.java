package gui.game;

import backend.logic.Room;
import frontend.IBroadcastListener;
import frontend.Sender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import main.Main;

import java.io.IOException;
import java.util.Optional;

public class GameController {

    Main parent;
    String name;
    String roomName;
    String pass;
    WordComponent[] wordsList;

    @FXML
    Label p1Label;

    @FXML
    Label p1Points;

    @FXML
    Label p2Label;

    @FXML
    Label p2Points;

    @FXML
    VBox words;

    @FXML
    Button leaveRoom;

    @FXML
    Button guessChar;

    @FXML
    Button price;

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        pass = password.toUpperCase();
        words.getChildren().clear();
        String wordsS[] = pass.split(" ");
        wordsList = new WordComponent[wordsS.length];
        int i = 0;
        for (String word : wordsS) {
            WordComponent wc = new WordComponent(word);
            words.getChildren().add(wc);
            wordsList[i++] = wc;
        }
    }

    @FXML
    public void initialize() {
    }

    public void setParent(Main main) {
        System.out.println("Setting parent for GameController to " + (main == null));
        parent = main;
    }

    public int guess(char ch) {
        int sum = 0;
        for (WordComponent wc : wordsList) sum += wc.guess(ch);
        return sum;
    }

    public void setRoom(Room room){
        this.roomName = room.getName();
        this.name = parent.getLogin();
        this.p1Label.setText(room.getPlayer1());
        this.p2Label.setText(room.getPlayer2());
        this.p1Points.setText("0");
        this.p2Points.setText("0");
    }

    public boolean guessPass(String pass){
        return pass.toUpperCase().equals(this.pass);
    }

    @FXML
    public void leaveRoomAction(){
        try {
            Sender.getInstance().send(this.name.trim() + ";game;roomList;leave;" + roomName.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guessCharAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Zgadywanie litery");
        dialog.setHeaderText(null);
        dialog.setContentText("Podaj literę, którą chcesz zgadnąć");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if(
                    name.length() > 1 ||
                    name.length() < 1 ||
                    name.toUpperCase().charAt(0) < 'A' ||
                    name.toUpperCase().charAt(0) > 'Z'
            ){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("To nie jest litera!");
                alert.showAndWait();
            }else{
                try {
                    Sender.getInstance().send(parent.getLogin().trim()+";game;cg;"+roomName.trim()+";"+name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void guessPassAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Zgadywanie hasła");
        dialog.setHeaderText(null);
        dialog.setContentText("Podaj hasło: ");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(pass -> {
            try {
                Sender.getInstance().send(parent.getLogin().trim()+";"+"game;gp;"+roomName.trim()+";"+pass.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void myTurn(boolean turn) {
        guessChar.setDisable(turn);
    }
}
