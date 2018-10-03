package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.MealType;
import au.edu.swin.ajass.enums.MenuItemType;
import au.edu.swin.ajass.models.MenuItem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by sky on 26/9/18.
 */
public class ChooseMenuItemsView implements IView {

    // Reference to MainView
    private MainView main;

    // View Elements
    private JPanel menuPanel;
    private JComboBox<String> foodList;
    private JComboBox<String> beverageList;

    // Logic variables
    private MealType currentMealType;

    public ChooseMenuItemsView(MainView main) {
        this.main = main;

        // Generate border
        menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 20));
        menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Choose Menu Items", TitledBorder.LEFT, TitledBorder.TOP));
        menuPanel.setPreferredSize(new Dimension(989, 100));

        // Generate View elements
        generate();
    }

    /**
     * Populate lists with a certain meal type.
     *
     * @param type Meal type to populate comboBox with.
     */
    public void populateLists(MealType type) {
        // Clear comboBoxes before filling.
        foodList.removeAllItems();
        beverageList.removeAllItems();

        // Enable them for selection.
        foodList.setEnabled(true);
        beverageList.setEnabled(true);

        // Re-set current meal type.
        currentMealType = type;

        // Populate food comboBox.
        foodList.addItem("--------------- Select Your Food ---------------");
        for (MenuItem item : main.getController().getMenuController().getMenuItem(MenuItemType.FOOD, type))
            foodList.addItem(item.getItemName());

        // Populate beverage comboBox.
        beverageList.addItem("------------- Select Your Beverage -------------");
        for (MenuItem item : main.getController().getMenuController().getMenuItem(MenuItemType.BEVERAGE, type))
            beverageList.addItem(item.getItemName());
    }

    /**
     * @return The currently selected food menu item and beverage menu item. Can be null.
     * @throws IllegalStateException Meal type has not been selected
     */
    MenuItem[] getSelectedItems() throws IllegalStateException {
        if (currentMealType == null)
            throw new IllegalStateException("Trying to get selected items before selecting meal type?");
        MenuItem food = foodList.getSelectedIndex() <= 0 ? null : main.getController().getMenuController().getMenuItem(MenuItemType.FOOD, currentMealType).get(foodList.getSelectedIndex() - 1);
        MenuItem beverage = beverageList.getSelectedIndex() <= 0 ? null : main.getController().getMenuController().getMenuItem(MenuItemType.BEVERAGE, currentMealType).get(beverageList.getSelectedIndex() - 1);
        return new MenuItem[]{food, beverage};
    }

    @Override
    public void generate() {
        JLabel foodLabel, beverageLabel;
        Font panelFont = new Font("Serif", Font.PLAIN, 15);

        foodLabel = new JLabel("Food");
        foodLabel.setFont(panelFont);
        beverageLabel = new JLabel("Beverage");
        beverageLabel.setFont(panelFont);

        foodList = new JComboBox<>();
        foodList.setFont(panelFont);
        foodList.setPreferredSize(new Dimension(300, 20));
        beverageList = new JComboBox<>();
        beverageList.setFont(panelFont);
        beverageList.setPreferredSize(new Dimension(300, 20));

        menuPanel.add(foodLabel);
        menuPanel.add(foodList);
        menuPanel.add(beverageLabel);
        menuPanel.add(beverageList);

        // Set combo boxes to default state.
        reset();
    }

    @Override
    public void reset() {
        // Disable and re-set stuff.
        currentMealType = null;
        foodList.setEnabled(false);
        beverageList.setEnabled(false);

        // Clear comboBoxes before filling.
        foodList.removeAllItems();
        beverageList.removeAllItems();

        // Default items.
        foodList.addItem("---------------- Select Food ----------------");
        beverageList.addItem("---------------- Select Beverage ----------------");
    }

    @Override
    public JPanel getPanel() {
        return menuPanel;
    }
}
