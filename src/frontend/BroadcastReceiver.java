package frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.List;

public class BroadcastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    protected InetAddress group;
    protected List<IBroadcastListener> listeners;
    private static BroadcastReceiver instance;

    private BroadcastReceiver() throws IOException {
        socket = new MulticastSocket(755);
        group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        this.listeners = new LinkedList<IBroadcastListener>();
    }

    public static BroadcastReceiver getInstance(){
        if(null==instance) {
            try {
                instance = new BroadcastReceiver();
                instance.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void addListener(IBroadcastListener listener){
        listeners.add(listener);
    }

    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            try {
                socket.receive(packet);
                System.out.println("Broadcast received " + new String(packet.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            listeners.forEach(listener -> listener.listen(packet.getData()));
            if(this.isInterrupted()){
                socket.close();
                break;
            }
        }
    }
}
