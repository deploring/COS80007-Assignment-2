package au.edu.swin.ajass.controller;

import au.edu.swin.ajass.Utilities;
import au.edu.swin.ajass.enums.Communication;
import au.edu.swin.ajass.enums.MealType;
import au.edu.swin.ajass.enums.MenuItemType;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.MenuItem;
import au.edu.swin.ajass.models.Order;
import au.edu.swin.ajass.models.OrderLocation;
import au.edu.swin.ajass.models.Table;
import au.edu.swin.ajass.thread.ServerHandlerThread;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * This controller handles all data manipulation handed
 * down to the models.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public final class ClientMenuController {

    // Reference to the Client Controller.
    private ClientController client;

    // List of mapped menu items received from the server.
    private HashMap<MenuItemType, HashMap<MealType, LinkedList<MenuItem>>> menuItems;

    // List of table numbers mapped to tables.
    private HashMap<Integer, Table> tables;

    ClientMenuController(ClientController client) {
        this.client = client;
        tables = new HashMap<>();
        menuItems = new HashMap<>();
        createTables();
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
     * Adds a new order to a table. Orders are reconstructed from
     * the Server Handler Thread. They are always valid.
     *
     * @param table The table to add the order to.
     * @param state The order's current state.
     * @param order The order, passed through from the server.
     * @see ServerHandlerThread#run()
     */
    public void addExistingOrder(Table table, OrderState state, Order order) {
        table.addOrder(state, order);
    }

    /**
     * Creates a Server request to make an order, for which
     * it will be distributed to every client afterwards.
     *
     * @param tableNumber  The table number this order belongs to.
     * @param customerName The name of the customer who made the order.
     * @param food         The food preference for the order.
     * @param beverage     The beverage preference for the order.
     * @see #addExistingOrder(Table, OrderState, Order)
     */
    public void newOrder(int tableNumber, String customerName, MenuItem food, MenuItem beverage) {
        client.writeToServer(Communication.CLIENT_CREATE_ORDER);
        client.writeToServer(tableNumber);
        client.writeToServer(customerName);
        // Instead of passing null through, pass SENTINEL instead.
        client.writeToServer(food == null ? Communication.SENTINEL : food);
        client.writeToServer(beverage == null ? Communication.SENTINEL : beverage);
        // No sentinel is required as we are supplying a finite amount of items!
    }

    /**
     * Creates a Server request to transition an existing order
     * into a different order state, which is reflected across each client.
     *
     * @param location The table and linked list position of an order.
     * @param oldState The order's current state.
     * @param newState The order's desired new state.
     * @see OrderLocation
     */
    public void updateOrderState(OrderLocation location, OrderState oldState, OrderState newState) {
        client.writeToServer(Communication.CLIENT_UPDATE_ORDER);
        client.writeToServer(oldState);
        client.writeToServer(location.getTable());
        client.writeToServer(location.getPosition());
        client.writeToServer(newState);
        // No sentinel is required as we are supplying a finite amount of items!
    }

    /**
     * This method actually changes the order's state. It should
     * only be changed by the Server Handler Thread when it is
     * reflecting changes across all clients.
     *
     * @param oldState    The order's current state.
     * @param tableNumber The number of the table this order belongs to.
     * @param position    The position of this order in its linked list.
     * @param newState    The order's desired new state.
     * @see OrderLocation
     */
    public void changeOrderState(OrderState oldState, int tableNumber, int position, OrderState newState) {
        getTable(tableNumber).swapOrder(oldState, position, newState);
    }

    /**
     * @param tableNumber Table number.
     * @return Instance of Table, based on table number.
     */
    public Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    /**
     * @return A stream over a collection of all the tables.
     */
    public Stream<Table> getTables() {
        return tables.values().stream();
    }

    /**
     * @param state A specific order state.
     * @return Number of orders across all tables for a given state.
     */
    public int getNumberOfOrders(OrderState state) {
        int result = 0;
        for (Table table : tables.values())
            result += table.getNumberOfOrders(state);
        return result;
    }

    /* Menu Item Stuff */

    /**
     * Returns a list of Menu Items of a specific food and meal type.
     *
     * @param foodType The food type we want.
     * @param mealType The meal type we want.
     * @return The list of relevant menu items.
     */
    public LinkedList<MenuItem> getMenuItem(MenuItemType foodType, MealType mealType) {
        return menuItems.get(foodType).get(mealType);
    }

    /**
     * Maps a new menu item to the correct list.
     * Map Menu Item Type -> Meal Type -> Specific List of Menu Items
     *
     * @param foodType The menu item's food type. (food or beverage).
     * @param mealType The menu item's meal type. (breakfast, lunch, or dinner).
     * @param toPlace  The menu item to place in the list.
     */
    public void newMenuItem(MenuItemType foodType, MealType mealType, MenuItem toPlace) {
        // If the map inside the Food Type map doesn't exist, create it first.
        if (!menuItems.containsKey(foodType))
            menuItems.put(foodType, new HashMap<>());

        // If the list inside the Meal Type map inside the Food Type map doesn't exist, create it first.
        if (!menuItems.get(foodType).containsKey(mealType))
            menuItems.get(foodType).put(mealType, new LinkedList<>());

        // Add to the List that contains Menu Items of a specific food type and meal type.
        menuItems.get(foodType).get(mealType).add(toPlace);
    }

    /**
     * @param orders An array of orders that have been billed.
     * @return A receipt for the orders billed.
     */
    public String generateReceipt(OrderLocation[] orders) {
        StringBuilder result = new StringBuilder();
        result.append("Receipt for ").append(orders.length).append(" Order(s):\n\n");

        AtomicInteger currentOrder = new AtomicInteger(0);
        AtomicInteger totalPrice = new AtomicInteger(0);
        Arrays.stream(orders).forEach(ol -> {
            Order o = getTable(ol.getTable()).getOrder(OrderState.SERVED, ol.getPosition());

            int orderIndex = currentOrder.addAndGet(1);
            double orderPrice = 0, orderEnergy = 0, orderProtein = 0, orderCarbs = 0, orderFat = 0, orderFiber = 0;

            result.append("Order #").append(orderIndex).append(":\n");

            if (o.getFood() != null) {
                result.append(o.getFood().getItemName()).append(" ($").append((int) o.getFood().getPrice()).append(".00)\n");
                totalPrice.addAndGet((int) o.getFood().getPrice());
                orderPrice += o.getFood().getPrice();
                orderEnergy += o.getFood().getEnergy();
                orderProtein += o.getFood().getProtein();
                orderCarbs += o.getFood().getCarbs();
                orderFat += o.getFood().getFat();
                orderFiber += o.getFood().getFibre();
            }

            if (o.getBeverage() != null) {
                result.append(o.getBeverage().getItemName()).append(" ($").append((int) o.getBeverage().getPrice()).append(".00)\n");
                totalPrice.addAndGet((int) o.getBeverage().getPrice());
                orderPrice += o.getBeverage().getPrice();
                orderEnergy += o.getBeverage().getEnergy();
                orderProtein += o.getBeverage().getProtein();
                orderCarbs += o.getBeverage().getCarbs();
                orderFat += o.getBeverage().getFat();
                orderFiber += o.getBeverage().getFibre();
            }

            result.append("Total Nutritional Information for Order:\n");
            result.append("Energy: ").append(orderEnergy).append("kJ, Protein: ").append(orderProtein).append("g, Carbs: ").append(orderCarbs).append("g, Fat: ").append(orderFat).append("g, Fiber: ").append(orderFiber).append("g\n");
            result.append("Order Total: $").append(((int) orderPrice)).append(".00\n\n");
        });

        result.append("Total for All ").append(orders.length).append(" Orders: $").append(totalPrice.get()).append(".00\nThank You!");
        return result.toString();
    }
}
