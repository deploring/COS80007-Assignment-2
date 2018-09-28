package au.edu.swin.ajass.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Vector;

/**
 * Created by sky on 26/9/18.
 */
public class OrderedItemsView extends JPanel {

    private JPanel itemsPanel;
    private JTable itemsTable;
    private DefaultTableModel model;

    public OrderedItemsView(int tableNum)
    {
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));
        itemsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Ordered Items at Table " + tableNum, TitledBorder.LEFT, TitledBorder.TOP));
        itemsPanel.setPreferredSize(new Dimension(989, 150));
    }

    public JPanel returnPanel(String[][] data)
    {
        model = new DefaultTableModel();
        itemsTable = new JTable(model);
        itemsTable.setBackground(Color.white);
        populateTable();
        JScrollPane holder = new JScrollPane(itemsTable);
        holder.setPreferredSize(new Dimension(950, 110));

        TableColumn column = itemsTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(400);

        itemsPanel.add(holder);


        return itemsPanel;
    }

    private void populateTable()
    {
        String[][] orderData = {{"Bradley" , "Sushi, V-Energy"},
                {"Keagan" , "Fries, Ice tea"},
                {"Josh" , "Shitty licorice, V-Energy"}};

        model.addColumn("Customer Name");
        model.addColumn("Ordered Items");

        for(int i = 0; i < orderData.length; i++){
            model.addRow(new Object[]{orderData[i][0], orderData[i][1]});
        }
    }
}
