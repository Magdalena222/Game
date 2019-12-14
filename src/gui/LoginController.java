package gui;


import frontend.IServerLoginListener;
import frontend.Sender;
import frontend.ServerLoginReceiver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Main;

import java.io.IOException;


public class LoginController implements IServerLoginListener {

    protected Main parent;
    protected ServerLoginReceiver receiver;

    public LoginController() {
        try {
            receiver = new ServerLoginReceiver(this);
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField nick;

    @FXML
    private Label nickVeri;

    @FXML
    private void signinbtnclick(ActionEvent event) throws Exception{
        String nickText = nick.getText();
        Sender.getInstance().send(nickText + ";game;login;localhost;" + receiver.PORT);
    }

    @Override
    public void loginOK(String name) {
        Platform.runLater(new Runnable() {
            public void run() {
                parent.login(name);
            }
        });
        receiver.interrupt();
    }

    @Override
    public void loginFailed(String msg) {
        Platform.runLater(new Runnable() {
            public void run() {
                nickVeri.setText(msg);
                nickVeri.setVisible(true);
            }
        });
    }

    public void setParent(Main parent) {
        this.parent = parent;
    }
}