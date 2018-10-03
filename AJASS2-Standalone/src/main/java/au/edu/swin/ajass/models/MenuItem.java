package au.edu.swin.ajass.models;

/**
 * An item that is located on the menu, and can be either
 * a Food Item, or a Beverage Item. This class does not
 * discriminate between food and beverages, as they should
 * always be stored separately from each other.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public final class MenuItem {

    private String itemName;
    private double price, energy, protein, carbs, fat, fibre;
    private int PLU;

    /**
     * Creates a new instance of a Menu Item.
     *
     * @param itemName The descriptive name of this item.
     * @param price The price, in $AUD, for this item.
     * @param energy The energy, in kJ, for this item.
     * @param protein The protein content, in grams, for this item.
     * @param carbs The carbohydrate content, in grams, for this item.
     * @param fat The fat content, in grams, for this item.
     * @param fibre The fibre content, in grams, for this item.
     * @param PLU The price look-up number for this item (a.k.a. menu item ID)
     */
    public MenuItem(String itemName, double price, double energy, double protein, double carbs, double fat, double fibre, int PLU) {
        this.itemName = itemName;
        this.price = price;
        this.energy = energy;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.fibre = fibre;
        this.PLU = PLU;
    }

    /* Getters */

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public double getEnergy() {
        return energy;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }

    public double getFibre() {
        return fibre;
    }

    public int getPLU() {
        return PLU;
    }
}
