package frontend;

import backend.Server;

import java.io.IOException;
import java.net.*;

public class Sender {
    private DatagramSocket socket;
    private static Sender instance;

    private Sender() throws SocketException {
        socket = new DatagramSocket(999);
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
}
