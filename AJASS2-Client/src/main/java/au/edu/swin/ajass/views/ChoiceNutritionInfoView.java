package au.edu.swin.ajass.views;

import au.edu.swin.ajass.Utilities;
import au.edu.swin.ajass.models.MenuItem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

/**
 * This view is an intermittently-displayed view, which shows the
 * user's selected items' nutritional information.
 *
 * @author Keagan Foster
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public class ChoiceNutritionInfoView implements IView {

    // Reference to MainView
    private MainView main;

    // View Elements
    private JPanel nutritionPanel;
    private JTable nutritionTable;
    private DefaultTableModel model;

    ChoiceNutritionInfoView(MainView main) {
        this.main = main;

        // Create border outline.
        nutritionPanel = new JPanel();
        nutritionPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));
        nutritionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                "Menu Choices and Nutrition Information", TitledBorder.LEFT, TitledBorder.TOP));
        nutritionPanel.setPreferredSize(new Dimension(989, 150));

        // Generate View elements.
        generate();
    }

    /**
     * Populates the JTable with properties of MenuItem.
     *
     * @param food  Selected food, if any.
     * @param drink Selected beverage, if any.
     */
    void populateTable(MenuItem food, MenuItem drink) {
        reset();

        // Calculate number of rows to display based on what data has been entered.
        int numberOfRows = (food != null ? 1 : 0) + (drink != null ? 1 : 0) + 2;

        // Create new list of data to enter.
        String[][] itemData = new String[numberOfRows][];

        // Populate with food information.
        int pos = 0;
        if (food != null) {
            itemData[pos] = convertMenuItem(food);
            pos++;
        }

        // Populate with drink information.
        if (drink != null) {
            itemData[pos] = convertMenuItem(drink);
            pos++;
        }

        // Empty row.
        itemData[pos] = convertMenuItem(null);
        pos++;

        // Populate with totals information.
        itemData[pos] = convertMenuItem(Utilities.totalMenuItems(food, drink));

        model.addColumn("Item Name");
        model.addColumn("Energy (kJ)");
        model.addColumn("Protein (g)");
        model.addColumn("Carbohydrates (g)");
        model.addColumn("Total Fat (g)");
        model.addColumn("Fibre (g)");
        model.addColumn("Price ($AUD)");

        nutritionTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        nutritionTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        // Add rows to the table model.
        /* STREAM */
        Arrays.stream(itemData).forEach(model::addRow);
    }

    /**
     * @param toConvert The MenuItem to convert.
     * @return MenuItem in an acceptable array format to display in a table model.
     * @see #populateTable(MenuItem, MenuItem)
     */
    private String[] convertMenuItem(MenuItem toConvert) {
        // Passing in null returns an empty row for the table.
        if (toConvert == null) return new String[]{"", "", "", "", "", "", ""};
        else
            return new String[]{toConvert.getItemName(), String.format("%.2f", toConvert.getEnergy()), String.format("%.2f", toConvert.getProtein()), String.format("%.2f", toConvert.getCarbs()), String.format("%.2f", toConvert.getFat()), String.format("%.2f", toConvert.getFibre()), String.format("$%.0f",toConvert.getPrice())};
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void generate() {
        model = new DefaultTableModel();
        nutritionTable = new JTable(model);
        nutritionTable.setBackground(Color.WHITE);
        JScrollPane holder = new JScrollPane(nutritionTable);
        holder.setPreferredSize(new Dimension(950, 110));

        nutritionPanel.add(holder);
    }

    @Override
    public void reset() {
        // Remove old values from table model.
        model.setRowCount(0);
        model.setColumnCount(0);
    }

    @Override
    public JPanel getPanel() {
        return nutritionPanel;
    }
}
