package au.edu.swin.ajass;

import au.edu.swin.ajass.views.MainView;

import javax.swing.*;
import java.awt.*;

/**
 *yo bro
 */
public final class Main {

    public static void main(String[] args) {
        MainView displayFrame = new MainView();
        // Set JFrame attributes
        displayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        displayFrame.setSize(1000, 700);
        displayFrame.setResizable(true);
        displayFrame.setVisible(true);
        // Set the frame to display in the center of the screen.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        displayFrame.setLocation(dim.width / 2 - displayFrame.getSize().width / 2, dim.height / 2 - displayFrame.getSize().height / 2);
    }
}
