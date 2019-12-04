package main;

import gui.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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


    public static void main(String[] args) {
        launch(args);
    }
}
