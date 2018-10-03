package au.edu.swin.ajass.models;

import au.edu.swin.ajass.controller.ClientController;
import au.edu.swin.ajass.thread.ServerReadThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * This model represents a Client's connection to
 * the server. It can
 */
public class ServerConnection {

    private final Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ServerConnection(ClientController client, Socket server) {
        this.server = server;
        try {
            output = new ObjectOutputStream(server.getOutputStream());
            input = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("I/O streams not correctly initialised?");
        }

        // Tell the console!
        System.out.println(String.format("Connection established with server (%s:%s)", server.getInetAddress(), server.getPort()));

        // Start a thread that listens for server messages.
        Thread read = new Thread(new ServerReadThread(client, input));
        read.setDaemon(true);
        read.start();
    }

    /**
     * Attempts to write a message to the client.
     *
     * @param message The message to write to the client.
     * @return Whether the server was unable to.
     */
    public boolean writeToServer(Object message) {
        try {
            output.writeObject(message);
        } catch (SocketException e) {
            System.out.println(String.format("Connection lost with server (%s): %s: %s", server.getInetAddress(), e.getClass().getTypeName(), e.getMessage()));
            // The socket has failed, return false so this connection can be destroyed.
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            // It could have failed for a non-socket reason. Just print the stack trace and forget about it.
        }
        return true;
    }
}
