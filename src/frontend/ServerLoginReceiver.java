package frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerLoginReceiver extends Thread {

    public static final int PORT = 477;
    protected IServerLoginListener listener;
    protected DatagramSocket socket;

    public ServerLoginReceiver(IServerLoginListener listener) throws IOException {
        socket = new DatagramSocket(PORT);
        this.listener = listener;
    }

    public void run() {
        boolean done = false;
        while (!done) {
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            try {
                socket.receive(packet);
                String[] msg = new String(packet.getData()).split(";");
                if(msg[2].equals("login")){
                    switch (msg[3]){
                        case "ok":
                            listener.loginOK(msg[4]);
                            done = true;
                            break;
                        case "fail":
                            listener.loginFailed(msg[5]);
                            break;
                        default:
                            System.out.println("Incorrect message: " + String.join(";",msg));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(this.isInterrupted()){
                done = true;
            }
        }
        System.out.println("Closing ServerLoginReceiver");
        socket.close();
    }
}
