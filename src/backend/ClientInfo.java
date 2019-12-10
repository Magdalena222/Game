package backend;

import java.net.InetAddress;

public class ClientInfo {
    protected InetAddress address;
    protected int port;
    protected String name;

    public ClientInfo(InetAddress address, int port, String name) {
        this.address = address;
        this.port = port;
        this.name = name;
    }

    public ClientInfo(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.name = "Client";
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return address.getHostName()+";"+port;
    }
}
