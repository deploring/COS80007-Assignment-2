package au.edu.swin.ajass.views;

import au.edu.swin.ajass.Utilities;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.Order;
import au.edu.swin.ajass.models.OrderLocation;
import au.edu.swin.ajass.models.Table;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sky on 26/9/18.
 */
public class OrderStatusView implements IView {

    // Reference to MainView
    private MainView main;

    // View Elements
    private JPanel statusPanel;
    private DefaultTableModel waitingModel, servedModel;
    private JLabel waitingLabel, servedLabel;
    private JTable waitingTable, servedTable;

    public OrderStatusView(MainView main) {
        this.main = main;

        // Generate border
        statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 20));
        statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Order Status", TitledBorder.LEFT, TitledBorder.TOP));
        statusPanel.setPreferredSize(new Dimension(989, 200));

        // Generate View elements
        generate();
    }

    public void updateTables() {
        // Clear tables first.
        waitingModel.setRowCount(0);
        servedModel.setRowCount(0);

        /* STREAM */
        // Use an atomic integer for index counting for table number.
        AtomicInteger tableNo = new AtomicInteger(0);
        main.getController().getTables().forEach(table -> {
            tableNo.addAndGet(1);
            Object[][] waitingData = new Object[table.getNumberOfOrders(OrderState.WAITING)][];
            Object[][] servedData = new Object[table.getNumberOfOrders(OrderState.SERVED)][];

            // IMPORTANT: For each order, also invisibly store the table number and the order's position in its linked list.
            // Every time the linked lists change, this updates, so it is irrelevant if values become outdated.

            int i = 0;
            for (Iterator<Order> iter = table.getOrders(OrderState.WAITING); iter.hasNext(); ) {
                Order order = iter.next();
                waitingData[i] = new String[]{String.format("%s | Table %d | %s", order.getCustomerName(), tableNo.get(), Utilities.generateOrderRowData(order)[1]), String.valueOf(i), String.valueOf(tableNo.get())};
                i++;
            }

            // Reset counter and do the same thing for the served orders.
            i = 0;
            for (Iterator<Order> iter = table.getOrders(OrderState.SERVED); iter.hasNext(); ) {
                Order order = iter.next();
                servedData[i] = new String[]{String.format("%s | Table %d | %s", order.getCustomerName(), tableNo.get(), Utilities.generateOrderRowData(order)[1]), String.valueOf(i), String.valueOf(tableNo.get())};
                i++;
            }

            // Populate table models.
            for (Object[] waitingDatum : waitingData)
                waitingModel.addRow(waitingDatum);

            for (Object[] servedDatum : servedData)
                servedModel.addRow(servedDatum);
        });

        // Update relevant labels.
        if (main.getController().getNumberOfOrders(OrderState.WAITING) > 0)
            waitingLabel.setText("Orders with waiting state");
        else waitingLabel.setText("Orders with waiting state (No orders available to prepare)");

        if (main.getController().getNumberOfOrders(OrderState.SERVED) > 0)
            servedLabel.setText("Orders with served state");
        else servedLabel.setText("Orders with served state (No orders available to bill)");
    }

    @Override
    public void generate() {
        // Generate with models that cannot be edited.
        waitingModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        servedModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        waitingTable = new JTable(waitingModel);
        servedTable = new JTable(servedModel);

        //FIXME: JTable cells do not overflow horizontally.

        // Set column count once.
        waitingModel.setColumnCount(3);
        servedModel.setColumnCount(3);

        // Make the two right-most columns invisible so we can hide data there.
        waitingTable.removeColumn(waitingTable.getColumnModel().getColumn(2));
        waitingTable.removeColumn(waitingTable.getColumnModel().getColumn(1));
        servedTable.removeColumn(servedTable.getColumnModel().getColumn(2));
        servedTable.removeColumn(servedTable.getColumnModel().getColumn(1));

        // Remove table headers as per specification
        waitingTable.setTableHeader(null);
        servedTable.setTableHeader(null);

        // Scrollpanes
        JScrollPane waitingHolder = new JScrollPane(waitingTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        waitingHolder.setPreferredSize(new Dimension(450, 100));
        JScrollPane servedHolder = new JScrollPane(servedTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        servedHolder.setPreferredSize(new Dimension(450, 100));

        Font panelFont = new Font("Serif", Font.PLAIN, 15);
        Font tableFont = new Font("Serif", Font.PLAIN, 12);
        waitingTable.setFont(tableFont);
        servedTable.setFont(tableFont);

        waitingLabel = new JLabel("Orders with waiting state (No orders available to prepare)", SwingConstants.CENTER);
        waitingLabel.setFont(panelFont);
        waitingLabel.setPreferredSize(new Dimension(450, 20));
        servedLabel = new JLabel("Orders with served state (No orders available to bill)", SwingConstants.CENTER);
        servedLabel.setFont(panelFont);
        servedLabel.setPreferredSize(new Dimension(450, 20));

        statusPanel.add(waitingLabel);
        statusPanel.add(servedLabel);
        statusPanel.add(waitingHolder);
        statusPanel.add(servedHolder);

        // Event handlers

        waitingTable.getSelectionModel().addListSelectionListener(e -> {
            // When selecting something in the waiting table, de-select anything in the served table.
            // This essentially allows only one side to be interacted with at once.
            servedTable.clearSelection();
            main.getButtonView().reset();
            main.getButtonView().enablePrepareButton();
        });

        servedTable.getSelectionModel().addListSelectionListener(e -> {
            // When selecting something in the waiting table, de-select anything in the served table.
            // This essentially allows only one side to be interacted with at once.
            waitingTable.clearSelection();
            main.getButtonView().reset();
            main.getButtonView().enableBillButton();
        });
    }

    /**
     * Go through each selected row and retrieve hidden information
     * on the order so that it can be retrieved and prepared/billed.
     * This is returned as OrderLocation, and can be used to retrieve
     * the instance of an Order for whatever purpose. Neat!
     *
     * @param state Which table? WAITING or SERVED orders?
     * @return Selected order(s). (or their locations, so we can get them!)
     */
    OrderLocation[] getSelectedOrders(OrderState state) {
        JTable tableOfInterest;
        switch (state) {
            case WAITING:
                tableOfInterest = waitingTable;
                break;
            case SERVED:
                tableOfInterest = servedTable;
                break;
            default:
                throw new IllegalArgumentException("Unsupported/unknown order state");
        }

        // Generate Order Location data based on data in the table.
        OrderLocation[] result = new OrderLocation[tableOfInterest.getSelectedRows().length];
        for (int i = 0; i < tableOfInterest.getSelectedRows().length; i++) {
            // Retrieve this Order's position in its linked list, and also its table.
            int orderPosition = Integer.parseInt(String.valueOf(tableOfInterest.getModel().getValueAt(tableOfInterest.getSelectedRow(), 1)));
            int tableNumber = Integer.parseInt(String.valueOf(tableOfInterest.getModel().getValueAt(tableOfInterest.getSelectedRows()[i], 2)));
            result[i] = new OrderLocation(tableNumber, orderPosition);
        }

        // List of selected order locations!
        return result;
    }

    @Override
    public void reset() {
        // Order status should never be fully reset.
        waitingTable.clearSelection();
        servedTable.clearSelection();
    }

    @Override
    public JPanel getPanel() {
        return statusPanel;
    }
}
