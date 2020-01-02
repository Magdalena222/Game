package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

            File inputFile = new File(GameSettings.class.getResource("settings.xml").getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("settings").item(0).getChildNodes();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    switch (eElement.getAttribute("name").trim()) {
                        case "server":
                            server = new Setting(
                                    InetAddress.getByName(eElement
                                            .getElementsByTagName("address")
                                            .item(0)
                                            .getTextContent()),
                                    Integer.parseInt(eElement
                                            .getElementsByTagName("port")
                                            .item(0)
                                            .getTextContent()));
                        case "broadcast":
                            broadcast = new Setting(
                                    InetAddress.getByName(eElement
                                            .getElementsByTagName("address")
                                            .item(0)
                                            .getTextContent()),
                                    Integer.parseInt(eElement
                                            .getElementsByTagName("port")
                                            .item(0)
                                            .getTextContent()));
                        case "gameReceiver":
                            gameReceiver = new Setting(
                                    InetAddress.getByName(eElement
                                            .getElementsByTagName("address")
                                            .item(0)
                                            .getTextContent()),
                                    Integer.parseInt(eElement
                                            .getElementsByTagName("port")
                                            .item(0)
                                            .getTextContent()));
                    }
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Failed to setup settings");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    public static GameSettings getInstance(){
        if(null == instance) instance = new GameSettings();
        return instance;
    }

    public Setting getServer() {
        return server;
    }

    public Setting getBroadcast() {
        return broadcast;
    }

    public Setting getGameReceiver() {
        return gameReceiver;
    }
}
