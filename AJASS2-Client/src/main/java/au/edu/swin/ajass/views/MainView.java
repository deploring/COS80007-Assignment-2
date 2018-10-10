package au.edu.swin.ajass.views;

import au.edu.swin.ajass.controller.ClientController;
import au.edu.swin.ajass.models.MenuItem;
import au.edu.swin.ajass.models.Table;

import javax.swing.*;
import java.awt.*;

/**
 * This View displays each individual sub-section on the screen.
 */
public final class MainView extends JFrame {

    private ClientController controller;

    private IView choiceNutritionInfo, chooseMenuItems, customerDetails, orderedItems, orderStatus, buttonPanel;
    private JPanel holderPanel;

    public MainView() {
        super("AJASS2 - Client");

        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 10));
        createUI();

        // Initialise controller.
        try {
            controller = new ClientController(this);
        } catch (IllegalStateException ex) {
            // We have caught an illegal state. Close the program!
            dispose();
            System.exit(0);
        }
    }

    /**
     * Called by Client Controller when a connection is lost with
     * the server. UI will be disabled and currently saved orders
     * will be cleared.
     */
    public void connectionLost() {
        // Clear the current orders as they are no longer useful.
        getController().getMenuController().getTables().forEach(Table::clear);

        // Disable input by user to prevent further errors, until connection is re-established.
        getButtonView().disableAll();
        getCustomerDetailsView().disableAll();
        getOrderStatusView().updateTables();

        // Inform the user. Do so in a separate thread so nothing important is held up.
       new Thread(() -> JOptionPane.showMessageDialog(null,
                "The connection to the server has been lost. \n" +
                        "User controls are disabled until the program\n" +
                        "can re-establish a connection with the server.",
                "Connection Lost",
                JOptionPane.WARNING_MESSAGE)).start();
    }

    /**
     * Called by Client Controller when the connection to the
     * server is re-established. UI is re-enabled and orders
     * will be sent through from the server.
     */
    public void connectionReEstablished() {
        getButtonView().reEnableAll();
        getCustomerDetailsView().reEnableAll();
        getOrderStatusView().updateTables();
    }

    /**
     * @return Instance of Client Controller so that models may access stuff.
     */
    ClientController getController() {
        return controller;
    }

    private void createUI() {
        customerDetails = new CustomerDetailsView(this);
        chooseMenuItems = new ChooseMenuItemsView(this);
        orderStatus = new OrderStatusView(this);
        buttonPanel = new ButtonView(this);
        orderedItems = new OrderedItemsView(this);
        choiceNutritionInfo = new ChoiceNutritionInfoView(this);

        // Blank panel to show when nothing else is available.
        holderPanel = new JPanel();
        holderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        holderPanel.setPreferredSize(new Dimension(989, 150));

        // Show the default state, which is NOTHING.
        updateState(UIState.NOTHING);
    }

    /**
     * Calls the reset() method in all the IView classes.
     * Effectively resets the program.
     *
     * @see IView#reset()
     */
    void reset() {
        customerDetails.reset();
        chooseMenuItems.reset();
        orderStatus.reset();
        buttonPanel.reset();
        orderedItems.reset();
        choiceNutritionInfo.reset();
        updateState(UIState.NOTHING);
    }

    /**
     * Chooses what to show in that empty space as per specification.
     *
     * @param state What should be shown?
     */
    void updateState(UIState state) {
        switch (state) {
            case NOTHING:
                swap(holderPanel);
                break;
            case SHOW_ORDER_ITEMS:
                swap(orderedItems.getPanel());
                getOrderedItemsView().populateTable(getCustomerDetailsView().getTableNumber());
                break;
            case SHOW_NUTRITIONAL:
                swap(choiceNutritionInfo.getPanel());
                MenuItem[] items = getChooseMenuItemsView().getSelectedItems();
                getChoiceNutritionInfoView().populateTable(items[0], items[1]);
                break;
            case UPDATE_ORDERS:
                ((OrderStatusView) orderStatus).updateTables();
                break;
            default:
                throw new IllegalArgumentException("Unsupported/unknown UI state");
        }
    }

    /**
     * Clear the content panel and re-add elements with
     * a preferred panel filling the empty space.
     *
     * @param panel The panel to show in the empty space.
     */
    private void swap(JPanel panel) {
        // Remove everything from the content panel.
        getContentPane().removeAll();

        getContentPane().add(customerDetails.getPanel());
        getContentPane().add(chooseMenuItems.getPanel());
        getContentPane().add(orderStatus.getPanel());

        getContentPane().add(panel);

        getContentPane().add(buttonPanel.getPanel());

        // Revalidate the frame and content pane.
        revalidate();
        repaint();
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    /**
     * Enumerated type that defines the finite
     * amount of states the screen may be in.
     */
    public enum UIState {
        NOTHING,
        SHOW_ORDER_ITEMS,
        SHOW_NUTRITIONAL,
        UPDATE_ORDERS
    }

    /* Instance getters for necessary Views */

    CustomerDetailsView getCustomerDetailsView() {
        return (CustomerDetailsView) customerDetails;
    }

    ChooseMenuItemsView getChooseMenuItemsView() {
        return (ChooseMenuItemsView) chooseMenuItems;
    }

    ButtonView getButtonView() {
        return (ButtonView) buttonPanel;
    }

    public OrderStatusView getOrderStatusView() {
        return (OrderStatusView) orderStatus;
    }

    private ChoiceNutritionInfoView getChoiceNutritionInfoView() {
        return (ChoiceNutritionInfoView) choiceNutritionInfo;
    }

    private OrderedItemsView getOrderedItemsView() {
        return (OrderedItemsView) orderedItems;
    }
}
