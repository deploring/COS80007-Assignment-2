package au.edu.swin.ajass;

import au.edu.swin.ajass.models.MenuItem;
import au.edu.swin.ajass.models.Order;

/**
 * This class is used to store random utility methods
 * that we felt belong to no class and could be used
 * in any realistic scenario in any class.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @author 1
 * @since 0.1
 */
public class Utilities {

    /**
     * @param items A variable list of Menu Items.
     * @return A new MenuItem instance with its values being the sum of the values in all MenuItems supplied.
     */
    public static MenuItem totalMenuItems(MenuItem... items) {
        if (items.length == 0) throw new IllegalArgumentException("No MenuItem supplied?");
        double price = 0, energy = 0, protein = 0, carbs = 0, fat = 0, fibre = 0;
        for (MenuItem item : items) {
            // Ignore nulls as they may appear.
            if (item == null) continue;
            price += item.getPrice();
            energy += item.getEnergy();
            protein += item.getProtein();
            carbs += item.getCarbs();
            fat += item.getFat();
            fibre += item.getFibre();
        }
        return new MenuItem("Total of Items", price, energy, protein, carbs, fat, fibre, -1);
    }

    /**
     * @param order An order.
     * @return Table model row data generated for this given order.
     */
    public static String[] generateOrderRowData(Order order) {
        String[] result = new String[2];
        result[0] = order.getCustomerName();
        if (order.getFood() == null && order.getBeverage() != null)
            // Show beverage only.
            result[1] = order.getBeverage().getItemName();
        else if (order.getFood() != null && order.getBeverage() == null)
            // Show food only.
            result[1] = order.getFood().getItemName();
        else if (order.getFood() != null && order.getBeverage() != null)
            // Show both.
            result[1] = String.format("%s, %s", order.getFood().getItemName(), order.getBeverage().getItemName());
        else
            // Impossibru! We have validation!
            throw new IllegalArgumentException("Both beverage and food are null for an order???");
        return result;
    }
}
