package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.OrderState;

/**
 * Utility model to temporarily store:
 * <ul>
 *     <li>What table number an Order originates from.</li>
 *     <li>What position in the LinkedList it holds.</li>
 * </ul>
 *
 * Useful for when you want to locate or move around an
 * Order. Specifically when preparing and billing one.
 * @see au.edu.swin.ajass.views.OrderStatusView#getSelectedOrders(OrderState)
 */
public final class OrderLocation {

    private int tableNumber;
    private int position;

    public OrderLocation(int tableNumber, int position) {
        this.tableNumber = tableNumber;
        this.position = position;
    }

    public int getTable() {
        return tableNumber;
    }

    public int getPosition() {
        return position;
    }
}
