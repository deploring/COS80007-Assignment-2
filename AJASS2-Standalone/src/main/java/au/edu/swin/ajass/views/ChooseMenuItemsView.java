package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.MealType;
import au.edu.swin.ajass.enums.MenuItemType;
import au.edu.swin.ajass.models.MenuItem;
import au.edu.swin.ajass.enums.CustomerType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This view contains two separate combo boxes that allows
 * the user to select one food item and one beverage item.
 *
 * @author Keagan Foster
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public class ChooseMenuItemsView implements IView {

    // Reference to MainView
    private MainView main;

    // View Elements
    private JPanel menuPanel;
    private JComboBox<String> foodList;
    private JComboBox<String> beverageList;
    private JButton groupAddButton, groupViewButton, groupResetButton;

    // Logic variables
    private MealType currentMealType;

    //Group Order container
    private java.util.List<MenuItem[]> orders;

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
        for (MenuItem item : main.getController().getMenuItems().get(MenuItemType.FOOD, type))
            foodList.addItem(item.getItemName());

        // Populate beverage comboBox.
        beverageList.addItem("------------- Select Your Beverage -------------");
        for (MenuItem item : main.getController().getMenuItems().get(MenuItemType.BEVERAGE, type))
            beverageList.addItem(item.getItemName());
    }

    /**
     * @return The currently selected food menu item and beverage menu item. Can be null.
     * @throws IllegalStateException Meal type has not been selected
     */
    MenuItem[] getSelectedItems() throws IllegalStateException {
        if (currentMealType == null)
            throw new IllegalStateException("Trying to get selected items before selecting meal type?");
        MenuItem food = foodList.getSelectedIndex() <= 0 ? null : main.getController().getMenuItems().get(MenuItemType.FOOD, currentMealType).get(foodList.getSelectedIndex() - 1);
        MenuItem beverage = beverageList.getSelectedIndex() <= 0 ? null : main.getController().getMenuItems().get(MenuItemType.BEVERAGE, currentMealType).get(beverageList.getSelectedIndex() - 1);
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

    /**
     *  Called if the order type is a group order.
     *  Creates and display buttons along with their
     *  event handlers.
     */
    public void addGroupButtons(){
        //JButtons to display
        groupAddButton = new JButton("Add");
        groupViewButton = new JButton("View");
        groupResetButton = new JButton("Reset");

        // Container for temporary orders to be held
        orders = new ArrayList<>();

        // Event handler for when the 'Add' button is clicked
        groupAddButton.addActionListener(e -> {
            // If items are not empty, add the order
            MenuItem[] items = getSelectedItems();
            if(orders.size() < main.getCustomerDetailsView().getGroupSize()){
                if(items[0] == null && items[1] == null ){
                    JOptionPane.showConfirmDialog(null, "Please select a food or beverage item to place order", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }
                else{
                    orders.add(getSelectedItems());
                }
            }
            else {
                JOptionPane.showConfirmDialog(null, "Maximum number of orders reached", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });
        // Event handler for when the 'View' button is clicked
        groupViewButton.addActionListener(e -> {
            // Format and display orders in a JOptionPane
            String viewOrder = "";
            for(int i = 0; i < orders.size(); i++){
                if(orders.get(i)[0] != null && orders.get(i)[1] != null)
                    viewOrder += orders.get(i)[0].getItemName() + " | " + orders.get(i)[1].getItemName() + "\n";
                else if (orders.get(i)[0] != null)
                    viewOrder += orders.get(i)[0].getItemName() + "\n";
                else if (orders.get(i)[1] != null)
                    viewOrder += orders.get(i)[1].getItemName() + "\n";
                else
                    throw new IllegalArgumentException("Food and beverage cannot be null");
            }
            if(orders.size() == 0){
                viewOrder = "No orders placed";
            }
            JOptionPane.showMessageDialog(null, viewOrder);
        });
        // Event handler for when the 'Remove' button is clicked
        groupResetButton.addActionListener(e -> {
            // Removes all orders
            orders.removeAll(orders);
        });

        if(main.getCustomerDetailsView().getCustType() == CustomerType.Group){
            menuPanel.add(groupAddButton);
            menuPanel.add(groupViewButton);
            menuPanel.add(groupResetButton);
        }
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

        //Clear group buttons
        if(menuPanel.getComponentCount() > 4){
            menuPanel.remove(groupAddButton);
            menuPanel.remove(groupViewButton);
            menuPanel.remove(groupResetButton);
        }

        // Default items.
        foodList.addItem("---------------- Select Food ----------------");
        beverageList.addItem("---------------- Select Beverage ----------------");
    }

    @Override
    public JPanel getPanel() {
        return menuPanel;
    }

    // Getter

    public List<MenuItem[]> getGroupOrder(){return orders;}
}
