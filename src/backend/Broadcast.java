package backend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Broadcast{

    private DatagramSocket socket;
    private InetAddress group;

    static Broadcast instance;

    private Broadcast() throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
    }

    public static Broadcast getInstance(){
        while (null == instance) {
            try {
                instance = new Broadcast();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return instance;
    }

    public synchronized void send(byte[] data) throws IOException {
        System.out.println("Sending by broadcast: " + new String(data));
        DatagramPacket packet = new DatagramPacket(data, data.length, group, 755);
        socket.send(packet);
    }

    public void close(){
        socket.close();
    }
}
