package com.magdalena.common.game;

import com.magdalena.common.room.Room;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void fullTest() {
        Room room = new Room("Koko", "Jojo", "Koko jest spoko");
        room.setSecondPlayer("Koko");
        assertEquals(4, room.getGame().guess("Jojo", 'o'));
        assertEquals(400, room.getGame().getP1Points());
        assertEquals(0, room.getGame().getP2Points());
        assertTrue(room.getGame().guess("Koko", "Koko jest spoko"));
        assertEquals(400, room.getGame().getP1Points());
        assertEquals(1000, room.getGame().getP2Points());
    }
}