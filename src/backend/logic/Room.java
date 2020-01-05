package backend.logic;

public class Room {
    String name;
    String player1;
    String player2;
    Game game;

    public Room(String name, String p1) {
        this.name = name;
        player1 = p1;
        player2 = "Wolny";
        game = new Game();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Game getGame() {
        return game;
    }
}
