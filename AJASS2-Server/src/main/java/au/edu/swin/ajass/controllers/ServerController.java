package au.edu.swin.ajass.controllers;

import au.edu.swin.ajass.config.Configuration;
import au.edu.swin.ajass.models.ClientConnection;
import au.edu.swin.ajass.thread.AcceptThread;
import au.edu.swin.ajass.thread.ClientHandlerThread;

import java.util.*;

/**
 * This Controller handles all interactions between the
 * server and clients. Changes made by clients are reflected
 * on the server-side models, and then are reflected back
 * on all client-side models.
 */
public class ServerController implements ISocketController {

    // Configuration values.
    private Configuration config;

    // Active client connections.
    private List<ClientConnection> clients;

    // Access to table/order models.
    private ServerMenuController menu;

    public ServerController() {
        menu = new ServerMenuController(this);
        config = new Configuration("server.settings", "mysql_user", "mysql_pass", "mysql_host", "mysql_port", "mysql_database", "server_port");
        clients = Collections.synchronizedList(new ArrayList<>());

        // Start a thread to listen for incoming client connections.
        Thread accept = new Thread(new AcceptThread(this, config.getInteger("server_port")));
        accept.setDaemon(false); //TODO: make this daemon again
        accept.start();

        // Start a thread to handle messages sent from clients.
        Thread handler = new Thread(new ClientHandlerThread(this));
        handler.setDaemon(true);
        handler.start();
    }

    /**
     * Sends a message back to all currently connected clients.
     *
     * @param message The message to send to each client.
     */
    public void writeToAllClients(Object message) {
        Set<ClientConnection> invalid = new HashSet<>();
        for (ClientConnection client : clients)
            if (!client.writeToClient(message))
                // If returns false, connection with client is dead. Remove it!
                invalid.add(client);
        clients.removeAll(invalid);
    }

    /**
     * Registers a new accepted client connection in the list.
     *
     * @param newConnection A new client connection!
     */
    public void acceptConnection(ClientConnection newConnection) {
        clients.add(newConnection);
    }

    /**
     * @return An instance of the Menu Controller.
     */
    public ServerMenuController getMenuController() {
        return menu;
    }
}
