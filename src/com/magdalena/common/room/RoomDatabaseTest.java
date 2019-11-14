package com.magdalena.common.room;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class RoomDatabaseTest {

    protected RoomDatabase db;

    public RoomDatabaseTest() {
        db = new RoomDatabase();
    }


    @Test
    public void fullTest() {
        Room room = new Room("Maciek", "Maciek");

        assertFalse(room.setFirstPlayer("Magda"));
        assertTrue(db.add(room));
        assertTrue(db.add(new Room("Magda", "Magda")));

        assertFalse(db.add(room));
        assertFalse(db.remove(new Room("Tomek", "Maciek")));
        assertTrue(db.remove(new Room("Magda", null)));

        Set<Room> all = db.getAllRooms();
        assertTrue(all.size() == 1);
        assertTrue(all.contains(new Room("Maciek", null)));
    }

}