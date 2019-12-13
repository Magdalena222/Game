package frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

public class ServerReceiver extends Thread {

    public static final int PORT = 477;
    protected IServerListener listener;
    protected DatagramSocket socket;

    public ServerReceiver(IServerListener listener) throws IOException {
        socket = new DatagramSocket(PORT);
        this.listener = listener;
    }

    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Echh... " + new String(packet.getData()));
            listener.listen(packet.getData());
            if(this.isInterrupted()){
                socket.close();
                break;
            }
        }
    }
}
