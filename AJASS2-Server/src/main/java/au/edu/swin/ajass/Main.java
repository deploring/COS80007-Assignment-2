package au.edu.swin.ajass;

import au.edu.swin.ajass.controllers.ServerController;
import au.edu.swin.ajass.enums.Communication;

import java.util.Scanner;

/**
 * Main class for the Server. Starts the Server Controller
 * and listens for user input afterward.
 *
 * @author Bradley Chick
 * @version 1.0
 * @since 0.1
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("-------------------- HELLO! --------------------");
        System.out.println("> Now attempting to load AJASS2-Server v0.1...");
        // Just simply pass off everything to the Server Controller.
        ServerController controller = new ServerController();

        // Listen for input.
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.toLowerCase().equals("quit")) {
            input = scanner.next();
            if (!input.toLowerCase().equals("quit")) {
                controller.writeToAllClients(Communication.SERVER_DIALOG);
                controller.writeToAllClients(input);
            }
        }

        // Exit after.
        System.exit(0);
    }
}
