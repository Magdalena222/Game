package frontend;

import main.GameSettings;
import main.Main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.List;

public class BroadcastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected Main listener;
    private static BroadcastReceiver instance;

    public BroadcastReceiver(Main main) throws IOException {
        socket = new MulticastSocket(GameSettings.getInstance().getBroadcast().getPort());
        socket.setSoTimeout(10000);
        InetAddress group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        listener = main;
    }

    public void run() {
        System.out.println("Broadcast receiver started");
        while (true) {
            try{
                if(this.isInterrupted()){
                    break;
                }
                DatagramPacket packet = new DatagramPacket(new byte[256], 256);
                socket.receive(packet);
                listener.listen(packet.getData());
            } catch (Exception e) {
                System.out.println("Broadcast ");
                e.printStackTrace();
            }
        }
        socket.close();
        System.out.println("Broadcast receiver closed");
    }
}
