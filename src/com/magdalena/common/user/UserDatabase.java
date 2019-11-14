package com.magdalena.common.user;

import java.util.Set;
import java.util.TreeSet;

public class UserDatabase {
    protected Set<String> users;

    public UserDatabase() {
        users = new TreeSet<>();
    }

    public boolean add(String name){
        return users.add(name);
    }

    public boolean remove(String name){
        return users.remove(name);
    }

    public Set<String> getAllUsers(){
        Set u = new TreeSet(users);
        return u;
    }
}
