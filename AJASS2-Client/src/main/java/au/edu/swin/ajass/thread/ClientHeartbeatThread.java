package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.controller.ClientController;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.models.ServerConnection;

/**
 * This Thread periodically sends a HEARTBEAT communication
 * to the server to keep the connection from dying from timeout.
 *
 * @see au.edu.swin.ajass.enums.Communication#CLIENT_HEARTBEAT
 */
public class ClientHeartbeatThread implements Runnable {

    private final ServerConnection server;
    private final ClientController client;

    public ClientHeartbeatThread(ServerConnection server, ClientController client) {
        this.server = server;
        this.client = client;
    }

    public void run() {
        // The thread should stop running once the connection becomes invalid.
        while (client.isValidConnection(server)) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ignored) {
            }
            // Always double-check after the sleep, too.
            if (client.isValidConnection(server))
                client.writeToServer(Communication.CLIENT_HEARTBEAT);
        }
    }
}
