package frontend;

import backend.logic.Room;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerRoomListReceiver extends Thread {

    public static final int PORT = 477;
    protected IServerRoomListListener listener;
    protected DatagramSocket socket;

    public ServerRoomListReceiver(IServerRoomListListener listener) throws IOException {
        System.out.println("Staring ServerRoomListReceiver");
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
                System.out.println(String.join(";", msg));
                if(msg[1].equals("game")){
                    switch (msg[2]){
                        case "roomList":
                            switch (msg[3]){
                                case "all":
                                    Room[] roomList = new Room[(msg.length-3)/3];
                                    for(int i = 0; i<(msg.length-3)/3; i++){
                                        roomList[i] = new Room(msg[4+i]);
                                        roomList[i].setPlayer1(msg[5+i]);
                                        roomList[i].setPlayer2(msg[6+i]);
                                    }
                                    listener.roomListReceived(roomList);
                                    break;
                                default:
                                    System.out.println("Incorrect message: " + String.join(";",msg));
                            }
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
        socket.close();
    }
}
