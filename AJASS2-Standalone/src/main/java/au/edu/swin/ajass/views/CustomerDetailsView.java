package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.CustomerType;
import au.edu.swin.ajass.enums.MealType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Sub-view that allows the user to enter:
 * <ul>
 * <li>Customer's Name</li>
 * <li>Customer's Details</li>
 * <li>Meal Type</li>
 * </ul>
 *
 * @author Keagan Foster
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public class CustomerDetailsView implements IView {

    // Reference to MainView
    private MainView main;

    // View Elements
    private JPanel detailsPanel;
    private JTextField orderName, tableNum, groupSize;
    private JRadioButton[] buttons;
    private ButtonGroup mealButtons;
    private JButton singleButton, groupButton;

    // Checks for View Elements
    private boolean typeChoosen = false;
    private CustomerType type;

    CustomerDetailsView(MainView main) {
        this.main = main;

        // Create border outline.
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 20));
        detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Customer Details", TitledBorder.LEFT, TitledBorder.TOP));
        detailsPanel.setPreferredSize(new Dimension(989, 100));

        // Generate View elements.
        generate();
    }

    @Override
    public void generate() {
        detailsPanel.removeAll();
        if(typeChoosen == false){
            singleButton = new JButton("Single");
            singleButton.setPreferredSize(new Dimension(125, 30));
            singleButton.setFont(new Font("Serif", Font.BOLD, 13));

            groupButton = new JButton("Group");
            groupButton.setPreferredSize(new Dimension(125, 30));
            groupButton.setFont(new Font("Serif", Font.BOLD, 13));

            detailsPanel.add(singleButton);
            detailsPanel.add(groupButton);

            singleButton.addActionListener(e -> {
                typeChoosen=true;
                type = CustomerType.Single;
                displayContent();
            });

            groupButton.addActionListener(e -> {
                typeChoosen=true;
                type = CustomerType.Group;
                main.getChooseMenuItemsView().addGroupButtons();
                displayContent();
            });

            main.revalidate();
            main.repaint();
        }
    }

    /**
     * Called upon when user choices order type. Displays
     * options to fill out to complete order.
     */
    private void displayContent(){
        detailsPanel.remove(singleButton);
        detailsPanel.remove(groupButton);

        JLabel tableLabel, mealType;
        Font panelFont = new Font("Serif", Font.PLAIN, 15);

        orderName = new JTextField(15);
        groupSize = new JTextField(5);

        // Checks order type to display view elements accordingly
        if(type == CustomerType.Single){
            JLabel custLabel = new JLabel("Customer Name:");
            custLabel.setFont(panelFont);
            orderName.setFont(panelFont);

            detailsPanel.add(custLabel);
            detailsPanel.add(orderName);
        }
        else {
            JLabel groupLabel = new JLabel("Group Name:");
            groupLabel.setFont(panelFont);
            JLabel groupSizeLabel = new JLabel("Group Size:");
            groupSizeLabel.setFont(panelFont);

            orderName.setFont(panelFont);
            groupSize.setFont(panelFont);

            detailsPanel.add(groupLabel);
            detailsPanel.add(orderName);
            detailsPanel.add(groupSizeLabel);
            detailsPanel.add(groupSize);
        }

        tableLabel = new JLabel("Table Number:");
        tableLabel.setFont(panelFont);
        mealType = new JLabel("Meal Type: ");
        mealType.setFont(panelFont);


        tableNum = new JTextField(5);
        tableNum.setFont(panelFont);

        JRadioButton breakfastButton = new JRadioButton("Breakfast");
        breakfastButton.setFont(panelFont);
        JRadioButton lunchButton = new JRadioButton("Lunch");
        lunchButton.setFont(panelFont);
        JRadioButton dinnerButton = new JRadioButton("Dinner");
        dinnerButton.setFont(panelFont);

        mealButtons = new ButtonGroup();
        mealButtons.add(breakfastButton);
        mealButtons.add(lunchButton);
        mealButtons.add(dinnerButton);

        buttons = new JRadioButton[]{breakfastButton, lunchButton, dinnerButton};

        detailsPanel.add(tableLabel);
        detailsPanel.add(tableNum);
        detailsPanel.add(mealType);
        detailsPanel.add(breakfastButton);
        detailsPanel.add(lunchButton);
        detailsPanel.add(dinnerButton);

        main.revalidate();
        main.repaint();

        // Event handlers.
        for (JRadioButton button : buttons)
            button.addActionListener(e -> main.getChooseMenuItemsView().populateLists(getMealType()));
    }
    /**
     * Used by ButtonView to "soft reset", which resets everything
     * except for the table number text, as per specifications.
     *
     * @see ButtonView#generate()
     */
    public void softReset() {
        typeChoosen = false;
        type = null;
        orderName.setText("");
        groupSize.setText("");
        mealButtons.clearSelection();
        generate();
    }

    @Override
    public void reset() {
        typeChoosen = false;
        type = null;
        orderName.setText("");
        groupSize.setText("");
        tableNum.setText("");
        mealButtons.clearSelection();
        generate();
    }

    @Override
    public JPanel getPanel() {
        return detailsPanel;
    }

    /* Getters */

    public String getOrderName() {
        return orderName.getText();
    }

    public CustomerType getCustType(){
        return type;
    }

    public int getGroupSize(){
        if(groupSize.getText().equals("")){
            return 0;
        }
        return Integer.parseInt(groupSize.getText());
    }

    /**
     * @throws NumberFormatException Table number may not be a number.
     * @throws IllegalStateException Nothing may be entered.
     */
    public Integer getTableNumber() throws NumberFormatException, IllegalStateException {
        if (tableNum.getText().equals("")) throw new IllegalStateException();
        return Integer.parseInt(tableNum.getText());
    }

    /**
     * @throws IllegalStateException Nothing may be selected.
     */
    public MealType getMealType() throws IllegalStateException {
        for (JRadioButton button : buttons)
            if (button.isSelected())
                return MealType.valueOf(button.getText().toUpperCase());
        throw new IllegalStateException();
    }
}