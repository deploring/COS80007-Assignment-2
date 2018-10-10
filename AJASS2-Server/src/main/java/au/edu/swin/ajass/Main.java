package au.edu.swin.ajass;

import au.edu.swin.ajass.controllers.ServerController;

/**
 * Created by Heartist on 3/10/2018.
 */
public class Main {

    public static void main(String[] args){
        System.out.println("-------------------- HELLO! --------------------");
        System.out.println("> Now attempting to load AJASS2-Server v0.1...");
        // Just simply pass off everything to the Server Controller.
        new ServerController();
    }
}
