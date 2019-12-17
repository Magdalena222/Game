package gui.game;

import frontend.IBroadcastListener;
import javafx.application.Platform;

public class GameController implements IBroadcastListener {
    @Override
    public void listen(byte[] msg) {
        Platform.runLater(new Runnable() {
            public void run() {
                String[] s = new String(msg).split(";");
                if(s[1].equals("game")) {

                }
            }
        });
    }
}
