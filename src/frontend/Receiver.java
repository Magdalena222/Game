package frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    protected InetAddress group;
    protected IBroadcastListener listener;

    public Receiver(IBroadcastListener listener) throws IOException {
        socket = new MulticastSocket(777);
        group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        this.listener = listener;
    }

    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            listener.listen(packet.getData());
            if(this.isInterrupted()){
                socket.close();
                break;
            }
        }
    }
}
