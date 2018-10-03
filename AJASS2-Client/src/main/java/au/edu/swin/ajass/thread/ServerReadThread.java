package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.controller.ClientController;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * This Thread listens for incoming messages from the
 * server and stores them in the queue for processing.
 */
public class ServerReadThread implements Runnable {

    private final ClientController client;
    private final ObjectInputStream input;

    public ServerReadThread(ClientController client, ObjectInputStream input) {
        this.client = client;
        this.input = input;
    }

    public void run() {
        while (true) {
            try {
                // Read in a new message whenever one comers along.
                client.newMessage(input.readObject());
            } catch (IOException | InterruptedException e) {
                System.out.println(String.format("Unable to read input: %s: %s", e.getClass().getTypeName(), e.getMessage()));
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("Unsupported or unknown class passed through as input?");
                return;
            }
        }
    }
}
