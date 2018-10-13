package au.edu.swin.ajass.views;

import au.edu.swin.ajass.controllers.MenuController;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.MenuItem;
import au.edu.swin.ajass.models.OrderLocation;
import au.edu.swin.ajass.models.Table;
import au.edu.swin.ajass.enums.CustomerType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * The bottom-most view that contains all the control buttons.
 * These buttons have numerous purposes, as outlined in the
 * specifications.
 *
 * @author Keagan Foster
 * @author Joshua Skinner
 * @version 1
 * @author 0.1
 */
public class ButtonView implements IView {

    // Reference to MainView
    private MainView main;

    // View Elements
    private JPanel buttonPanel;
    private JButton enterButton, displayChoicesButton, displayOrderButton, prepareButton, billButton, clearButton, quitButton;

    public ButtonView(MainView main) {
        this.main = main;

        // Create border outline.
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 13, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Command Buttons", TitledBorder.LEFT, TitledBorder.TOP));
        buttonPanel.setPreferredSize(new Dimension(989, 80));

        // Generate View elements.
        generate();
    }

    @Override
    public void generate() {
        enterButton = new JButton("Enter Data");
        displayChoicesButton = new JButton("Display Choices");
        displayOrderButton = new JButton("Display Order");
        prepareButton = new JButton("Prepare");
        billButton = new JButton("Bill");
        clearButton = new JButton("Clear Display");
        quitButton = new JButton("Quit");

        prepareButton.setEnabled(false);
        billButton.setEnabled(false);

        JButton[] buttons = {enterButton, displayChoicesButton, displayOrderButton, prepareButton, billButton, clearButton, quitButton};

        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(125, 30));
            button.setFont(new Font("Serif", Font.BOLD, 13));
        }

        buttonPanel.add(enterButton);
        buttonPanel.add(displayChoicesButton);
        buttonPanel.add(displayOrderButton);
        buttonPanel.add(prepareButton);
        buttonPanel.add(billButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // Button event handlers

        // Enter data and confirm order
        enterButton.addActionListener(e -> {
            // Validate orders name first and foremost
            String name = main.getCustomerDetailsView().getOrderName();
            if (name.length() == 0) {
                JOptionPane.showConfirmDialog(null, "Please enter the order's name", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if table number is valid and is in range.
            int tableNumber = main.getCustomerDetailsView().getTableNumber();
            if (tableNumber <= 0 || tableNumber > MenuController.NUMBER_OF_TABLES) {
                JOptionPane.showConfirmDialog(null, "Please enter a valid table number (1-8)", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // We can assume table is valid if it is in range.
            Table table = main.getController().getTable(tableNumber);

            // Checks order type to create corresponding order
            if(main.getCustomerDetailsView().getCustType() == CustomerType.Single){
                try {
                    // Validate their choices.
                    MenuItem[] choices = main.getChooseMenuItemsView().getSelectedItems();
                    if (choices[0] == null && choices[1] == null) {
                        JOptionPane.showConfirmDialog(null, "Please select a food or beverage item to place order", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create order.
                    main.getController().createIndividualOrder(table, name, choices[0], choices[1]);
                    JOptionPane.showConfirmDialog(null, "Order placed successfully.", "Information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    // Reset!
                    main.getCustomerDetailsView().softReset();
                    main.getChooseMenuItemsView().reset();

                    // Update state!
                    main.updateState(MainView.UIState.UPDATE_ORDERS);
                    main.updateState(MainView.UIState.NOTHING);
                } catch (IllegalStateException ex) {
                    JOptionPane.showConfirmDialog(null, "Please select a meal type to place order", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }
            // Checks order type to create corresponding order
            else if(main.getCustomerDetailsView().getCustType() == CustomerType.Group){
                // Check if the group number is valid and is in range
                int groupNumber = main.getCustomerDetailsView().getGroupSize();
                if(groupNumber <= 1 || groupNumber > 8){
                    JOptionPane.showConfirmDialog(null, "Please enter a group size between 2-8", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Create group order
                main.getController().createGroupOrder(table, name, groupNumber, main.getChooseMenuItemsView().getGroupOrder());
                JOptionPane.showConfirmDialog(null, "Order placed successfully.", "Information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                // Reset!
                main.getCustomerDetailsView().softReset();
                main.getChooseMenuItemsView().reset();

                // Update state!
                main.updateState(MainView.UIState.UPDATE_ORDERS);
                main.updateState(MainView.UIState.NOTHING);
            }
        });

        // Display nutritional info for choices
        displayChoicesButton.addActionListener(e -> {
            try {
                // Validate their choices.
                MenuItem[] choices = main.getChooseMenuItemsView().getSelectedItems();
                if (choices[0] == null && choices[1] == null) {
                    JOptionPane.showConfirmDialog(null, "Please select a food or beverage item to view Nutritional Information", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // All good? Let's show nutritional information then.
                main.updateState(MainView.UIState.SHOW_NUTRITIONAL);
            } catch (IllegalStateException ex) {
                JOptionPane.showConfirmDialog(null, "Please select a meal type to view Nutritional Information", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });

        // Display orders for a table
        displayOrderButton.addActionListener(e -> {
            try {
                // Check if table number is valid and is in range.
                int tableNumber = main.getCustomerDetailsView().getTableNumber();
                if (tableNumber <= 0 || tableNumber > MenuController.NUMBER_OF_TABLES)
                    throw new IllegalArgumentException();

                // We can assume table is valid if it is in range.
                Table table = main.getController().getTable(tableNumber);

                if (table.getTotalNumberOfOrders() == 0)
                    JOptionPane.showConfirmDialog(null, "There are no orders to display for this table", "Information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                else
                    main.updateState(MainView.UIState.SHOW_ORDER_ITEMS);
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showConfirmDialog(null, "Please enter a valid table number (1-8)", "Validation Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });

        // Prepare selected waiting orders
        prepareButton.addActionListener(e -> {
            OrderLocation[] toPrepare = main.getOrderStatusView().getSelectedOrders(OrderState.WAITING);

            if (toPrepare.length == 0) {
                // This should never appear but show this dialog if it does anyway.
                JOptionPane.showConfirmDialog(null, "You have not selected any orders to prepare", "Continuity Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "Do you want to proceed for preparing order(s)", "Confirmation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                // Go through each individual order location, retrieve the order, and prepare it.
                for (OrderLocation location : toPrepare)
                    main.getController().prepareOrder(location);

                // Show message.
                JOptionPane.showConfirmDialog(null, "Selected orders prepared", "Information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                // Update state!
                main.updateState(MainView.UIState.UPDATE_ORDERS);
                reset();
            }
        });

        // Bill selected served orders
        billButton.addActionListener(e -> {
            OrderLocation[] toBill = main.getOrderStatusView().getSelectedOrders(OrderState.SERVED);

            if (toBill.length == 0) {
                // This should never appear but show this dialog if it does anyway.
                JOptionPane.showConfirmDialog(null, "You have not selected any orders to bill", "Continuity Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "Do you want to proceed for billing order(s)", "Confirmation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                // Go through each individual order location, retrieve the order, and prepare it.
                for (OrderLocation location : toBill)
                    main.getController().billOrder(location);

                // Show message.
                JOptionPane.showConfirmDialog(null, "Selected orders billed", "Information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                // Update state!
                main.updateState(MainView.UIState.UPDATE_ORDERS);
                reset();
            }
        });

        // Clear display
        clearButton.addActionListener(e -> main.reset());

        // Dispose JFrame when attempting to quit
        quitButton.addActionListener(e -> main.dispose());
    }

    /**
     * Called by OrderStatusView when a table cell is clicked
     * and is allowed to be prepared. (Waiting Orders)
     */
    void enablePrepareButton() {
        prepareButton.setEnabled(true);
    }

    /**
     * Called by OrderStatusView when a table cell is clicked
     * and is allowed to be billed. (Served Orders)
     */
    void enableBillButton() {
        billButton.setEnabled(true);
    }

    @Override
    public void reset() {
        billButton.setEnabled(false);
        prepareButton.setEnabled(false);
    }

    public JPanel getPanel() {
        return buttonPanel;
    }
}