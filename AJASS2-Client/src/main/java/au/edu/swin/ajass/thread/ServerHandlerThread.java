package au.edu.swin.ajass.thread;

import au.edu.swin.ajass.controller.ClientController;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.enums.MealType;
import au.edu.swin.ajass.enums.MenuItemType;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.MenuItem;
import au.edu.swin.ajass.models.Order;
import au.edu.swin.ajass.models.Table;

/**
 * Handles received messages from the server that
 * have been placed in the ClientController queue.
 * <p>
 * All messages should start with a Communication, followed by
 * the data necessary for that Communication.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @see au.edu.swin.ajass.enums.Communication
 * @since 0.1
 */
public class ServerHandlerThread implements Runnable {

    private final ClientController client;

    public ServerHandlerThread(ClientController client) {
        this.client = client;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            try {
                // The Thread waits for a message to become available to take from the queue.
                Object take = client.takeMessage();

                try {
                    // We can assume the next message is a Communication enumerated type.
                    Communication command = (Communication) take;
                    System.out.println(String.format("Command from Server: %s", command));

                    switch (command) {
                        // The server is notifying this client that an existing order has been updated.
                        case SERVER_UPDATE_ORDER:
                            // Get the table number and order for adding.
                            OrderState oldState = (OrderState) client.takeMessage();
                            int tableNumber = (Integer) client.takeMessage();
                            int position = (Integer) client.takeMessage();
                            OrderState newState = (OrderState) client.takeMessage();

                            // Update it!
                            client.getMenuController().changeOrderState(oldState, tableNumber, position, newState);

                            // Force UI elements to update.
                            client.getMainView().getOrderStatusView().updateTables();
                            break;
                        // The server is notifying this client that a new order has been created, and it must update.
                        case SERVER_ADD_ORDER:
                            // Get the table number and order for adding.
                            tableNumber = (Integer) client.takeMessage();
                            Order toAdd = (Order) client.takeMessage();

                            // Add it!
                            client.getMenuController().addExistingOrder(client.getMenuController().getTable(tableNumber), OrderState.WAITING, toAdd);

                            // Force UI elements to update.
                            client.getMainView().getOrderStatusView().updateTables();
                            break;
                        // The server has acknowledged this client's request to receive all the current menu items.
                        case SERVER_SEND_ITEMS:
                            // Get the first two values.
                            MenuItemType foodType = (MenuItemType) client.takeMessage();
                            MealType mealType = (MealType) client.takeMessage();

                            // Keep taking objects until we reach sentinel!
                            Object next = client.takeMessage();
                            while (!(next instanceof Communication) && next != Communication.SENTINEL) {
                                if (next instanceof MenuItemType)
                                    // We changed food type!
                                    foodType = (MenuItemType) next;
                                else if (next instanceof MealType)
                                    // We changed meal type!
                                    mealType = (MealType) next;
                                else
                                    // This is a Menu Item, add it!
                                    client.getMenuController().newMenuItem(foodType, mealType, (MenuItem) next);
                                // Next!
                                next = client.takeMessage();
                            }
                            break;
                        // The server has acknowledged this client's request to receive all the current orders.
                        case SERVER_SEND_ORDERS:
                            // Get the current table, first up.
                            Table table = client.getMenuController().getTable((Integer) client.takeMessage());
                            OrderState state = (OrderState) client.takeMessage();

                            // Keep taking objects until we reach sentinel!
                            next = client.takeMessage();
                            while (!(next instanceof Communication) && next != Communication.SENTINEL) {
                                if (next instanceof Integer)
                                    // We changed table number!
                                    table = client.getMenuController().getTable((Integer) next);
                                else if (next instanceof OrderState)
                                    // We changed order state!
                                    state = (OrderState) next;
                                else
                                    // This is a Menu Item, add it!
                                    client.getMenuController().addExistingOrder(table, state, (Order) next);
                                // Next!
                                next = client.takeMessage();
                            }

                            // Force UI elements to update.
                            client.getMainView().getOrderStatusView().updateTables();
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
