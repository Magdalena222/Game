package backend;

import backend.logic.Room;
import frontend.Sender;
import main.GameSettings;
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
        this.server = new DatagramSocket(GameSettings.getInstance().getServer().getPort());
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
        System.out.println("Serching for room to join " + roomName.trim());
        for (String rn: rooms.keySet()
        ) {
            System.out.println(rn);
        }
        if(rooms.containsKey(roomName.trim())){
            Room room = rooms.get(roomName.trim());
            System.out.println("Rooms containe " + room.getPlayer1() + " " + room.getPlayer2());
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
            if(!room.getPlayer1().equals("Wolny") && !room.getPlayer2().equals("Wolny")){
                room.getGame().reset();
                try {
                    send(("server;game;init;"+room.getGame().getPassword()).getBytes(), clients.get(room.getPlayer1()).address, clients.get(room.getPlayer1()).port);
                    send(("server;game;init;"+room.getGame().getPassword()).getBytes(), clients.get(room.getPlayer2()).address, clients.get(room.getPlayer2()).port);
                    sendToRoom((room.getName().trim() + ";game;cg;"+room.getPlayer2().trim()+";0;"+room.getGame().getP1Points()+";"+room.getGame().getP2Points()+";"+room.getGame().getPrice()).split(";"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            System.out.println("Rooms not containe " + roomName.trim());
        }
    }

    public void createRoom(String clientName, String roomName){
        System.out.println("Serching for room to create " + roomName.trim());
        for (String rn: rooms.keySet()
        ) {
            System.out.println(rn);
        }
        if(rooms.containsKey(roomName.trim())) {
            try {
                send("server;game;roomList;create;failed;Istnieje już pokój o podanej nazwie".getBytes(), clients.get(clientName).address, clients.get(clientName).port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Room r = new Room(roomName.trim(), clientName.trim());
                rooms.put(roomName.trim(), r);
                Broadcast.getInstance().send(("server;game;roomList;create;ok;"+roomName.trim()+";"+clientName.trim()).getBytes());
                if(!r.getPlayer2().equals("Wolny") && !r.getPlayer1().equals("Wolny"))
                    sendToRoom((roomName.trim() + ";game;init;"+r.getGame().getPassword().trim()).split(";"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToRoom(String[] msg) throws IOException {
        if(msg.length>2){
            if(rooms.containsKey(msg[0])){
                Room room = rooms.get(msg[0]);
                if(!room.getPlayer1().equals("Wolny"))
                    send(String.join(";", msg).getBytes(), clients.get(room.getPlayer1()).address, clients.get(room.getPlayer1()).port);
                if(!room.getPlayer2().equals("Wolny"))
                    send(String.join(";", msg).getBytes(), clients.get(room.getPlayer2()).address, clients.get(room.getPlayer2()).port);
            }else{
                System.err.println("There is no room named " + msg[0]);
            }
        }else{
            System.err.println("Message to short " + String.join(";", msg));
        }
    }

    public void leaveRoom(String login, String roomName) {
        if(rooms.containsKey(roomName)){
            Room room = rooms.get(roomName);
            if(room.getPlayer1().trim().equals(login.trim())) room.setPlayer1("Wolny");
            else room.setPlayer2("Wolny");
            try {
                Broadcast.getInstance().send((login+";game;roomList;leave;"+roomName).getBytes());
                if(room.getPlayer1().trim().equals("Wolny") && room.getPlayer2().trim().equals("Wolny")) {
                    rooms.remove(roomName);
                    Broadcast.getInstance().send((login + ";game;roomList;delete;" + roomName).getBytes());
                }else{
                    sendToRoom((roomName.trim() + ";game;init;").split(";"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void guessChar(String player, String roomName, String ch) {
        int num = rooms.get(roomName).getGame().guess(ch.charAt(0));
        if(rooms.get(roomName).getPlayer1().trim().equals(player.trim())){
            rooms.get(roomName).getGame().setP1Points(rooms.get(roomName).getGame().getP1Points()+num*rooms.get(roomName).getGame().getPrice());
            rooms.get(roomName).getGame().newPrice();
        }
        if(rooms.get(roomName).getPlayer2().trim().equals(player.trim())){
            rooms.get(roomName).getGame().setP2Points(rooms.get(roomName).getGame().getP2Points()+num*rooms.get(roomName).getGame().getPrice());
            rooms.get(roomName).getGame().newPrice();
        }
        try {
            rooms.get(roomName).getGame().newPrice();
            sendToRoom((roomName.trim() + ";game;cg;"+player.trim()+";"+ch+";"+rooms.get(roomName).getGame().getP1Points()+";"+rooms.get(roomName).getGame().getP2Points()+";"+rooms.get(roomName).getGame().getPrice()).split(";"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guessPass(String player, String roomName, String pass) {
        Room r = rooms.get(roomName.trim());
        if(r!=null){
            System.out.println(pass.trim().toUpperCase() + " => " + r.getGame().getPassword().toUpperCase().trim());
            if(pass.trim().toUpperCase().equals(r.getGame().getPassword().toUpperCase().trim())){
                System.out.println(pass.trim().toUpperCase());
                System.out.println(r.getGame().getPassword().toUpperCase().trim());
                if(r.getPlayer1().equals(player))
                    r.getGame().setP1Points(r.getGame().getP1Points() + 2000);
                else
                    r.getGame().setP2Points(r.getGame().getP2Points() + 2000);
                try {
                    sendToRoom((roomName + ";game;gp;" + player.trim() + ";ok;" + r.getGame().getP1Points() + ";" + r.getGame().getP2Points()).split(";"));
                    sendToRoom((roomName.trim() + ";game;cg;" + player.trim() + ";0;" + rooms.get(roomName.trim()).getGame().getP1Points() + ";" + rooms.get(roomName.trim()).getGame().getP2Points() + ";" + rooms.get(roomName.trim()).getGame().getPrice()).split(";"));
                    Broadcast.getInstance().send(("server;game;roomList;delete;" + roomName).getBytes());
                    rooms.remove(roomName);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    sendToRoom((roomName+";game;gp;"+player+";fail;"+r.getGame().getP1Points()+";"+r.getGame().getP2Points()).split(";"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
