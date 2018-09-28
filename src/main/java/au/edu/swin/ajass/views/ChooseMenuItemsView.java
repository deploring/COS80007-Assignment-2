package au.edu.swin.ajass.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by sky on 26/9/18.
 */
public class ChooseMenuItemsView extends JPanel {

    private JPanel menuPanel;
    private JComboBox foodList, beverageList;

    public ChooseMenuItemsView()
    {
        menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 20));
        menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Choose Menu Items", TitledBorder.LEFT, TitledBorder.TOP));

        menuPanel.setPreferredSize(new Dimension(989, 100));
    }

    public JPanel returnPanel()
    {
        JLabel foodLabel, beverageLabel;
        Font panelFont = new Font("Serif", Font.PLAIN, 15);

        foodLabel = new JLabel("Food");
        foodLabel.setFont(panelFont);
        beverageLabel = new JLabel("Beverage");
        beverageLabel.setFont(panelFont);

        foodList = new JComboBox();
        String[] foodData = new String[]{"test1", "test2", "test3"};
        fillList(foodList, foodData);
        foodList.setFont(panelFont);
        foodList.setPreferredSize(new Dimension(300, 20));
        beverageList = new JComboBox();
        String[] beverageData = new String[]{"test1", "test2", "test3"};
        fillList(beverageList, beverageData);
        beverageList.setFont(panelFont);
        beverageList.setPreferredSize(new Dimension(300,20));

        menuPanel.add(foodLabel);
        menuPanel.add(foodList);
        menuPanel.add(beverageLabel);
        menuPanel.add(beverageList);

        return menuPanel;
    }

    private void fillList(JComboBox list, String[] data)
    {
        for(int i = 0; i < data.length; i++)
        {
            list.addItem(data[i]);
        }
    }
}
