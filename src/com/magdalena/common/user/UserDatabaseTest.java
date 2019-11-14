package com.magdalena.common.user;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Set;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDatabaseTest {

    protected UserDatabase db;

    public UserDatabaseTest() {
        db = new UserDatabase();
    }

    @Test
    public void add() {
        assertTrue("Can not add 'Maciek'",db.add("Maciek"));
        assertTrue("Can not add 'Aneta'",db.add("Aneta"));
        assertTrue("Can not add 'Tomek'",db.add("Tomek"));
        assertFalse("Can add 'Maciek'",db.add("Maciek"));
    }

    @Test
    public void remove() {
        add();
        assertTrue(db.remove("Maciek"));
        assertFalse(db.remove("Maciek"));
    }

    @Test
    public void testGetAllUsers() {
        remove();
        Set<String> users = db.getAllUsers();
        assertTrue(users.contains("Aneta"));
        assertTrue(users.contains("Tomek"));
        assertFalse(users.contains("Maciek"));
    }
}