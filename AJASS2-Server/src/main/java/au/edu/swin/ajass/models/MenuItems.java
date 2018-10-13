package au.edu.swin.ajass.models;

import au.edu.swin.ajass.controllers.ServerController;
import au.edu.swin.ajass.enums.MealType;
import au.edu.swin.ajass.enums.MenuItemType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * This class loads and stores the values contained
 * within the Assignment2Data resource file.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public final class MenuItems {

    private ServerController server;
    private HashMap<MenuItemType, HashMap<MealType, LinkedList<MenuItem>>> menuItems;

    public MenuItems(ServerController server) {
        this.server = server;
        menuItems = new HashMap<>();
    }

    /**
     * Returns an item by PLU. This is used when the server is loading orders
     * back in after a restart and the only reference held to the item is a PLU.
     *
     * @param foodType The item's food type.
     * @param mealType The item's meal type.
     * @param PLU The item's PLU.
     * @return The item!
     * @throws IllegalArgumentException The item may not exist..
     */
    public MenuItem getByPLU(MenuItemType foodType, MealType mealType, int PLU) {
        // Double check the relevant map and list exist first!
        if (!menuItems.containsKey(foodType) || !menuItems.get(foodType).containsKey(mealType))
            throw new IllegalArgumentException("Map/list doesn't exist?");

        // Check if any of the menu items share the same PLU.
        for (MenuItem item : menuItems.get(foodType).get(mealType))
            if (item.getPLU() == PLU) return item;
        throw new IllegalArgumentException("Unable to locate menu item by PLU");
    }

    /**
     * @return A set of menu item entries so that they can be exported to clients.
     */
    public Set<Map.Entry<MenuItemType, HashMap<MealType, LinkedList<MenuItem>>>> entrySet() {
        return menuItems.entrySet();
    }

    /**
     * Load the Menu Item values from the Assignment2Data.csv.
     */
    public void load() {
        BufferedReader input = null;
        try {
            // Create a buffered reader to read over the .csv resource file.
            input = new BufferedReader(new InputStreamReader(MenuItems.class.getResourceAsStream("/Assignment2Data.csv"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            // Impossibru!
        }
        if (input == null) return;
        // Use a stream to process each line.
        input.lines().forEach(this::parseMenuItem);
    }

    /**
     * Parse a single line from the .csv data file for menu items.
     * Only adds items into the map that don't already exist.
     *
     * @param line The line to parse.
     */
    private void parseMenuItem(String line) {
        // Split the line into multiple strings with ',' as the delimiter.
        String[] elements = line.split(",");
        if (elements.length != 10) return; // Ignore this line, as it does not contain the required amount of elements.
        try {
            // Go ahead and create the rest of the menu item class, cancel safely if any obvious errors are found.
            MenuItemType foodType = MenuItemType.valueOf(elements[0].toUpperCase());
            MealType mealType = MealType.valueOf(elements[1].toUpperCase());
            int PLU = Integer.parseInt(elements[9]);

            // Skip this item as it already exists in the map.
            if (menuItemExists(foodType, mealType, PLU)) return;

            String itemName = elements[2];
            double price = Double.valueOf(elements[3]);
            double energy = Double.valueOf(elements[4]);
            double protein = Double.valueOf(elements[5]);
            double carbs = Double.valueOf(elements[6]);
            double fat = Double.valueOf(elements[7]);
            double fibre = Double.valueOf(elements[8]);

            // All good? Then let's place it in the map.
            MenuItem result = new MenuItem(itemName, price, energy, protein, carbs, fat, fibre, PLU);
            placeItem(foodType, mealType, result);

            // Since this menu item doesn't exist in the database, let's write it to the database.
            server.getDatabase().newMenuItem(foodType, mealType, result);
        } catch (IllegalArgumentException e) {
            // Ignore this line, as it does not contain a valid entry.
            // It is most likely the header of the .csv file, we do not care about that.
        }
    }

    /**
     * Maps a menu item to the correct list.
     * Map Menu Item Type -> Meal Type -> Specific List of Menu Items
     *
     * @param foodType The menu item's food type. (food or beverage).
     * @param mealType The menu item's meal type. (breakfast, lunch, or dinner).
     * @param toPlace  The menu item to place in the list.
     */
    public void placeItem(MenuItemType foodType, MealType mealType, MenuItem toPlace) {
        // If the map inside the Food Type map doesn't exist, create it first.
        if (!menuItems.containsKey(foodType))
            menuItems.put(foodType, new HashMap<>());

        // If the list inside the Meal Type map inside the Food Type map doesn't exist, create it first.
        if (!menuItems.get(foodType).containsKey(mealType))
            menuItems.get(foodType).put(mealType, new LinkedList<>());

        // Add to the List that contains Menu Items of a specific food type and meal type.
        menuItems.get(foodType).get(mealType).add(toPlace);
    }

    /**
     * Double checks that an item being placed into a linked list does not
     * already exist. Unique menu items are only guaranteed inside individual
     * linked lists of a defined menu item type and meal type.
     *
     * @param foodType The menu item's food type.
     * @param mealType The menu item's meal type.
     * @param PLU      The PLU we're checking for to make sure it doesn't already exist.
     * @return If an item of a defined food type and meal type already exists in the linked list.
     */
    private boolean menuItemExists(MenuItemType foodType, MealType mealType, int PLU) {
        // If the map inside the Food Type map doesn't exist, the item doesn't exist.
        if (!menuItems.containsKey(foodType)) return false;

        // If the list inside the Meal Type map inside the Food Type map doesn't exist, it doesn't exist.
        if (!menuItems.get(foodType).containsKey(mealType)) return false;

        // Check if any of the menu items share the same PLU.
        for (MenuItem item : menuItems.get(foodType).get(mealType))
            if (item.getPLU() == PLU) return true;
        return false;
    }
}
