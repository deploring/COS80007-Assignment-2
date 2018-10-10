package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.controllers.ServerController;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.models.ClientConnection;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * This Thread listens for incoming messages from
 * clients and stores them in the queue for processing.
 */
public class ClientReadThread implements Runnable {

    private final ServerController server;
    private final ClientConnection client;
    private final ObjectInputStream input;

    public ClientReadThread(ServerController server, ClientConnection client, ObjectInputStream input) {
        this.server = server;
        this.client = client;
        this.input = input;
    }

    public void run() {
        while (true) {
            try {
                // Read in a new message whenever one comers along.
                Object in = input.readObject();
                server.newMessage(in);

                // Send the Client Connection too, in case a message wants to be sent back.
                // Only send it paired with a non-sentinel Communication.
                if (in instanceof Communication && in != Communication.SENTINEL)
                    server.newMessage(client);
            } catch (IOException | InterruptedException e) {
                System.out.println(String.format("!! Unable to read input: %s: %s", e.getClass().getTypeName(), e.getMessage()));
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("!! Unsupported or unknown class passed through as input?");
                return;
            }
        }
    }
}
