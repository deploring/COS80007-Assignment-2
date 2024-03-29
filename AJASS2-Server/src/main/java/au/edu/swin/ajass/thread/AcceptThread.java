package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.controllers.ServerController;
import au.edu.swin.ajass.models.ClientConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This thread listens for, and establishes connections with
 * clients, it does not perform any data exchange. Connections
 * are converted into ClientConnections, which then run their
 * own worker threads to handling receiving data.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public class AcceptThread implements Runnable {

    private final ServerController server;
    private ServerSocket socket;

    public AcceptThread(ServerController server, int serverPort) {
        System.out.println("> Opening port to listen to incoming client connections...");
        this.server = server;
        try {
            socket = new ServerSocket(serverPort);
            System.out.println("...success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        try {
            while (true) {
                // Accept any new incoming client connections.
                Socket clientSocket = socket.accept();
                ClientConnection newConnection = new ClientConnection(server, clientSocket);
                server.acceptConnection(newConnection);

                // Tell the console!
                System.out.println(String.format("> New incoming connection from %s:%s", clientSocket.getInetAddress(), clientSocket.getPort()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Nothing should happen, but just in case it does...
        }
    }
}