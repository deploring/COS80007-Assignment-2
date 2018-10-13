package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.OrderState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Table is a model that represents, well, a table at
 * a restaurant. Multiple orders can be made to one table.
 * This model exists to organise and store orders.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @see Order
 * @since 0.1
 */
public final class Table {

    private HashMap<OrderState, LinkedList<Order>> orders;

    public Table() {
        orders = new HashMap<>();

        // Pre-fill hashmap with empty lists.
        for (OrderState state : OrderState.values())
            orders.put(state, new LinkedList<>());
    }

    /**
     * Clears orders from the table.
     */
    public void clear() {
        for (LinkedList<Order> orders : this.orders.values())
            orders.clear();
    }

    /**
     * @return Total amount of orders for this table.
     */
    public int getTotalNumberOfOrders() {
        return getNumberOfOrders(OrderState.SERVED) + getNumberOfOrders(OrderState.WAITING);
    }

    /**
     * @param state A specific order state.
     * @return Amount of orders in that order state for this table.
     */
    public int getNumberOfOrders(OrderState state) {
        return orders.get(state).size();
    }

    /**
     * @param state    A specific order state.
     * @param position A specific position in the linked list.
     * @return An order of a specific order state and position in a list.
     */
    public Order getOrder(OrderState state, int position) {
        return orders.get(state).get(position);
    }

    /**
     * @param state A specific order state.
     * @return An iterator over a list of orders of a specific order state.
     */
    public Iterator<Order> getOrders(OrderState state) {
        return orders.get(state).iterator();
    }

    /**
     * Populate the map with an order of a specific state.
     *
     * @param state The order's state.
     * @param order The actual order.
     */
    public Order addOrder(OrderState state, Order order) {
        // Don't allow the same order instance to be added to the same list twice.
        if (orders.get(state).contains(order))
            throw new IllegalStateException("Added same order instance to list twice?");

        orders.get(state).add(order);

        // Return the order too, so we can distribute it!
        return order;
    }

    /**
     * @param state    A specific order state.
     * @param position A specific position in the linked list.
     * @return An order of a specific order state and position in a list. (is removed from list)
     */
    private Order removeOrder(OrderState state, int position) {
        return orders.get(state).remove(position);
    }

    /**
     * A conjunction of the removeOrder(), and addOrder() methods.
     * Allows an existing order in a specific order state to be
     * transferred to a different linked list of a different state.
     *
     * @param oldState    The order's current order state.
     * @param oldPosition The order's current position in the linked list.
     * @param newState    The desired new order state.
     */
    public void swapOrder(OrderState oldState, int oldPosition, OrderState newState) {
        if (oldState == newState) throw new IllegalArgumentException("Old state cannot be new state");
        addOrder(newState, removeOrder(oldState, oldPosition));
    }
}
