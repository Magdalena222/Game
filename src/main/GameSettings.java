package main;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameSettings {

    public class Setting{
        protected InetAddress adress;
        int port;

        public Setting() {

        }

        public Setting(InetAddress adress, int port) {
            this.adress = adress;
            this.port = port;
        }

        public InetAddress getAdress() {
            return adress;
        }

        public void setAdress(InetAddress adress) {
            this.adress = adress;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    private static GameSettings instance;
    protected Setting server;
    protected Setting broadcast;
    protected Setting gameReceiver;

    private GameSettings() {
        try {
            server = new Setting(InetAddress.getByName("localhost"), 666);
            broadcast = new Setting(InetAddress.getByName("230.0.0.0"), 755);
            gameReceiver = new Setting(InetAddress.getByName("localhost"), 477);
        } catch (UnknownHostException e) {
            System.err.println("Failed to setup settings");
        }
    }

    public static GameSettings getInstance(){
        if(null == instance) instance = new GameSettings();
        return instance;
    }
}
