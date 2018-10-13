package au.edu.swin.ajass.models;

import java.util.List;

/**
 * Order is a model that represents a person's order.
 * Orders are contained within Tables.
 */
public class Order {
    // Static constants
    public static int FOOD_ONLY = 0;
    public static int BEVERAGE_ONLY = 1;

    // Overridden variables
    private boolean billed;
    public String orderName;
    public boolean isGroupOrder;
    public MenuItem food, beverage;

    /**
     * Marks the Order as billed.
     */
    public void bill() {
        billed = true;
    }

    /* Getters */
    public String getOrderName() {
        return orderName;
    }

    public boolean isBilled() {
        return billed;
    }

    public MenuItem getFood() {
        return food;
    }

    public MenuItem getBeverage() {
        return beverage;
    }

    public List<MenuItem[]> getItems(){return null;}

    public int getOrderSize(){return 1;}
}