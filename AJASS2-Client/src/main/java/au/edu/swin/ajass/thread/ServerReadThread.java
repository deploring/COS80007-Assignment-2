package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.controller.ClientController;
import au.edu.swin.ajass.models.ServerConnection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

/**
 * This Thread listens for incoming messages from the
 * server and stores them in the queue for processing.
 */
public class ServerReadThread implements Runnable {

    private final ServerConnection server;
    private final ClientController client;
    private final ObjectInputStream input;

    public ServerReadThread(ServerConnection server, ClientController client, ObjectInputStream input) {
        this.server = server;
        this.client = client;
        this.input = input;
    }

    public void run() {
        // The thread should stop running once the connection becomes invalid.
        while (client.isValidConnection(server)) {
            try {
                // Read in a new message whenever one comes along.
                client.newMessage(input.readObject());
            } catch (SocketException | EOFException e) {
                // Connection was lost or timed out.
                System.out.println(String.format("Unable to read from server: %s: %s", e.getClass().getTypeName(), e.getMessage()));
                client.lostConnection();
            } catch (IOException | InterruptedException e) {
                // Invalid data sent through with request.
                System.out.println(String.format("Unable to read input: %s: %s", e.getClass().getTypeName(), e.getMessage()));
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("Unsupported or unknown class passed through as input?");
                return;
            }
        }
    }
}
