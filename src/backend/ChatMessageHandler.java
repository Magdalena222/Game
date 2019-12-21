package backend;

import java.io.IOException;

public class ChatMessageHandler implements IMessageHandler{

    protected Server server;

    public ChatMessageHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(String[] msg) {
        System.out.println("Received message '" + msg[2].trim() +"' from " + msg[0]);
        try {
            Broadcast.getInstance().send(String.join(";", msg).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
