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
        System.out.println(String.join(";",msg));
        if(msg.length>2)
            switch (msg[2]){
                case "login":
                    if(msg.length<5){
                        System.err.println("Message to short: " + String.join(";",msg));
                    }
                    String res = server.checkLogin(msg[0], msg[3], Integer.parseInt(msg[4].trim()));
                    System.out.println(res);
                    try {
                        server.send(res.getBytes(), InetAddress.getByName(msg[3]), Integer.parseInt(msg[4].trim()));
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "roomList":
                    if(msg.length<4){
                        System.err.println("Message to short: " + String.join(";",msg));
                    }
                    switch (msg[3].trim()){
                        case "getAll":
                            server.sendRoomList(msg[0]);
                            break;
                        case "create":
                            if(msg.length<5){
                                System.err.println("Message to short: " + String.join(";",msg));
                            }
                            server.createRoom(msg[0], msg[4]);
                            break;
                        case "leave":
                            if(msg.length<5){
                                System.err.println("Message to short: " + String.join(";",msg));
                            }
                            server.leaveRoom(msg[0].trim(), msg[4].trim());
                            break;
                        default:
                            System.err.println("Unrecognised message!");
                    }
                    break;

                case "joinRoom":
                    if(msg.length<4){
                        System.err.println("Message to short: " + String.join(";",msg));
                    }
                    server.joinRoom(msg[3], msg[0]);
                    break;
                case "cg":
                    if(msg.length<5){
                        System.err.println("Message to short: " + String.join(";",msg));
                    }
                    server.guessChar(msg[0].trim(),msg[3].trim(),msg[4].trim());
                    break;
                case "gp":
                    if(msg.length<5){
                        System.err.println("Message to short: " + String.join(";",msg));
                    }
                    server.guessPass(msg[0].trim(),msg[3].trim(),msg[4].trim());
                    break;
                default:
                    System.err.println("Unrecognised message!");
            }
    }
}