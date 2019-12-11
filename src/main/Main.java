package main;

import backend.ChatMessageHandler;
import backend.GameMessageHandler;
import backend.MessageHandler;
import backend.Server;

public class Main{

    public static void main(String[] args){
        try{
            System.out.println("Staring server...");
            Server server = new Server("Server");
            server.setHandler(new MessageHandler(server,new ChatMessageHandler(server), s-> {System.out.println(s[2]);}));
            server.start();
        }catch(Exception e){
            System.err.println("Failed to start server!");
            System.err.println(e.getMessage());
        }
    }
}
