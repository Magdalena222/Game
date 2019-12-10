package main;

import backend.ChatMessageHandler;
import backend.MessageHandler;
import backend.Server;
import frontend.Sender;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    protected static Server server;

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("../gui/MainWindow.fxml"));
        FXMLLoader load = new FXMLLoader(getClass().getResource("../gui/login.fxml"));
//        load.setController(new Controller());

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(load.load(), 800, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            server.interrupt();
            try {
                Sender.getInstance().send("kok");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }


    public static void main(String[] args){
        try{
            System.out.println("Staring server...");
            server = new Server("Server");
            server.setHandler(new MessageHandler(server,new ChatMessageHandler(server), s-> {System.out.println(s[2]);}));
            server.start();
        }catch(Exception e){
            System.err.println("Failed to start server!");
            System.err.println(e.getMessage());
            return;
        }

        launch(args);
    }
}
