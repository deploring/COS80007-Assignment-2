package au.edu.swin.ajass.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Created by sky on 26/9/18.
 */
public class ChoiceNutritionInfoView extends JPanel {

    private JPanel nutritionPanel;
    private JTable nutritionTable;
    private DefaultTableModel model;

    public ChoiceNutritionInfoView()
    {
        nutritionPanel = new JPanel();
        nutritionPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));
        nutritionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Menu Choices and Nutrition Information", TitledBorder.LEFT, TitledBorder.TOP));
        nutritionPanel.setPreferredSize(new Dimension(989, 150));
    }

    public JPanel returnPanel(String[][] data)
    {
        model = new DefaultTableModel();
        nutritionTable = new JTable(model);
        nutritionTable.setBackground(Color.white);
        populateTable();
        JScrollPane holder = new JScrollPane(nutritionTable);
        holder.setPreferredSize(new Dimension(950, 110));

        TableColumn nameColumn = nutritionTable.getColumnModel().getColumn(0);
        nameColumn.setPreferredWidth(300);
        TableColumn totalFatColumn = nutritionTable.getColumnModel().getColumn(4);
        totalFatColumn.setPreferredWidth(100);

        nutritionPanel.add(holder);


        return nutritionPanel;
    }

    private void populateTable()
    {
        String[][] itemData = {{"Sandwich chicken & salad","653","10.4","18.0","4.0","2.6","7.0"}};

        model.addColumn("Item Name");
        model.addColumn("Energy");
        model.addColumn("Protein");
        model.addColumn("Carbohydrates");
        model.addColumn("Total Fat");
        model.addColumn("Fibre");
        model.addColumn("Price");

        for(int i = 0; i < itemData.length; i++){
            model.addRow(new Object[]{itemData[i][0], itemData[i][1], itemData[i][2], itemData[i][3],
                                        itemData[i][4], itemData[i][5], itemData[i][6]});
        }
    }
}
