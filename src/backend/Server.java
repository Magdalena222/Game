package backend;

import backend.logic.Room;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Server extends Thread {

    DatagramSocket server;
    Map<String, ClientInfo> clients;
    String name;
    IMessageHandler handler;
    List<Room> rooms;

    public Server(String name) throws IOException {
        this.clients = new HashMap<String, ClientInfo>();
        this.name = name;
        this.server = new DatagramSocket(666);
        rooms = new LinkedList<Room>();
        rooms.add(new Room("Koko"));
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
            if(isInterrupted()){
                server.close();
                break;
            }
        }
    }

    protected void handle(DatagramPacket dp) throws IOException {
        String msg = new String(dp.getData());
        System.out.println("Server received: " + msg);
        String[] message = msg.split(";");
        handler.handle(message);
    }

    public String checkLogin(String nick, String host, int port){
        String msg;
        if(clients.containsKey(nick)){
            msg = "server;game;login;fail;"+nick+";Podany login juz istnieje\n";
        }else{
            try{
                clients.put(nick, new ClientInfo(InetAddress.getByName(host), port, nick));
            }catch(UnknownHostException e){
                System.err.println(e.getMessage());
            }
            msg = "server;game;login;ok;" + nick;
        }
        return msg;
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

    public void sendRoomList(String name) {
        String roomsString = name+";game;roomList;all";
        for (Room room:rooms) {
            roomsString = roomsString + ";" + room.getName() + ";" + room.getPlayer1() + ";" + room.getPlayer2();
        }
        ClientInfo clientInfo = clients.get(name);
        try {
            send(roomsString.getBytes(), clientInfo.address, clientInfo.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
