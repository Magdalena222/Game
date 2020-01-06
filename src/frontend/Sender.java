package frontend;

import backend.ClientInfo;
import backend.Server;
import main.GameSettings;

import java.io.IOException;
import java.net.*;

public class Sender {
    private DatagramSocket socket;
    private static Sender instance;

    private Sender() throws SocketException {
        socket = new DatagramSocket(GameSettings.getInstance().getGameSender().getPort());
    }

    public static Sender getInstance(){
        if(null == instance) {
            try {
                instance = new Sender();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void send(String msg) throws IOException {
        System.out.println("Sending " + msg);
        int msgLenght = msg.length();
        DatagramPacket p = new DatagramPacket(msg.getBytes(), msgLenght, GameSettings.getInstance().getServer().getAdress(), GameSettings.getInstance().getServer().getPort());
        socket.send(p);
    }

    public ClientInfo getClientInfo(){
        try {
            return new ClientInfo(InetAddress.getByName("localhost"), GameSettings.getInstance().getGameSender().getPort());
        }catch(IOException e){
            return null;
        }
    }
}
