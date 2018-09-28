package au.edu.swin.ajass.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ButtonView extends JPanel {

    private JPanel buttonPanel;
    private JButton enterButton, displayChoicesButton, displayOrderButton, prepareButton, billButton, clearButton, quitButton;

    public ButtonView()
    {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 13, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Command Buttons", TitledBorder.LEFT, TitledBorder.TOP));

        buttonPanel.setPreferredSize(new Dimension(989, 80));
    }

    public JPanel returnPanel()
    {
        enterButton = new JButton("Enter Data");
        displayChoicesButton = new JButton("Display Choices");
        displayOrderButton = new JButton("Display Order");
        prepareButton = new JButton("Prepare");
        prepareButton.setEnabled(false);
        billButton = new JButton("Bill");
        billButton.setEnabled(false);
        clearButton = new JButton("Clear Display");
        quitButton = new JButton("Quit");

        JButton[] buttons = new JButton[]{enterButton, displayChoicesButton, displayOrderButton, prepareButton, billButton, clearButton, quitButton};

        for(int i = 0; i < buttons.length; i++){
            buttons[i].setPreferredSize(new Dimension(125, 30));
            buttons[i].setFont(new Font("Serif", Font.BOLD, 13));
        }

        buttonPanel.add(enterButton);
        buttonPanel.add(displayChoicesButton);
        buttonPanel.add(displayOrderButton);
        buttonPanel.add(prepareButton);
        buttonPanel.add(billButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        return buttonPanel;
    }
}
