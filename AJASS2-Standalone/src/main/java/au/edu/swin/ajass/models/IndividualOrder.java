package au.edu.swin.ajass.models;

public class IndividualOrder extends Order {

    private boolean billed;

    /**
     * @param customerName Name of the customer who made the order.
     * @param food         The food that was ordered.
     * @param beverage     The beverage that was ordered.
     */
    public IndividualOrder(String customerName, MenuItem food, MenuItem beverage) {
        orderName = customerName;
        this.food = food;
        this.beverage = beverage;
        this.billed = false;
        isGroupOrder = false;
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
    public IndividualOrder(String customerName, int state, MenuItem item) {
        orderName = customerName;
        this.billed = false;

        if (state == FOOD_ONLY)
            this.food = item;
        else if (state == BEVERAGE_ONLY)
            this.beverage = item;
    }
}