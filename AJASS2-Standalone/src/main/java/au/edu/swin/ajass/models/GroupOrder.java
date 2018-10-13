package au.edu.swin.ajass.models;

import java.util.List;

public class GroupOrder extends Order {

    private int groupSize;
    private boolean billed;
    private List<MenuItem[]> orders;

    /**
     * @param size      The size of the group.
     * @param name      The name of the group who made the order.
     * @param items     The menu items ordered by the group.
     */
    public GroupOrder(int size, String name, List<MenuItem[]> items){
        groupSize = size;
        orderName = name;
        orders = items;
        isGroupOrder = true;
    }

    /* Getters */
    public int getGroupSize(){
        return groupSize;
    }

    @Override
    public int getOrderSize(){
        return orders.size();
    }

    @Override
    public List<MenuItem[]> getItems(){
        return orders;
    }

}
