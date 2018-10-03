package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.controllers.ServerController;
import au.edu.swin.ajass.models.ClientConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This thread listens for, and establishes connections with
 * clients, it does not perform any data exchange.
 */
public class AcceptThread implements Runnable {

    private final ServerController server;
    private ServerSocket socket;

    public AcceptThread(ServerController server, int serverPort) {
        this.server = server;
        try {
            socket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                // Accept any new incoming client connections.
                Socket clientSocket = socket.accept();
                ClientConnection newConnection = new ClientConnection(server, clientSocket);
                server.acceptConnection(newConnection);

                // Tell the console!
                System.out.println(String.format("New incoming connection from %s:%s", clientSocket.getInetAddress(), clientSocket.getPort()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Nothing should happen, but just in case it does...
        }
    }
}