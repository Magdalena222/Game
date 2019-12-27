package gui.chat;

import frontend.IBroadcastListener;
import frontend.BroadcastReceiver;
import frontend.Sender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;

public class GeneralChatController implements IBroadcastListener {

    @FXML public TextArea textArea;
    @FXML public TextField textField;
    protected String name;


    public void onEnter(ActionEvent ae){
        try {
            System.out.println("Chat is sending " + name + ";chat;"+textField.getText());
            Sender.getInstance().send(name.trim() + ";chat;"+textField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textField.setText("");
    }

    @Override
    public synchronized void listen(byte[] msg) {
        Platform.runLater(new Runnable() {
            public void run() {
                String[] s = new String(msg).split(";");
                if(s[1].equals("chat")) {
                    textArea.setText(s[0] + ": " + s[2] + "\n" + textArea.getText());
                }
            }
        });
    }

    public synchronized void message(String from, String msg){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.setText(from + ": " + msg + "\n" +textArea.getText());
            }
        });
    }

    public void setName(String name) {
        this.name = name;
    }
}
