package au.edu.swin.ajass.controllers;

import au.edu.swin.ajass.Utilities;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.*;

import java.util.HashMap;

/**
 * This controller handles all data manipulation handed
 * down to the models.
 * <p>
 * <em>This only reflects the server-side state.</em>
 * <p>
 * Messages should be send to each client notifying them
 * of any changes that occur to the orders or tables.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @see ServerController
 * @since 0.1
 */
public final class ServerMenuController {

    // Reference to the Server Controller.
    private ServerController server;

    // List of table numbers mapped to tables.
    private HashMap<Integer, Table> tables;

    // Menu item cache.
    private MenuItems itemCache;

    ServerMenuController(ServerController server) {
        System.out.println("> Creating table models and collections...");
        this.server = server;
        tables = new HashMap<>();
        itemCache = new MenuItems();
        createTables();
        System.out.println("...success!");
    }

    /**
     * Populates the tables map with instances of Table.
     */
    private void createTables() {
        for (int i = 1; i <= Utilities.NUMBER_OF_TABLES; i++) {
            tables.put(i, new Table());
        }
    }

    /**
     * Creates a new order for a table. The order must be
     * constructed specially if only one food item has been
     * ordered and not two.
     *
     * @param tableNumber  The number of the table the order was placed at.
     * @param customerName The customer's name.
     * @param food         The customer's preference of food.
     * @param beverage     The customer's preference of beverage.
     * @see Order
     */
    public void createOrder(int tableNumber, String customerName, MenuItem food, MenuItem beverage) {
        // Get the table first.
        Table table = getTable(tableNumber);

        if (food != null && beverage != null)
            // Proceed as normal and create the order.
            distributeOrder(tableNumber, table.addOrder(OrderState.WAITING, new Order(customerName, food, beverage)));
        else if (food != null)
            // The order only contains food, no beverage.
            distributeOrder(tableNumber, table.addOrder(OrderState.WAITING, new Order(customerName, Order.FOOD_ONLY, food)));
        else if (beverage != null)
            // The order only contains a beverage, no food.
            distributeOrder(tableNumber, table.addOrder(OrderState.WAITING, new Order(customerName, Order.BEVERAGE_ONLY, beverage)));
        else
            throw new IllegalArgumentException("Food and beverage cannot be null");
    }

    /**
     * Distributes the order amongst all the active Clients.
     *
     * @param tableNumber The number of the table the order was placed at.
     * @param toDisribute The actual order.
     */
    private void distributeOrder(int tableNumber, Order toDisribute) {
        server.writeToAllClients(Communication.SERVER_ADD_ORDER);
        server.writeToAllClients(tableNumber);
        server.writeToAllClients(toDisribute);
        // No sentinel is required as we are supplying a finite amount of items!
    }

    /**
     * Creates a Server request to transition an existing order
     * into a different order state, which is reflected across each client.
     *
     * @param oldState    The order's current state.
     * @param tableNumber The number of the table this order belongs to.
     * @param position    The position of this order in its linked list.
     * @param newState    The order's desired new state.
     * @see OrderLocation
     */
    public void changeOrderState(OrderState oldState, int tableNumber, int position, OrderState newState) {
        getTable(tableNumber).swapOrder(oldState, position, newState);

        // Distribute the changes to all the clients afterward.
        server.writeToAllClients(Communication.SERVER_UPDATE_ORDER);
        server.writeToAllClients(oldState);
        server.writeToAllClients(tableNumber);
        server.writeToAllClients(position);
        server.writeToAllClients(newState);
        // No sentinel is required as we are supplying a finite amount of items!
    }

    /**
     * @param tableNumber Table number.
     * @return Instance of Table, based on table number.
     */
    public Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    /**
     * @return Instance of menu item cache object.
     * Used to retrieve menu items.
     */
    public MenuItems getMenuItems() {
        return itemCache;
    }
}
