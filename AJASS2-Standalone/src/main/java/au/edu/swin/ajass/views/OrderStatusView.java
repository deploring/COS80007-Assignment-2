package au.edu.swin.ajass.views;

import au.edu.swin.ajass.Utilities;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.Order;
import au.edu.swin.ajass.models.OrderLocation;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This view is responsible for displaying orders of specific
 * states in separate tables. In the context of the requirements,
 * the only orders that need to be displayed are the ones marked
 * as WAITING or SERVED. BILLED orders do not need to be displayed.
 *
 * @author Keagan Foster
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 * @see OrderState
 */
public class OrderStatusView implements IView {

    // Reference to MainView
    private MainView main;

    // View Elements
    private JPanel statusPanel;
    private DefaultTableModel waitingModel, servedModel;
    private JLabel waitingLabel, servedLabel;
    private JTable waitingTable, servedTable;
    private Object[][] waitingData, servedData;

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
            waitingData = new Object[table.getNumberOfOrders(OrderState.WAITING)][];
            servedData = new Object[table.getNumberOfOrders(OrderState.SERVED)][];

            // IMPORTANT: For each order, also invisibly store the table number and the order's position in its linked list.
            // Every time the linked lists change, this updates, so it is irrelevant if values become outdated.

            int i = 0;
            for (Iterator<Order> iter = table.getOrders(OrderState.WAITING); iter.hasNext(); ) {
                Order order = iter.next();
                String orderInfo = resizeOrderData(order, tableNo.get());
                waitingData[i] = new String[]{orderInfo, String.valueOf(i), String.valueOf(tableNo.get())};
                i++;
            }

            // Reset counter and do the same thing for the served orders.
            i = 0;
            for (Iterator<Order> iter = table.getOrders(OrderState.SERVED); iter.hasNext(); ) {
                Order order = iter.next();
                String orderInfo = resizeOrderData(order, tableNo.get());
                servedData[i] = new String[]{orderInfo,  String.valueOf(i), String.valueOf(tableNo.get())};
                i++;
            }

            // Populate table models.
            i = 0;
            for (Object[] waitingDatum : waitingData){
                waitingModel.addRow(waitingDatum);
                String rowData = (String)waitingDatum[0];
                int rowSize = resizeRow(rowData);
                waitingTable.setRowHeight(i, waitingTable.getRowHeight() * rowSize);
                i++;
            }

            i=0;
            for (Object[] servedDatum : servedData){
                servedModel.addRow(servedDatum);
                String rowData = (String)servedDatum[0];
                int rowSize = resizeRow(rowData);
                servedTable.setRowHeight(i, servedTable.getRowHeight() * rowSize);
                i++;
            }
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
        waitingModel = new DefaultTableModel(0, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        servedModel = new DefaultTableModel(0, 1) {
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
            int trueIndex = tableOfInterest.getSelectedRows().length - 1 - i;
            // Retrieve this Order's position in its linked list, and also its table.
            int orderPosition = Integer.parseInt(String.valueOf(tableOfInterest.getModel().getValueAt(tableOfInterest.getSelectedRows()[trueIndex], 1)));
            int tableNumber = Integer.parseInt(String.valueOf(tableOfInterest.getModel().getValueAt(tableOfInterest.getSelectedRows()[trueIndex], 2)));
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

    /**
     * @param order     Content to be reformatted
     * @param tableNo   Information attached to the row
     * @return  Reformatted string
     */
    private String resizeOrderData(Order order, int tableNo){
        String orderInfo = String.format("<html>%s | Table %d | %s</html>", order.getOrderName(), tableNo, Utilities.generateOrderRowData(order)[1]);

        if(orderInfo.length() > 95){
            StringBuilder str =  new StringBuilder(orderInfo);
            str.insert(95, "<br>");
        }

        return orderInfo;
    }

    /**
     *
     * @param rowData   Information used to resize row to fit all content
     * @return  Multiplier needed to fit all content
     */
    private int resizeRow(String rowData ){
        int extraRowCount = 0;
        String orders[] = rowData.split("<br>");
        for(int y = 0; y < orders.length; y ++){
            if(orders[y].length() > 95)
                extraRowCount++;
        }
        return  extraRowCount + orders.length;
    }
}
