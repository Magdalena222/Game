package backend;

public class MessageHandler implements IMessageHandler{

    protected Server server;
    protected IMessageHandler chatHandler;
    protected IMessageHandler gameHandler;

    public MessageHandler(Server server, IMessageHandler chatHandler, IMessageHandler gameHandler) {
        this.server = server;
        this.chatHandler = chatHandler;
        this.gameHandler = gameHandler;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setChatHandler(IMessageHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    public void setGameHandler(IMessageHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void handle(String[] msg){
        switch (msg[1]){
            case "chat":
                chatHandler.handle(msg);
                break;
            case "game":
                gameHandler.handle(msg);
                break;
            default:
                System.err.println("Unrecognised message!");
        }

    }
}
