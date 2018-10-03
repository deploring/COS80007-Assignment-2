package au.edu.swin.ajass.models;

import au.edu.swin.ajass.controllers.ServerController;
import au.edu.swin.ajass.thread.ClientReadThread;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * This model represents a Client's connection to
 * the server. It can
 */
public class ClientConnection {

    private final Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ClientConnection(ServerController server, Socket client) {
        this.client = client;
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("I/O streams not correctly initialised?");
        }

        // Start a thread that listens for client messages.
        Thread read = new Thread(new ClientReadThread(server, this, input));
        read.setDaemon(true);
        read.start();
    }

    /**
     * Attempts to write a message to the client.
     *
     * @param message The message to write to the client.
     * @return Whether the server was unable to.
     */
    public boolean writeToClient(Object message) {
        try {
            output.writeObject(message);
        } catch (SocketException e) {
            System.out.println(String.format("Connection lost with client (%s): %s: %s", client.getInetAddress(), e.getClass().getTypeName(), e.getMessage()));
            // The socket has failed, return false so this connection can be destroyed.
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            // It could have failed for a non-socket reason. Just print the stack trace and forget about it.
        }
        return true;
    }
}
