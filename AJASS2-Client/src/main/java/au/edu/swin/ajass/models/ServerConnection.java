package au.edu.swin.ajass.models;

import au.edu.swin.ajass.controller.ClientController;
import au.edu.swin.ajass.thread.ClientHeartbeatThread;
import au.edu.swin.ajass.thread.ServerReadThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * This model represents a Client's connection to the server.
 * It listens for incoming messages, as well as writing messages
 * to the server when needed.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public class ServerConnection {

    private final ClientController client;
    private final Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ServerConnection(ClientController client, Socket server) {
        this.client = client;
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

        // Start a thread that sends periodic heartbeats to the server.
        Thread beat = new Thread(new ClientHeartbeatThread(this, client));
        beat.setDaemon(true);
        beat.start();
    }

    /**
     * The thread should start after the ServerConnection object is initialised.
     * If it does not, the consequences can lead to the read thread being terminated
     * early. This means that the client will not listen to incoming messages.
     */
    public void postStartReaderThread(){
        // Start a thread that listens for server messages. Lowest priority.
        Thread read = new Thread(new ServerReadThread(this, client, input));
        read.setPriority(1);
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
            if (client.isValidConnection(this))
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
