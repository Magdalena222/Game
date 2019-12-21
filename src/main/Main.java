package main;

import backend.ChatMessageHandler;
import backend.GameMessageHandler;
import backend.MessageHandler;
import backend.Server;
import backend.logic.Room;
import frontend.BroadcastReceiver;
import frontend.Sender;
import frontend.ServerReceiver;
import gui.LoginController;
import gui.MainWindowController;
import gui.game.GameController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    protected Stage primaryStage;
    private BroadcastReceiver breceiver;
    private ServerReceiver sreceiver;

    LoginController loginPage;
    Pane loginPane;

    MainWindowController roomsPage;
    Pane mainPane;

    GameController gamePage;
    Pane gamePane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        breceiver = new BroadcastReceiver(this);
        breceiver.start();

        sreceiver = new ServerReceiver(this);
        sreceiver.start();

        this.primaryStage = primaryStage;

        FXMLLoader lp = new FXMLLoader(getClass().getResource("../gui/login.fxml"));
        loginPane = lp.load();
        loginPage = lp.getController();
        loginPage.setParent(this);

        FXMLLoader rp = new FXMLLoader(getClass().getResource("../gui/MainWindow.fxml"));
        mainPane = rp.load();
        roomsPage = rp.getController();
        roomsPage.setParent(this);

        FXMLLoader gp = new FXMLLoader(getClass().getResource("../gui/game/game.fxml"));
        gamePane = gp.load();
        gamePage = gp.getController();
        gamePage.setParent(this);

        primaryStage.setTitle("Koło fortuny");
        primaryStage.setScene(new Scene(loginPane, 800, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            breceiver.interrupt();
            sreceiver.interrupt();
            Platform.exit();
        });
    }

    public static void main(String[] args){
        launch(args);
    }

    public void login(String name){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                primaryStage.setScene(new Scene(mainPane, 800,400));
                roomsPage.setName(name);
            }
        });
    }

    protected void enterRoom(String name){
        roomsPage.enterGameRoom(name);
    }

    public synchronized void listen(byte[] data) {
        String fullMsg = new String(data);
        System.out.println("Received from server: " + fullMsg);
        String[] msg = new String(data).split(";");

        if(msg.length < 2){
            System.err.println("Message to short: " + fullMsg);
            return;
        }

        switch (msg[1]){
            case "game":
                if(msg.length<3){
                    System.err.println("Message to short: " + fullMsg);
                    return;
                }
                switch (msg[2]){
                    case "login":
                        if(msg.length<5){
                            System.err.println("Message to short: " + fullMsg);
                            return;
                        }

                        switch (msg[3]){
                            case "ok":
                                login(msg[4]);
                                break;
                            case "fail":
                                if(msg.length<6){
                                    System.err.println("Message to short: " + fullMsg);
                                    return;
                                }
                                loginPage.loginFailed(msg[5]);
                                break;
                        }
                        break;
                    case "roomList":
                        if(msg.length<5){
                            System.err.println("Message to short: " + fullMsg);
                            return;
                        }
                        switch (msg[3]){
                            case "all":
                                Room[] roomList = new Room[(msg.length-3)/3];
                                for(int i = 0; i<(msg.length-3)/3; i++){
                                    roomList[i] = new Room(msg[4+i*3]);
                                    roomList[i].setPlayer1(msg[5+i*3]);
                                    roomList[i].setPlayer2(msg[6+i*3]);
                                }
                                    roomsPage.roomListReceived(roomList);
                                break;
                            case "create":
                                switch (msg[4]){
                                    case "ok":
                                        if(msg.length<6){
                                            System.err.println("Message to short: " + fullMsg);
                                            return;
                                        }
                                        roomsPage.createRoom(msg[5]);

                                }
                        }
                        break;
                    case "joinRoom":
                        roomsPage.enterRoom(msg[4], msg[3].equals("p1"), msg[5]);
                        if(msg[5].trim().equals(roomsPage.getName().trim())){
                            enterRoom(msg[4]);
                        }
                        break;

                }
                break;
        }
    }

}
