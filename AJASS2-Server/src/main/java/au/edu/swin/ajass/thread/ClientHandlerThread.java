package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.Utilities;
import au.edu.swin.ajass.controllers.ServerController;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.enums.MealType;
import au.edu.swin.ajass.enums.MenuItemType;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.ClientConnection;
import au.edu.swin.ajass.models.MenuItem;
import au.edu.swin.ajass.models.Order;
import au.edu.swin.ajass.models.Table;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Handles received messages from clients that
 * have been placed in the ServerController queue.
 * <p>
 * All messages should start with a Communication, followed by
 * the data necessary for that Communication.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @see au.edu.swin.ajass.enums.Communication
 * @since 0.1
 */
@SuppressWarnings("UnusedAssignment")
public class ClientHandlerThread implements Runnable {

    private final ServerController server;

    public ClientHandlerThread(ServerController server) {
        this.server = server;
    }

    public void run() {
        while (true) {
            try {
                // The Thread waits for a message to become available to take from the queue.
                Object take = server.takeMessage();

                // Keep an instance of the Client Connection in case messages want to be returned back to the single Client.
                ClientConnection client = (ClientConnection) server.takeMessage();

                try {
                    // We can assume the next message is a Communication enumerated type.
                    Communication command = (Communication) take;
                    if (command != Communication.CLIENT_HEARTBEAT)
                        System.out.println(String.format("Command from Client: %s", command));

                    switch (command) {
                        // The client has updated an order.
                        case CLIENT_UPDATE_ORDER:
                            // Let's retrieve the values passed through with the command.
                            OrderState oldState = (OrderState) server.takeMessage();
                            Integer tableNumber = (Integer) server.takeMessage();
                            Integer position = (Integer) server.takeMessage();
                            OrderState newState = (OrderState) server.takeMessage();

                            // Update that order!
                            server.getMenuController().changeOrderState(oldState, tableNumber, position, newState);
                            break;
                        // The client has created a new order.
                        case CLIENT_CREATE_ORDER:
                            // Let's retrieve the values passed through with the command.
                            tableNumber = (Integer) server.takeMessage();
                            String customerName = (String) server.takeMessage();

                            // Check if food was cast to SENTINEL. If so, set to null (no selection).
                            MenuItem food;
                            Object chkFood = server.takeMessage();
                            if (chkFood == Communication.SENTINEL)
                                food = null;
                            else food = (MenuItem) chkFood;

                            // Check if food was cast to SENTINEL. If so, set to null (no selection).
                            MenuItem beverage;
                            Object chkBev = server.takeMessage();
                            if (chkBev == Communication.SENTINEL)
                                beverage = null;
                            else beverage = (MenuItem) chkBev;

                            // Create that order!
                            server.getMenuController().createOrder(tableNumber, customerName, food, beverage);
                            break;
                        // The client has requested that the server send back all of the Menu Items, in strict order.
                        case CLIENT_WANT_ITEMS:
                            // Firstly, declare that we are sending a heap of Menu Items.
                            client.writeToClient(Communication.SERVER_SEND_ITEMS);

                            // Iterate through the first entry set.
                            for (Map.Entry<MenuItemType, HashMap<MealType, LinkedList<MenuItem>>> entry : server.getMenuController().getMenuItems().entrySet()) {
                                // Declare that we are now sending a specific food type.
                                client.writeToClient(entry.getKey());

                                // Iterate through the second entry set.
                                for (Map.Entry<MealType, LinkedList<MenuItem>> entry2 : entry.getValue().entrySet()) {
                                    // Declare that we are now sending a specific meal type.
                                    client.writeToClient(entry2.getKey());

                                    // Send through the menu item!
                                    for (MenuItem toSend : entry2.getValue())
                                        client.writeToClient(toSend);
                                }
                            }

                            // Send a sentinel communication to mark the end of the sending.
                            client.writeToClient(Communication.SENTINEL);
                            break;
                        // The client has requested that the server send back all of the current Orders in strict order.
                        case CLIENT_WANT_ORDERS:
                            // Firstly, declare that we are sending a heap of Orders.
                            client.writeToClient(Communication.SERVER_SEND_ORDERS);

                            // Iterate through each table in order.
                            for (int i = 1; i <= Utilities.NUMBER_OF_TABLES; i++) {
                                Table table = server.getMenuController().getTable(i);

                                // Declare what table we are currently on.
                                client.writeToClient(i);

                                // Declare we are sending through WAITING orders first.
                                client.writeToClient(OrderState.WAITING);

                                // Send through each order!
                                for (Iterator<Order> iter = table.getOrders(OrderState.WAITING); iter.hasNext(); ) {
                                    Order order = iter.next();
                                    client.writeToClient(order);
                                }

                                // Declare we are sending through SERVED orders second.
                                client.writeToClient(OrderState.SERVED);

                                // Send through each order!
                                for (Iterator<Order> iter = table.getOrders(OrderState.SERVED); iter.hasNext(); ) {
                                    Order order = iter.next();
                                    client.writeToClient(order);
                                }

                                // Declare we are sending through BILLED orders second.
                                client.writeToClient(OrderState.BILLED);

                                // Send through each order!
                                for (Iterator<Order> iter = table.getOrders(OrderState.BILLED); iter.hasNext(); ) {
                                    Order order = iter.next();
                                    client.writeToClient(order);
                                }
                            }

                            // Send a sentinel communication to mark the end of the sending.
                            client.writeToClient(Communication.SENTINEL);
                            break;
                        case CLIENT_HEARTBEAT:
                            // We have received a heartbeat! This will stop the connection from timing out...
                            break;
                        default:
                            // The client has sent an invalid Communication.
                            System.out.println(String.format("WARNING: Communication '%s' should not be sent by clients.", command));
                            System.out.println("Stability of server is no longer guaranteed if data was also passed with the command!!!");
                            break;
                    }
                } catch (ClassCastException ex) {
                    ex.printStackTrace();
                    // The client has sent an unknown Communication.
                    System.out.println(String.format("DANGER: '%s' could not be treated as a Communication.", take.toString()));
                    System.out.println("Stability of server is no longer guaranteed if data was also passed with the command!!!");
                    System.out.println(String.format("Technical details: %s: %s", ex.getClass().getTypeName(), ex.getMessage()));
                }
            } catch (InterruptedException e) {
                System.out.println(String.format("Unable to process input: %s: %s", e.getClass().getTypeName(), e.getMessage()));
            }
        }
    }
}
