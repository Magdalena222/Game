package com.magdalena.backend;

import java.io.IOException;
import java.net.*;

public class MultiServer extends Thread{

    protected DatagramSocket serverSocket;

    public MultiServer(int port) throws IOException {
        super();
        serverSocket = new DatagramSocket();
    }

    @Override
    public void run() {
        InetAddress group = null;
        try {
            group = InetAddress.getByName("230.0.0.0");
        } catch (UnknownHostException e) {
            return;
        }
        byte[] buf = "Hello".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        while(true){
            try {
                serverSocket.send(packet);
                Thread.currentThread().sleep(5000);
            } catch (Exception e) {
                break;
            }
        }
        serverSocket.close();
    }
}
