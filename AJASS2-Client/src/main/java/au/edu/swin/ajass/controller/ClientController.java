package au.edu.swin.ajass.controller;

import au.edu.swin.ajass.config.Configuration;
import au.edu.swin.ajass.controllers.ISocketController;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.models.ServerConnection;
import au.edu.swin.ajass.thread.ServerHandlerThread;
import au.edu.swin.ajass.views.MainView;

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

    public ClientController(MainView main) {
        this.main = main;

        // Load in settings.
        config = new Configuration("client.settings", "server_hostname", "server_port");

        try {
            server = new ServerConnection(this, new Socket(config.getString("server_hostname"), config.getInteger("server_port")));
        } catch (IOException e) {
            e.printStackTrace();
            // This shouldn't happen, so just print stack trace.
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
     * Sends a message back to the server.
     *
     * @param message The message to send to the server.
     */
    public void writeToServer(Object message) {
        server.writeToServer(message);
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
