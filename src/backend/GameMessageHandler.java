package backend;

import java.io.IOException;
import java.net.InetAddress;

public class GameMessageHandler implements IMessageHandler{

    protected Server server;

    public GameMessageHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(String[] msg) {
        if(msg.length>2)
            switch (msg[2]){
                case "login":
                    System.out.println(String.join(";",msg));
                    String res = server.checkLogin(msg[0], msg[3], Integer.parseInt(msg[4].trim()));
                    System.out.println(res);
                    try {
                        server.send(res.getBytes(), InetAddress.getByName(msg[3]), Integer.parseInt(msg[4].trim()));
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "joinRoom":
//                    server.joinRoom(msg[3], msg[0]);
                    break;
                default:
                    System.err.println("Unrecognised message!");
            }
    }
}
