package au.edu.swin.ajass.views;

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
    private JTextField custName, tableNum;
    private JRadioButton[] buttons;
    private ButtonGroup mealButtons;

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
        JLabel custLabel, tableLabel, mealType;
        Font panelFont = new Font("Serif", Font.PLAIN, 15);

        custLabel = new JLabel("Customer Name:");
        custLabel.setFont(panelFont);
        tableLabel = new JLabel("Table Number:");
        tableLabel.setFont(panelFont);
        mealType = new JLabel("Meal Type: ");
        mealType.setFont(panelFont);

        custName = new JTextField(20);
        custName.setFont(panelFont);
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

        detailsPanel.add(custLabel);
        detailsPanel.add(custName);
        detailsPanel.add(tableLabel);
        detailsPanel.add(tableNum);
        detailsPanel.add(mealType);
        detailsPanel.add(breakfastButton);
        detailsPanel.add(lunchButton);
        detailsPanel.add(dinnerButton);

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
        custName.setText("");
        mealButtons.clearSelection();
    }

    @Override
    public void reset() {
        custName.setText("");
        tableNum.setText("");
        mealButtons.clearSelection();
    }

    @Override
    public JPanel getPanel() {
        return detailsPanel;
    }

    /* Getters */

    public String getCustomerName() {
        return custName.getText();
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
