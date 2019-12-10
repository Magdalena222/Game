package main;

import backend.ChatMessageHandler;
import backend.MessageHandler;
import backend.Server;
import frontend.Receiver;
import frontend.Sender;
import gui.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("../gui/MainWindow.fxml"));
        FXMLLoader load = new FXMLLoader(getClass().getResource("../gui/MainWindow.fxml"));
        load.setController(new MainWindowController());

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(load.load(), 800, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args){
        try{
            System.out.println("Staring server...");
            Server server = new Server("Server");
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
