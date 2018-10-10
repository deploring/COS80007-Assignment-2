package au.edu.swin.ajass.controllers;

import au.edu.swin.ajass.config.Configuration;
import au.edu.swin.ajass.models.ClientConnection;
import au.edu.swin.ajass.sql.Database;
import au.edu.swin.ajass.thread.AcceptThread;
import au.edu.swin.ajass.thread.ClientHandlerThread;

import java.sql.SQLException;
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

    // Access to database.
    private Database database;

    // Active client connections.
    private List<ClientConnection> clients;

    // Access to table/order models.
    private ServerMenuController menu;

    public ServerController() {
        menu = new ServerMenuController(this);
        clients = Collections.synchronizedList(new ArrayList<>());

        // Read configuration for server and database configuration settings.
        System.out.println("> Reading configuration...");
        try {
            config = new Configuration("server.settings", "mysql_user", "mysql_pass", "mysql_host", "mysql_port", "mysql_database", "server_port");
        } catch (IllegalArgumentException ex) {
            // Configuration was not loaded. Do not continue with execution.
            System.out.println(String.format("!! Unable to load configuration: %s", ex.getMessage()));
            System.exit(0);
            return;
        }
        System.out.println("...success!");

        // Attempt to connect to database and load stuff.
        System.out.println("> Connecting to database...");
        try {
            database = new Database(config.getString("mysql_user"), config.getString("mysql_pass"), config.getString("mysql_host"), config.getString("mysql_port"), config.getString("mysql_database"));

            System.out.println(">> Checking tables...");
            database.createTables();
       } catch (IllegalStateException e) {
            // Database was not loaded. Do not continue with execution.
            System.out.println(String.format("!! Unable to connect to database: %s", e.getMessage()));
            System.exit(0);
            return;
        } catch (SQLException e) {
            // Database tables were not created.
            System.out.println(String.format("!! Unable to create database tables: %s", e.getMessage()));
            System.exit(0);
            return;
        }
        System.out.println("...success!");

        // Start a thread to listen for incoming client connections.
        Thread accept = new Thread(new AcceptThread(this, config.getInteger("server_port")));
        accept.setDaemon(false); // This thread is not daemon since it continuously listens for ports.
        accept.start();

        // Start a thread to handle messages sent from clients.
        System.out.println("> Starting client handler thread...");
        Thread handler = new Thread(new ClientHandlerThread(this));
        handler.setDaemon(true);
        handler.start();
        System.out.println("...success!");

        // More console spam.
        System.out.println("------------- SERVER IS GOOD TO GO -------------");
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
