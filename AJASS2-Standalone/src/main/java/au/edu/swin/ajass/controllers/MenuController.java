package au.edu.swin.ajass.controllers;

import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.*;

import java.util.HashMap;
import java.util.List;
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
public final class MenuController {

    // Static constant(s)
    public final static int NUMBER_OF_TABLES = 8;

    private HashMap<Integer, Table> tables;
    private MenuItems itemCache;

    public MenuController() {
        tables = new HashMap<>();
        itemCache = new MenuItems();
        createTables();
    }

    /**
     * Populates the tables map with instances of Table.
     */
    private void createTables() {
        for (int i = 1; i <= NUMBER_OF_TABLES; i++) {
            tables.put(i, new Table());
        }
    }

    /**
     * Creates a new individual order for a table. The order must be
     * constructed specially if only one food item has been
     * ordered and not two.
     *
     * @param table        The table the customer ordered from.
     * @param customerName The customer's name.
     * @param food         The customer's preference of food.
     * @param beverage     The customer's preference of beverage.
     * @see IndividualOrder
     */
    public void createIndividualOrder(Table table, String customerName, MenuItem food, MenuItem beverage) {
        if (food != null && beverage != null)
            // Proceed as normal and create the order.
            table.addOrder(OrderState.WAITING, new IndividualOrder(customerName, food, beverage));
        else if (food != null)
            // The order only contains food, no beverage.
            table.addOrder(OrderState.WAITING, new IndividualOrder(customerName, Order.FOOD_ONLY, food));
        else if (beverage != null)
            // The order only contains a beverage, no food.
            table.addOrder(OrderState.WAITING, new IndividualOrder(customerName, Order.BEVERAGE_ONLY, beverage));
        else
            throw new IllegalArgumentException("Food and beverage cannot be null");
    }
    /**
     * Creates a new group order for a table.
     *
     * @param table        The table the customer ordered from.
     * @param groupName    The group's name.
     * @param groupSize    The number of people in the group.
     * @param items        The group's order items.
     * @see GroupOrder
     */
    public void createGroupOrder(Table table, String groupName, int groupSize , List<MenuItem[]> items){
        table.addOrder(OrderState.WAITING, new GroupOrder(groupSize, groupName, items));
    }

    /**
     * Prepares an order and transitions it to the SERVED state.
     * @param location The table and linked list position of an order.
     * @see OrderLocation
     */
    public void prepareOrder(OrderLocation location) {
        getTable(location.getTable()).swapOrder(OrderState.WAITING, location.getPosition(), OrderState.SERVED);
    }

    /**
     * Prepares an order and transitions it to the BILLED state.
     * @param location The table and linked list position of an order.
     * @see OrderLocation
     */
    public void billOrder(OrderLocation location) {
        getTable(location.getTable()).swapOrder(OrderState.SERVED, location.getPosition(), OrderState.BILLED);
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
     * @return Instance of menu item cache object.
     * Used to retrieve menu items.
     */
    public MenuItems getMenuItems() {
        return itemCache;
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
}