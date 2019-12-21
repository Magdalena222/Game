package backend;

import backend.logic.Room;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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
    Map<String, Room> rooms;

    public Server(String name) throws IOException {
        this.clients = new HashMap<String, ClientInfo>();
        this.name = name;
        this.server = new DatagramSocket(666);
        rooms = new HashMap<String, Room>();
        rooms.put("Koko", new Room("Koko", "McKing"));
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
        for (Room room:rooms.values()) {
            roomsString = roomsString + ";" + room.getName().trim() + ";" + room.getPlayer1().trim() + ";" + room.getPlayer2().trim();
        }
        ClientInfo clientInfo = clients.get(name);
        try {
            send(roomsString.getBytes(), clientInfo.address, clientInfo.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinRoom(String roomName, String login){
        if(rooms.containsKey(roomName.trim())){
            System.out.println("Rooms containe");
            Room room = rooms.get(roomName.trim());
            if(room.getPlayer1().equals("Wolny")){
                System.out.println("Pierwszy wolny");
                room.setPlayer1(login);
                try {
                    Broadcast.getInstance().send(("server;game;joinRoom;p1;"+roomName.trim()+";"+login.trim()).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(room.getPlayer2().equals("Wolny")){
                System.out.println("Drugi wolny");
                room.setPlayer2(login);
                try {
                    Broadcast.getInstance().send(("server;game;joinRoom;p2;"+roomName.trim()+";"+login.trim()).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    send(("server;game;joinRoom;"+roomName.trim()+";fail").getBytes(), clients.get(login).address, clients.get(login).port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            System.out.println("Rooms not containe");
        }
    }

    public void createRoom(String clientName, String roomName){
        if(rooms.containsKey(roomName)) {
            try {
                send("server;game;roomList;create;failed;Istnieje już pokój o podanej nazwie".getBytes(), clients.get(clientName).address, clients.get(clientName).port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                rooms.put(roomName, new Room(roomName, clientName));
                Broadcast.getInstance().send(("server;game;roomList;create;ok;"+roomName+";"+clientName).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
