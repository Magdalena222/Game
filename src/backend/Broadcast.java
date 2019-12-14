package backend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Broadcast{

    private DatagramSocket socket;
    private InetAddress group;

    public Broadcast() throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
    }

    public void send(byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, group, 755);
        socket.send(packet);
        socket.close();
    }
}
