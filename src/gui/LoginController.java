package gui;


import backend.Server;
import frontend.IServerListener;
import frontend.Sender;
import frontend.ServerReceiver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Main;

import java.io.IOException;


public class LoginController implements IServerListener {

    protected Main parent;
    protected ServerReceiver receiver;

    public LoginController() {
        try {
            receiver = new ServerReceiver(this);
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
    public void listen(byte[] msg) {
        Platform.runLater(new Runnable() {
            public void run() {
                String xxx = new String(msg);
                System.out.println("Listen " + xxx);
                String[] message = xxx.split(";");
                if(message[3].equals("ok"))
                    parent.login(message[4]);
                else{
                    nickVeri.setText("Podany login już istnieje");
                    nickVeri.setVisible(true);
                }

            }
        });
    }

    public void setParent(Main parent) {
        this.parent = parent;
    }
}