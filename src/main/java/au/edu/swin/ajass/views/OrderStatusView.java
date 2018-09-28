package au.edu.swin.ajass.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by sky on 26/9/18.
 */
public class OrderStatusView extends JPanel {

    private JPanel statusPanel;

    public OrderStatusView()
    {
        statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 20));
        statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Order Status", TitledBorder.LEFT, TitledBorder.TOP));
        statusPanel.setPreferredSize(new Dimension(989, 200));
    }

    public JPanel returnPanel()
    {
        JLabel waitingLabel, servedLabel;
        JTable waitingTable, servedTable;
        Font panelFont = new Font("Serif", Font.PLAIN, 15);

        waitingLabel = new JLabel("Orders with waiting state", SwingConstants.CENTER);
        waitingLabel.setFont(panelFont);
        waitingLabel.setPreferredSize(new Dimension(450, 20));
        servedLabel = new JLabel("Orders with served state(No orders available to serve)", SwingConstants.CENTER);
        servedLabel.setFont(panelFont);
        servedLabel.setPreferredSize(new Dimension(450, 20));

        waitingTable = createTable();
        servedTable = createTable();

        statusPanel.add(waitingLabel);
        statusPanel.add(servedLabel);
        statusPanel.add(waitingTable);
        statusPanel.add(servedTable);

        return statusPanel;
    }

    private JTable createTable()
    {
        JTable table;
        String[][] testData = {{"Bradley | Table: 1 | Sushi | V-Energy"},
                {"Keagan | Table: 1 | Fries | Ice tea"},
                {"Josh | Tabel: 1 | Shitty licorice | V-Energy"}};
        table = new JTable(testData, new String[]{""});
        table.setFont(new Font("Serif", Font.PLAIN, 15));
        table.setPreferredSize(new Dimension(450, 100));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setDefaultEditor(Object.class, null);
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        return table;
    }

}
