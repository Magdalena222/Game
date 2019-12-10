package backend;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread {

    DatagramSocket server;
    Map<String, ClientInfo> clients;
    String name;
    IMessageHandler handler;

    public Server(String name) throws IOException {
        this.clients = new HashMap<String, ClientInfo>();
        this.name = name;
        this.server = new DatagramSocket(666);
    }

    public void setHandler(IMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        while(true){
            DatagramPacket dp = new DatagramPacket(new byte[250], 250);
            try {
                server.receive(dp);
                handle(dp);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }

    protected void handle(DatagramPacket dp) throws IOException {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String msg = new String(dp.getData());
        String[] message = msg.split(";");

        if(!clients.containsKey(message[0])){
            clients.put(message[0], new ClientInfo(dp.getAddress(), dp.getPort(), message[0]));
        }
        handler.handle(message);
    }

    public void send(byte[] data, InetAddress adress, int port) throws IOException {
        System.out.println(this.name + " is sending to " + adress.toString() + " " + port + ": " + new String(data));
        DatagramPacket senddp = new DatagramPacket(data, data.length,
                adress, port);
        server.send(senddp);
    }

    public ClientInfo getClientInfo(String name){
        return clients.get(name);
    }
}
