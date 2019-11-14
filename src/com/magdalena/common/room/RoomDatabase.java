package com.magdalena.common.room;

import java.util.Set;
import java.util.TreeSet;

public class RoomDatabase {
    protected Set<Room> rooms;

    public RoomDatabase() {
        rooms = new TreeSet<Room>();
    }

    public boolean add(Room r){
        return null==r?false:rooms.add(r);
    }

    public boolean remove(Room r){
        return null==r?false:rooms.remove(r);
    }

    public Set<Room> getAllRooms(){
        return new TreeSet<Room>(rooms);
    }
}
