package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.controller.ClientController;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.models.ServerConnection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;

/**
 * This Thread periodically sends a HEARTBEAT communication
 * to the server to keep the connection from dying from timeout.
 *
 * @see au.edu.swin.ajass.enums.Communication#CLIENT_HEARTBEAT
 */
public class ClientHeartbeatThread implements Runnable {

    private final ClientController client;
    private final ObjectOutputStream output;

    public ClientHeartbeatThread(ClientController client, ObjectOutputStream output) {
        this.client = client;
        this.output = output;
    }

    public void run() {
        // The thread should stop running once the connection becomes invalid.
        while (ServerConnection.CONNECTION_STATE == ServerConnection.VALID) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ignored) {
            }
            try {
                output.writeObject(Communication.CLIENT_HEARTBEAT);
            } catch (SocketException | EOFException e) {
                // Connection was lost or timed out.
                System.out.println(String.format("Unable to heartbeat to server: %s: %s", e.getClass().getTypeName(), e.getMessage()));
                client.lostConnection();
            } catch (IOException ignored) {
                // Ignore any I/O exception. This will be picked up by the ServerReadThread instead.
            }
        }
    }
}
