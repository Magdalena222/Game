package com.magdalena.common.game;

import com.magdalena.common.room.Room;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class Game {
    protected Room room;
    protected String originPassword;
    protected String password;
    protected String hashPassword;
    protected static String[] passwords = {"Do trzech razy sztuka", "Gra o tron"};
    protected int p1Points;
    protected int p2Points;

    public Game(Room r, String p) {
        Random rand = new Random();
        room = r;
        if(null == p)
            originPassword = passwords[rand.nextInt(passwords.length)].toUpperCase();
        else
            originPassword = p.toUpperCase();
        password = originPassword;
        hashPassword = password.replaceAll("[A-Z]", "#");
        p1Points = 0;
        p2Points = 0;
    }

    public Game(Room r) {
        this(r, null);
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public int guess(String player, char c){
        int num = 0;
        int pos = 0;
        c = Character.toUpperCase(c);
        StringBuilder pass = new StringBuilder(password);
        StringBuilder hpass = new StringBuilder(hashPassword);

        while(pos>=0){
            pos = password.indexOf(c, pos);
            if(pos>=0){
                num++;
                pass.setCharAt(pos, '#');
                hpass.setCharAt(pos, c);
                pos++;
            }
        }
        password = pass.toString();
        hashPassword = hpass.toString();
        if(room.getFirstPlayer().equals(player)) p1Points += num*100;
        else p2Points += num*100;
        return num;
    }

    public String getPassword() {
        return password;
    }

    public String getOriginPassword() {
        return originPassword;
    }

    public boolean guess(String player, String pass){

        if(pass.toUpperCase().equals(originPassword)){
            if(room.getFirstPlayer().equals(player)) p1Points += 1000;
            else p2Points += 1000;
            return true;
        }
        return false;
    }

    public int getP1Points() {
        return p1Points;
    }

    public int getP2Points() {
        return p2Points;
    }
}
