package backend;

import java.io.IOException;

public class ChatMessageHandler implements IMessageHandler{

    protected Server server;

    public ChatMessageHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(String[] msg) {
        if(msg[1].equals("chat")) {
            try {
                Broadcast.getInstance().send(String.join(";", msg).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(msg[1].equals("rchat")) {
            try {
                server.sendToRoom(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
