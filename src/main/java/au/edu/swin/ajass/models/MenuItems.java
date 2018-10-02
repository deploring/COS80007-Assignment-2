package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.MealType;
import au.edu.swin.ajass.enums.MenuItemType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class loads and stores the values contained
 * within the Assignment2Data resource file.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public class MenuItems {

    private HashMap<MenuItemType, HashMap<MealType, LinkedList<MenuItem>>> menuItems;

    public MenuItems() {
        menuItems = new HashMap<>();
        load();
    }

    /**
     * Returns a list of Menu Items of a specific food and meal type.
     *
     * @param foodType The food type we want.
     * @param mealType The meal type we want.
     * @return The list of relevant menu items.
     */
    public List<MenuItem> get(MenuItemType foodType, MealType mealType) {
        return menuItems.get(foodType).get(mealType);
    }

    /**
     * Load the Menu Item values from the Assignment2Data.csv.
     */
    private void load() {
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
            String itemName = elements[2];
            double price = Double.valueOf(elements[3]);
            double energy = Double.valueOf(elements[4]);
            double protein = Double.valueOf(elements[5]);
            double carbs = Double.valueOf(elements[6]);
            double fat = Double.valueOf(elements[7]);
            double fibre = Double.valueOf(elements[8]);
            int PLU = Integer.parseInt(elements[9]);

            // All good? Then let's place it in the map.
            placeQuestion(foodType, mealType, new MenuItem(itemName, price, energy, protein, carbs, fat, fibre, PLU));
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
    private void placeQuestion(MenuItemType foodType, MealType mealType, MenuItem toPlace) {
        // If the map inside the Food Type map doesn't exist, create it first.
        if (!menuItems.containsKey(foodType))
            menuItems.put(foodType, new HashMap<>());

        // If the list inside the Meal Type map inside the Food Type map doesn't exist, create it first.
        if (!menuItems.get(foodType).containsKey(mealType))
            menuItems.get(foodType).put(mealType, new LinkedList<>());

        // Add to the List that contains Menu Items of a specific food type and meal type.
        menuItems.get(foodType).get(mealType).add(toPlace);
    }
}
