package frontend;

import main.Main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerReceiver extends Thread {

    public static final int PORT = 477;
    protected Main listener;
    protected DatagramSocket socket;

    public ServerReceiver(Main listener) throws IOException {
        socket = new DatagramSocket(PORT);
        socket.setSoTimeout(10000);
        this.listener = listener;
    }

    public void run() {
        System.out.println("Server receiver started");
        boolean done = false;
        while (!done) {
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            try {
                socket.receive(packet);
                listener.listen(packet.getData());
            } catch (Exception e) {
                System.out.println("Server timeout");
            }
            if(this.isInterrupted()){
                done = true;
            }
        }
        socket.close();
        System.out.println("Server closed");
    }
}
