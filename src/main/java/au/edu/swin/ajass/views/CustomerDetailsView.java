package au.edu.swin.ajass.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by sky on 26/9/18.
 */
public class CustomerDetailsView extends JPanel {

    private JPanel detailsPanel;
    private JTextField custName, tableNum;

    public CustomerDetailsView()
    {
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 20));
        detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Customer Details", TitledBorder.LEFT, TitledBorder.TOP));

        detailsPanel.setPreferredSize(new Dimension(989, 100));

    }

    public JPanel returnPanel()
    {
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
        ButtonGroup mealButtons = new ButtonGroup();
        mealButtons.add(breakfastButton);
        mealButtons.add(lunchButton);
        mealButtons.add(dinnerButton);

        detailsPanel.add(custLabel);
        detailsPanel.add(custName);
        detailsPanel.add(tableLabel);
        detailsPanel.add(tableNum);
        detailsPanel.add(mealType);
        detailsPanel.add(breakfastButton);
        detailsPanel.add(lunchButton);
        detailsPanel.add(dinnerButton);

        return detailsPanel;
    }


}
