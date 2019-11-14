package com.magdalena;

import com.magdalena.backend.MultiServer;
import com.magdalena.backend.MultiServerHandler;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new MultiServer(4446).start();

        int i;
        for(i = 5; i < 10; i++){
            System.out.println("Building receiver " + i);
            Thread sh = new MultiServerHandler(i);
            sh.start();
        }
    }
}
