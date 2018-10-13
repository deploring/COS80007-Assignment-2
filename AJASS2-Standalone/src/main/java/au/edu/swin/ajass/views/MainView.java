package au.edu.swin.ajass.views;

import au.edu.swin.ajass.controllers.MenuController;
import au.edu.swin.ajass.models.MenuItem;

import javax.swing.*;
import java.awt.*;

/**
 * This View displays each individual sub-section on the screen.
 * It also acts as the central class where models, views, and
 * controllers can all communicate with one another.
 *
 * @author Joshua Skinner
 * @author Keagan Foster
 * @version 1.0
 * @since 0.1
 */
public final class MainView extends JFrame {

    private MenuController controller;
    private IView choiceNutritionInfo, chooseMenuItems, customerDetails, orderedItems, orderStatus, buttonPanel;
    private JPanel holderPanel;

    public MainView() {
        super("AJASS2 - Standalone");

        // Initialise controller.
        controller = new MenuController();

        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 10));
        createUI();
    }

    /**
     * @return Instance of Menu Controller so models may be accessed/modified by controller.
     */
    MenuController getController() {
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

    OrderStatusView getOrderStatusView() {
        return (OrderStatusView) orderStatus;
    }

    private ChoiceNutritionInfoView getChoiceNutritionInfoView() {
        return (ChoiceNutritionInfoView) choiceNutritionInfo;
    }

    private OrderedItemsView getOrderedItemsView() {
        return (OrderedItemsView) orderedItems;
    }
}
