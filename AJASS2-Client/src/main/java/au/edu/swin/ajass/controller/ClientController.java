package au.edu.swin.ajass.controller;

import au.edu.swin.ajass.config.Configuration;
import au.edu.swin.ajass.controllers.ISocketController;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.models.ServerConnection;
import au.edu.swin.ajass.thread.ServerHandlerThread;
import au.edu.swin.ajass.views.MainView;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Heartist on 3/10/2018.
 */
public class ClientController implements ISocketController {

    // Reference back to the UI.
    private MainView main;

    // Configuration values.
    private Configuration config;

    // Server connection.
    private ServerConnection server;

    // Access to table/order models.
    private ClientMenuController menu;

    public ClientController(MainView main) throws IllegalStateException {
        this.main = main;

        // Load in settings.
        config = new Configuration("client.settings", "server_hostname", "server_port");

        try {
            server = new ServerConnection(this, new Socket(config.getString("server_hostname"), config.getInteger("server_port")));
            server.postStartReaderThread();
        } catch (IOException e) {
            e.printStackTrace();
            // They couldn't connect. Throw an illegal state so the program does not continue loading up.

            JOptionPane.showMessageDialog(null, String.format("Unable to connect to '%s:%s'.\n" +
                            "Please double-check server settings in 'client.settings' file.\n" +
                            "This file is created in the same directory as your JAR file.",
                    config.getString("server_hostname"), config.getString("server_port")),
                    "Unable to Connect", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException();
        }

        // Create menu controller after we connect to the server.
        menu = new ClientMenuController(this);

        // Tell the server to send all of the Menu Items.
        writeToServer(Communication.CLIENT_WANT_ITEMS);

        // Tell the server to send all of the existing Orders.
        writeToServer(Communication.CLIENT_WANT_ORDERS);

        // Start a thread to handle messages sent from the server.
        Thread handler = new Thread(new ServerHandlerThread(this));
        handler.setDaemon(true);
        handler.start();
    }

    /**
     * @return True if this ServerConnection is the currently monitored one.
     */
    public boolean isValidConnection(ServerConnection toCheck) {
        return server == toCheck;
    }

    /**
     * Sends a message back to the server.
     *
     * @param message The message to send to the server.
     */
    public void writeToServer(Object message) {
        if (!server.writeToServer(message))
            lostConnection();
    }

    /**
     * This should be called when the connection to the
     * server is, or is suspected to be invalid or dead.
     */
    public void lostConnection() {
        // Nothing will happen unless the connection still exists.
        if (server != null) {
            server = null;

            // Disable the UI and reconnect as quickly as possible.
            main.connectionLost();
            System.out.println("Now attempting to re-connect to server...");
            reconnect();
        }
    }

    /**
     * Attempts to re-establish a connection with the server.
     * If a connection cannot be established, it is re-attempted
     * until a connection is established.
     */
    private void reconnect() {
        // Set the connection as invalid so reconnections cannot be attempted multiple times.
        try {
            // Re-create a new ServerConnection. This will automatically set CONNECTION_STATE back to VALID if successful.
            server = new ServerConnection(this, new Socket(config.getString("server_hostname"), config.getInteger("server_port")));
            server.postStartReaderThread();

            // Since we have re-connected, get the server to send through the current orders again.
            writeToServer(Communication.CLIENT_WANT_ORDERS);

            // Allow the UI to be used again.
            main.connectionReEstablished();
        } catch (IOException e) {
            // Continually attempt to re-connect in the background.
            Thread reconnectThread = new Thread(() -> {
                try {
                    // Do it every half a second. It's not important.
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                reconnect();
            });
            reconnectThread.setDaemon(true);
            reconnectThread.start();
        }
    }

    /**
     * @return An instance of the UI.
     */
    public MainView getMainView() {
        return main;
    }

    /**
     * @return An instance of the Menu Controller.
     */
    public ClientMenuController getMenuController() {
        return menu;
    }
}
