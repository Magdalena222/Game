package frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class BroadcastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    protected InetAddress group;
    protected IBroadcastListener listener;

    public BroadcastReceiver(IBroadcastListener listener) throws IOException {
        socket = new MulticastSocket(777);
        group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
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
            listener.listen(packet.getData());
            if(this.isInterrupted()){
                socket.close();
                break;
            }
        }
    }
}
