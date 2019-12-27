package gui.game;

import backend.logic.Room;
import frontend.IBroadcastListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.Main;

public class GameController {


    FXMLLoader roomList;
    FXMLLoader roomChat;
    Main parent;
    String name;
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
        parent = main;
    }

    public int guess(char ch) {
        int sum = 0;
        for (WordComponent wc : wordsList) sum += wc.guess(ch);
        return sum;
    }

    public void setRoom(Room room){
        this.p1Label.setText(room.getPlayer1());
        this.p2Label.setText(room.getPlayer2());
        this.p1Points.setText("0 zł");
        this.p2Points.setText("0 zł");
    }

    public boolean guessPass(String pass){
        return pass.toUpperCase().equals(this.pass);
    }
}
