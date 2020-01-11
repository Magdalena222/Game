package backend;

import main.GameSettings;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    Server server;

    @BeforeEach
    void setUp() {
        try {
            server = new Server("Server");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkLogin() {
            Assert.assertTrue(server.checkLogin("test", "localhost", 123).equals("server;game;login;ok;test"));
            Assert.assertTrue(server.checkLogin("test", "localhost", 123).equals("server;game;login;fail;test;Podany login juz istnieje"));
    }

    @Test
    void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket dp = new DatagramPacket(new byte[250], 250);
                try {
                    DatagramSocket socket = new DatagramSocket(999);
                    socket.setSoTimeout(1000);
                    socket.receive(dp);
                    Assert.assertTrue(new String(dp.getData()).trim().equals("Hello"));
                    System.out.println("Hello received");
                } catch (SocketException e) {
                    System.out.println("Socket unknown error");
                } catch (IOException e) {
                    System.out.println("Socket IOException error");
                }
            }
        }).start();
        try {
            server.send("Hello".getBytes(), InetAddress.getLocalHost(),999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        server.interrupt();
        while(server.isAlive()){
            System.out.println("Waiting for server to shut down...");
            try {
                server.interrupt();
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Sleep interrupted");
            }
        }
    }
}