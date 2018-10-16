package au.edu.swin.ajass.controllers;

import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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