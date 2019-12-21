package gui.game;

import frontend.IBroadcastListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import main.Main;

public class GameController implements IBroadcastListener {


    FXMLLoader roomList;
    FXMLLoader roomChat;
    Main parent;

    public void setName(String name){

    }

    @FXML
    public void initialize(){

    }

    @Override
    public void listen(byte[] msg) {
        Platform.runLater(new Runnable() {
            public void run() {
                String[] s = new String(msg).split(";");
                if(s[1].equals("game")) {

                }
            }
        });
    }

    public void setParent(Main main) {
        parent = main;
    }
}
