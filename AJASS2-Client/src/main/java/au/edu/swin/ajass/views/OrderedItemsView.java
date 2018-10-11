package au.edu.swin.ajass.views;

import au.edu.swin.ajass.Utilities;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.Order;
import au.edu.swin.ajass.models.Table;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This view is an intermittently-displayed view, which shows
 * all the current orders on a table that have yet to be billed.
 *
 * @author Keagan Foster
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 */
public class OrderedItemsView implements IView {

    // Reference to MainView
    private MainView main;

    // View Elements
    private JPanel itemsPanel;
    private JTable itemsTable;
    private DefaultTableModel model;

    OrderedItemsView(MainView main) {
        this.main = main;

        // Generate layout
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));
        itemsPanel.setPreferredSize(new Dimension(989, 150));

        // Generate View elements
        generate();
    }

    /**
     * Populates JTable with Table order data.
     *
     * @param tableNum The table number to reference.
     */
    void populateTable(int tableNum) {
        // Reset old values.
        reset();

        // Get associated table number.
        Table table = main.getController().getMenuController().getTable(tableNum);

        // Set border title.
        itemsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Ordered Items at Table " + tableNum, TitledBorder.LEFT, TitledBorder.TOP));

        String[][] orderData = new String[table.getTotalNumberOfOrders()][2];

        // Populate orderData with waiting orders, and then served orders.
        int i = -1;
        for (Iterator<Order> iter = table.getOrders(OrderState.WAITING); iter.hasNext(); )
            orderData[++i] = Utilities.generateOrderRowData(iter.next());
        for (Iterator<Order> iter = table.getOrders(OrderState.SERVED); iter.hasNext(); )
            orderData[++i] = Utilities.generateOrderRowData(iter.next());


        // Add rows to the table model.
        /* STREAM */
        Arrays.stream(orderData).forEach(model::addRow);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void generate() {
        model = new DefaultTableModel();
        itemsTable = new JTable(model);
        itemsTable.setBackground(Color.WHITE);
        JScrollPane holder = new JScrollPane(itemsTable);
        holder.setPreferredSize(new Dimension(950, 110));
        itemsPanel.add(holder);

        // Add column information.
        model.addColumn("Customer Name");
        model.addColumn("Ordered Items");
        itemsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
    }

    @Override
    public void reset() {
        // Remove old values from table model.
        model.setRowCount(0);
    }

    @Override
    public JPanel getPanel() {
        return itemsPanel;
    }
}
