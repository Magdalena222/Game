package frontend;

import backend.ClientInfo;
import backend.Server;

import java.io.IOException;
import java.net.*;

public class Sender {
    private final int PORT = 999;
    private DatagramSocket socket;
    private static Sender instance;

    private Sender() throws SocketException {
        socket = new DatagramSocket(PORT);
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
        int msgLenght = msg.length();
        DatagramPacket p = new DatagramPacket(msg.getBytes(), msgLenght, InetAddress.getByName("localhost"), 666);
        socket.send(p);
    }

    public ClientInfo getClientInfo(){
        try {
            return new ClientInfo(InetAddress.getByName("localhost"), PORT);
        }catch(IOException e){
            return null;
        }
    }
}
