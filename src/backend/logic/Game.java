package backend.logic;

import java.util.Random;

public class Game {

    String passwords[] = {"GRA O TRON", "RODZINA SOPRANO", "RODZINA OD ZARAZ", "MATRIX", "OJCIEC CHRZESTNY"};
    String password;
    String hash;
    int price;
    int p1Points;
    int p2Points;

    public Game() {
        reset();
        newPrice();
    }

    public String getPassword(){
        return password;
    }

    public int guess(char ch) {
        int num = 0;
        int pos = 0;
        char c = Character.toUpperCase(ch);
        StringBuilder pass = new StringBuilder(password);
        StringBuilder hpass = new StringBuilder(hash);

        while(pos>=0){
            pos = password.indexOf(c, pos);
            if(pos>=0){
                num++;
                hpass.setCharAt(pos, c);
                pass.setCharAt(pos, '#');
                pos++;
            }
        }
        hash = hpass.toString();
        password = pass.toString();
        return num;
    }

    public void reset() {
        p1Points = 0;
        p2Points = 0;
        password = passwords[new Random().nextInt(passwords.length)];
        hash = this.password.replaceAll("[A-Z]", "#");
    }

    public void newPrice(){
        price = new Random().nextInt(10)*100 + 100;
    }

    public int getPrice() {
        return price;
    }

    public int getP1Points() {
        return p1Points;
    }

    public void setP1Points(int p1Points) {
        this.p1Points = p1Points;
    }

    public int getP2Points() {
        return p2Points;
    }

    public void setP2Points(int p2Points) {
        this.p2Points = p2Points;
    }
}
