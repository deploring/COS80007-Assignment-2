package au.edu.swin.ajass.models;

import java.io.Serializable;

/**
 * Order is a model that represents a person's order.
 * Orders are contained within Tables, and are ordered
 * by a customer (name), and contain a food menu item
 * as well as a beverage menu item.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1.1
 * @since 0.1
 */
public final class Order implements Serializable {

    // Static constants
    public static int FOOD_ONLY = 0;
    public static int BEVERAGE_ONLY = 1;

    private int orderID;
    private String customerName;
    private MenuItem food, beverage;

    /**
     * @param customerName Name of the customer who made the order.
     * @param food         The food that was ordered.
     * @param beverage     The beverage that was ordered.
     */
    public Order(int orderID, String customerName, MenuItem food, MenuItem beverage) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.food = food;
        this.beverage = beverage;
    }

    /**
     * The constructor is different when only one item has
     * been ordered. It is considered unsafe to pass null
     * through a constructor, so we must discern what single
     * item has been ordered. In this case, static constants
     * can be used to tell the Order constructor what type
     * of food has been ordered, and assign the value to the
     * correct global field.
     *
     * @param customerName Name of the customer who made the order.
     * @param state        Did they only order food or only order a beverage?
     * @param item         The item ordered.
     */
    public Order(int orderID, String customerName, int state, MenuItem item) {
        this.orderID = orderID;
        this.customerName = customerName;

        if (state == FOOD_ONLY)
            this.food = item;
        else if (state == BEVERAGE_ONLY)
            this.beverage = item;
    }

    /* Getters */

    public String getCustomerName() {
        return customerName;
    }

    public MenuItem getFood() {
        return food;
    }

    public MenuItem getBeverage() {
        return beverage;
    }

    public int getOrderID() {
        return orderID;
    }
}
