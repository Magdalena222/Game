package main;

import backend.ChatMessageHandler;
import backend.GameMessageHandler;
import backend.MessageHandler;
import backend.Server;
import frontend.Sender;
import gui.LoginController;
import gui.MainWindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    protected Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        FXMLLoader load = new FXMLLoader(getClass().getResource("../gui/login.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(load.load(), 800, 400));
        load.<LoginController>getController().setParent(this);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            try {
                Sender.getInstance().send("kok");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args){
        launch(args);
    }

    public void login(String name){
        System.out.println("Zalogowano " + name);
        FXMLLoader load = new FXMLLoader(getClass().getResource("../gui/MainWindow.fxml"));
        try {
            primaryStage.setScene(new Scene(load.load(), 800,400));
            load.<MainWindowController>getController().setName(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
