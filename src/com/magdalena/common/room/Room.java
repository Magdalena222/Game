package com.magdalena.common.room;

import com.magdalena.common.game.Game;

import java.security.InvalidParameterException;

public class Room implements Comparable{
    protected String name;
    protected String firstPlayer;
    protected String secondPlayer;
    protected Game game;

    public Room(String name, String player) {
        this.name = name;
        firstPlayer = player;
        this.game = new Game(this);
    }

    public Room(String name, String player, String pass) {
        this.name = name;
        firstPlayer = player;
        this.game = new Game(this, pass);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public boolean setFirstPlayer(String firstPlayer) {
        if(null!=firstPlayer) return false;
        this.firstPlayer = firstPlayer;
        return true;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public boolean setSecondPlayer(String secondPlayer) {
        if(null!=secondPlayer) return false;
        this.secondPlayer = secondPlayer;
        return true;
    }

    @Override
    public int compareTo(Object o) throws InvalidParameterException{
        if(o instanceof Room)
            return this.name.compareTo(((Room)o).getName());
        throw new InvalidParameterException();
    }

    public Game getGame() {
        return game;
    }
}
