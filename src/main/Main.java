package main;

import backend.logic.Room;
import frontend.BroadcastReceiver;
import frontend.ServerReceiver;
import gui.LoginController;
import gui.MainWindowController;
import gui.game.GameController;
import gui.game.RoomController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    protected Stage primaryStage;
    private BroadcastReceiver breceiver;
    private ServerReceiver sreceiver;
    private String loginName;
    private static Main instance;

    LoginController loginPage;
    Pane loginPane;

    MainWindowController mainController;
    Pane mainPane;

    RoomController roomController;
    Pane roomPane;

    GameController gameController;
    Pane gamePane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        instance = this;
        breceiver = new BroadcastReceiver(this);
        breceiver.start();

        sreceiver = new ServerReceiver(this);
        sreceiver.start();

        this.primaryStage = primaryStage;

        FXMLLoader lp = new FXMLLoader(getClass().getResource("../gui/login.fxml"));
        loginPane = lp.load();
        loginPage = lp.getController();
        loginPage.setParent(this);

        FXMLLoader mp = new FXMLLoader(getClass().getResource("../gui/MainWindow.fxml"));
        mainPane = mp.load();
        mainController = mp.getController();
        mainController.setParent(this);

        FXMLLoader rp = new FXMLLoader(getClass().getResource("../gui/game/room.fxml"));
        roomPane = rp.load();
        roomController = rp.getController();
        roomController.setParent(this);

        FXMLLoader gp = new FXMLLoader(getClass().getResource("../gui/game/game.fxml"));
        gamePane = gp.load();
        gameController = gp.getController();
        gameController.setParent(this);

        primaryStage.setTitle("Koło fortuny");
//        primaryStage.setScene(new Scene(gamePane, 400, 400));
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
        this.loginName = name;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                primaryStage.setScene(new Scene(mainPane, 800,400));
                mainController.setName(name);
            }
        });
    }

    protected void enterRoom(String name){
        mainController.enterGameRoom(name);
    }

    public synchronized void listen(byte[] data) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String fullMsg = new String(data);
                System.out.println("Received from server: " + fullMsg);
                String[] msg = new String(data).split(";");

                if (msg.length < 2) {
                    System.err.println("Message to short: " + fullMsg);
                    return;
                }

                switch (msg[1]) {
                    case "chat":
                        if (msg.length < 3) {
                            System.err.println("Message to short: " + fullMsg);
                            return;
                        }
                        mainController.generalMessage(msg[0].trim(), msg[2].trim());
                        break;
                    case "rchat":
                        if (msg.length < 4) {
                            System.err.println("Message to short: " + fullMsg);
                            return;
                        }
                        mainController.roomMessage(msg[0].trim(), msg[2].trim(), msg[3].trim());
                        break;
                    case "game":
                        if (msg.length < 3) {
                            System.err.println("Message to short: " + fullMsg);
                            return;
                        }
                        switch (msg[2].trim()) {
                            case "login":
                                if (msg.length < 5) {
                                    System.err.println("Message to short: " + fullMsg);
                                    return;
                                }

                                switch (msg[3]) {
                                    case "ok":
                                        login(msg[4]);
                                        break;
                                    case "fail":
                                        if (msg.length < 6) {
                                            System.err.println("Message to short: " + fullMsg);
                                            return;
                                        }
                                        loginPage.loginFailed(msg[5]);
                                        break;
                                }
                                break;
                            case "roomList":
                                if (msg.length < 5) {
                                    System.err.println("Message to short: " + fullMsg);
                                    return;
                                }
                                switch (msg[3]) {
                                    case "all":
                                        Room[] roomList = new Room[(msg.length - 3) / 3];
                                        for (int i = 0; i < (msg.length - 3) / 3; i++) {
                                            roomList[i] = new Room(msg[4 + i * 3]);
                                            roomList[i].setPlayer1(msg[5 + i * 3]);
                                            roomList[i].setPlayer2(msg[6 + i * 3]);
                                        }
                                        mainController.roomListReceived(roomList);
                                        break;
                                    case "create":
                                        switch (msg[4]) {
                                            case "ok":
                                                if (msg.length < 6) {
                                                    System.err.println("Message to short: " + fullMsg);
                                                    return;
                                                }
                                                mainController.createRoom(msg[5].trim(),msg[6].trim());

                                                if (msg[6].trim().equals(mainController.getName().trim())) {
                                                    mainController.enterRoom(msg[5].trim(), msg[3].trim().equals("p1"), Main.getInstance().getLogin().trim());
                                                }
                                        }
                                        break;
                                    case "leave":
                                        leaveRoom(msg[0], msg[4]);
                                        break;
                                    case "delete":
                                        mainController.deleteRoom(msg[4].trim());
                                        break;
                                }
                                break;
                            case "joinRoom":
                                if (msg[5].trim().equals(mainController.getName().trim())) {
                                    System.out.println("To chodzi o mnie :)");
                                    mainController.enterRoom(msg[4], msg[3].equals("p1"), msg[5]);
                                }else{
                                    System.out.println("To chodzi o kogośtam...");
                                    mainController.joinRoom(msg[4].trim(), msg[5].trim());
                                }
                                break;

                            case "init":
                                if(msg.length==3)
                                    mainController.initGame("");
                                else
                                    mainController.initGame(msg[3].trim());
                                break;

                            case "cg":
                                mainController.guessChar(msg[0].trim(),msg[3].trim(),msg[4].trim(),msg[5].trim(),msg[6].trim(),msg[7].trim());
                                break;
                            case "gp":
                                mainController.guessPass(msg[3].trim(),msg[4].trim(),msg[5].trim(),msg[6].trim());
                                break;

                        }
                        break;
                }
            }});
    }

    public void leaveRoom(String name, String roomName) {
        mainController.leaveRoom(name, roomName);
    }

    public String getLogin() {
        return loginName;
    }

    public GameController getGameController(){
        return gameController;
    }

    public Pane getGamePane(){
        return gamePane;
    }

    public static Main getInstance() {
        return instance;
    }
}
