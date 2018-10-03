package au.edu.swin.ajass.models;

/**
 * Order is a model that represents a person's order.
 * Orders are contained within Tables.
 */
public final class Order {

    // Static constants
    public static int FOOD_ONLY = 0;
    public static int BEVERAGE_ONLY = 1;

    private String customerName;
    private MenuItem food, beverage;
    private boolean billed;

    /**
     * @param customerName Name of the customer who made the order.
     * @param food         The food that was ordered.
     * @param beverage     The beverage that was ordered.
     */
    public Order(String customerName, MenuItem food, MenuItem beverage) {
        this.customerName = customerName;
        this.food = food;
        this.beverage = beverage;
        this.billed = false;
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
     * @param state Did they only order food or only order a beverage?
     * @param item The item ordered.
     */
    public Order(String customerName, int state, MenuItem item) {
        this.customerName = customerName;
        this.billed = false;

        if (state == FOOD_ONLY)
            this.food = item;
        else if (state == BEVERAGE_ONLY)
            this.beverage = item;
    }

    /**
     * Marks the Order as billed.
     */
    public void bill() {
        billed = true;
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

    public boolean isBilled() {
        return billed;
    }
}
