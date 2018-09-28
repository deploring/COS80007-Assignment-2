package au.edu.swin.ajass.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by sky on 26/9/18.
 */
public class MainView extends JFrame {

    JPanel choiceNutritionInfo, chooseMenuItems, customerDetails, orderedItems, orderStatus, holderPanel, buttonPanel;

    public MainView()
    {
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 10));

        CreateUI();
    }

    public void CreateUI()
    {
        CustomerDetailsView cDV = new CustomerDetailsView();
        customerDetails = cDV.returnPanel();
        ChooseMenuItemsView cMIV = new ChooseMenuItemsView();
        chooseMenuItems = cMIV.returnPanel();
        OrderStatusView oSV = new OrderStatusView();
        orderStatus = oSV.returnPanel();
        holderPanel = new JPanel();
        holderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        holderPanel.setPreferredSize(new Dimension(989, 150));
        ButtonView bV = new ButtonView();
        buttonPanel = bV.returnPanel();

        OrderedItemsView oIV = new OrderedItemsView(1);
        ChoiceNutritionInfoView cIV = new ChoiceNutritionInfoView();
        holderPanel = oIV.returnPanel(new String[][]{});


        add(customerDetails);
        add(chooseMenuItems);
        add(orderStatus);
        add(holderPanel);
        add(buttonPanel);
    }

}
