package com.magdalena.backend;

import java.io.IOException;
import java.net.*;

public class MultiServerHandler extends Thread {
    protected MulticastSocket socket;
    protected int id;

    public MultiServerHandler(int id) throws IOException {
        super();
        System.out.println("Creating receiver " + id);
        this.socket = new MulticastSocket(4446);
        this.id = id;
    }

    @Override
    public void run() {
        InetAddress group = null;
        try {
            group = InetAddress.getByName("230.0.0.0");
        } catch (UnknownHostException e) {
            return;
        }
        try {
            socket.joinGroup(group);
        } catch (IOException e) {
            return;
        }

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            try{
                socket.receive(packet);
            }catch (Exception e){
                return;
            }

            String received = new String(
                    packet.getData(), 0, packet.getLength());
            if ("end".equals(received)) {
                break;
            }
            System.out.println(this.id + " => " +received);
        }
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            return;
        }
        socket.close();
    }
}
