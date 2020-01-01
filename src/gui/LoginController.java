package gui;


import frontend.Sender;
import frontend.ServerReceiver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.GameSettings;
import main.Main;

import java.io.IOException;


public class LoginController{

    protected Main parent;

    public LoginController() {
    }

    @FXML
    private TextField nick;

    @FXML
    private Label nickVeri;

    @FXML
    private void signinbtnclick(ActionEvent event) throws Exception{
        String nickText = nick.getText();
        Sender.getInstance().send(nickText.trim() + ";game;login;localhost;" + GameSettings.getInstance().getGameReceiver().getPort());
    }

    public void loginFailed(String msg) {
        Platform.runLater(() ->{
            nickVeri.setText(msg);
            nickVeri.setVisible(true);
        });
    }

    public void setParent(Main parent) {
        this.parent = parent;
    }
}