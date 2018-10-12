package au.edu.swin.ajass.sql;

import au.edu.swin.ajass.controllers.ServerMenuController;
import au.edu.swin.ajass.enums.MealType;
import au.edu.swin.ajass.enums.MenuItemType;
import au.edu.swin.ajass.enums.OrderState;
import au.edu.swin.ajass.models.MenuItem;
import au.edu.swin.ajass.models.MenuItems;

import java.sql.*;

/**
 * Simple yet direct Database model that uses the JDBC driver.
 *
 * @author Joshua skinner
 * @version 1
 * @since 0.1
 */
public class Database {

    // Database fields.
    private final String user, pass, url;
    private Connection connection;

    /**
     * @param user     MySQL server username.
     * @param pass     MySQL server password.
     * @param hostname MySQL server's hostname.
     * @param port     MySQL server port.
     * @param database Name of schema/database on MySQL server.
     */
    public Database(String user, String pass, String hostname, String port, String database) {
        // Create database settings.
        this.user = user;
        this.pass = pass;
        this.url = String.format("jdbc:mysql://%s:%s/%s", hostname, port, database);

        // Open the connection.
        connection = open();

        // Check if the connection is valid.
        if (connection == null)
            throw new IllegalStateException("Unable to connect to database!");
    }

    /**
     * Double checks that the JDBC driver is installed.
     *
     * @throws IllegalStateException If the JDBC driver is not installed.
     */
    private void initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // This will never happen in normal operation, as we include the mysql-connector library with the server JAR.
            throw new IllegalStateException("JDBC driver not found?");
        }
    }

    /**
     * Checks if the MySQL connection is still alive.
     * Re-opens the connection if it is no longer alive.
     */
    private void validate() {
        try {
            if (!connection.isValid(0))
                connection = open();
        } catch (SQLException e) {
            // Impossible.
        }
    }

    /**
     * Attempt to open a connection to the MySQL database.
     *
     * @return Instance of Connection to MySQL database.
     */
    private Connection open() {
        initialize();
        try {
            if (connection == null)
                return DriverManager.getConnection(this.url, this.user, this.pass);
            else if (connection.isValid(3)) // Check the connection is valid
                return connection;
            else {
                // Return a new connection!
                return DriverManager.getConnection(this.url, this.user, this.pass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Print the stack trace as this should never happen in normal operation.
            throw new IllegalStateException("Could not establish MySQL connection");
        }
    }

    /**
     * Closes the MySQL connection. Does not attempt to re-open.
     * Unused.
     */
    public void close() {
        // Only close the connection if there is a connection to close.
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // This shouldn't happen, so just print the stack trace.
            }
            connection = null;
        }
    }

    /**
     * Creates an injection-safe MySQL Prepared Statement.
     *
     * @param query The MySQL query.
     * @return The Prepared Statement.
     * @see PreparedStatement
     */
    @SuppressWarnings("WeakerAccess")
    public PreparedStatement prepare(String query) {
        // Validate connection first.
        validate();
        try {
            return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
            // Nothing bad should happen in normal operation, so just print the stack trace.
        }
        return null;
    }

    /* Below this line are actual uses of the database. */

    /**
     * Creates the required schema tables if they do not exist.
     */
    public void createTables() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();

        // Check if the `menuItems` table exists, and then create it if not.
        ResultSet check1 = meta.getTables(null, null, "menuItems", null);
        if (!check1.next()) {
            System.out.println(">>> Creating `menuItems` table...");
            connection.prepareStatement(
                    "CREATE TABLE `menuItems` (" +
                            "`PLU` SMALLINT UNSIGNED NOT NULL," +
                            "`food_type` ENUM ('FOOD', 'BEVERAGE')," +
                            "`meal_type` ENUM ('BREAKFAST', 'LUNCH', 'DINNER')," +
                            "`item_name` VARCHAR(255) NOT NULL," +
                            "`price` DOUBLE NOT NULL," +
                            "`energy` DOUBLE NOT NULL," +
                            "`protein` DOUBLE NOT NULL," +
                            "`carbs` DOUBLE NOT NULL," +
                            "`fat` DOUBLE NOT NULL," +
                            "`fibre` DOUBLE NOT NULL," +
                            "PRIMARY KEY (`PLU`));"
            ).executeUpdate();

            // Create the placeholder menu item for a 'null' item.
            prepare("INSERT INTO `menuItems` (`PLU`, `food_type`, `meal_type`, `item_name`, `price`, `energy`, `protein`, `carbs`, `fat`, `fibre`) VALUES (0,NULL,NULL,'',0,0,0,0,0,0)").executeUpdate();
        }
        check1.close();

        // Check if the `orders` table exists, and then create it if not.
        ResultSet check2 = meta.getTables(null, null, "orders", null);
        if (!check2.next()) {
            System.out.println(">>> Creating `orders` table...");
            connection.prepareStatement(
                    "CREATE TABLE `orders` (" +
                            "`order_id` INT NOT NULL AUTO_INCREMENT," +
                            "`table_number` TINYINT UNSIGNED NOT NULL," +
                            "`order_status` ENUM ('WAITING', 'SERVED', 'BILLED') NOT NULL," +
                            "`customer_name` VARCHAR(255) NOT NULL," +
                            "`food` SMALLINT UNSIGNED NOT NULL," +
                            "`beverage` SMALLINT UNSIGNED NOT NULL," +
                            "PRIMARY KEY (`order_id`)," +
                            "FOREIGN KEY (`food`) REFERENCES menuItems(`PLU`)," +
                            "FOREIGN KEY (`beverage`) REFERENCES menuItems(`PLU`));"
            ).executeUpdate();
        }
        check2.close();
    }

    /**
     * Loads existing menu items from the database into the program first.
     * MenuItems then loads any additional items in the .csv file that may
     * not exist in the database.
     * <p>
     * Note: This must be done synchronously, as the program is starting up
     * and relies on the pre-existing menu items to be loaded in first.
     */
    public void loadMenuItems(MenuItems menuItems) throws SQLException {
        ResultSet items = prepare("SELECT * FROM `menuItems`").executeQuery();
        while (items.next()) {
            // Skip if the PLU is zero, as this is just a placeholder and serves no purpose outside the DB.
            int PLU = items.getInt("PLU");
            if (PLU == 0) continue;

            // Retrieve all of the menu item information and re-construct it.
            MenuItemType foodType = MenuItemType.valueOf(items.getString("food_type"));
            MealType mealType = MealType.valueOf(items.getString("meal_type"));
            String itemName = items.getString("item_name");
            double price = items.getDouble("price");
            double energy = items.getDouble("energy");
            double protein = items.getDouble("protein");
            double carbs = items.getDouble("carbs");
            double fat = items.getDouble("fat");
            double fibre = items.getDouble("fibre");

            menuItems.placeItem(foodType, mealType, new MenuItem(itemName, price, energy, protein, carbs, fat, fibre, PLU));
        }
        items.close();
    }

    /**
     * Loads existing orders from the database into the program first.
     * This is necessary because the server may turn off with existing
     * orders and this loads them back in after restarting.
     * <p>
     * Note: This must be done synchronously, as the program is starting
     * up and relies on the orders to be loaded in first.
     *
     * @param menu A reference to the menu controller is necessary.
     */
    public void loadOrders(ServerMenuController menu) throws SQLException {
        // This SQL query selects non-billed orders, but also joins on the items' food type and meal type, if applicable.
        ResultSet orders = prepare("SELECT o1.*, m1.*, m2.* FROM `orders` o1 " +
                "JOIN (SELECT `PLU`, `food_type` as `food_foodtype`, `meal_type` as `food_mealtype` FROM `menuItems`) m1 " +
                "ON m1.`PLU` = o1.`food` " +
                "JOIN (SELECT `PLU`, `food_type` as `beverage_foodtype`, `meal_type` as `beverage_mealtype` FROM `menuItems`) m2 " +
                "ON m2.`PLU` = o1.`beverage` " +
                "WHERE o1.`order_status` <> 'BILLED'").executeQuery();

        while (orders.next()) {
            // Retrieve all of the menu item information and re-construct it.
            OrderState state = OrderState.valueOf(orders.getString("order_status"));
            String custName = orders.getString("customer_name");
            int tableNum = orders.getInt("table_number");
            int orderID = orders.getInt("order_id");

            // Create food menu item from PLU number if it isn't 0 (null).
            MenuItem food = null;
            int foodPLU = (orders.getInt("food"));
            if (foodPLU != 0) {
                MenuItemType foodFoodType = MenuItemType.valueOf(orders.getString("food_foodtype"));
                MealType foodMealType = MealType.valueOf(orders.getString("food_mealtype"));
                food = menu.getMenuItems().getByPLU(foodFoodType, foodMealType, foodPLU);
            }

            // Create beverage menu item from PLU number if it isn't 0 (null).
            MenuItem beverage = null;
            int beveragePLU = (orders.getInt("beverage"));
            if (beveragePLU != 0) {
                MenuItemType beverageFoodType = MenuItemType.valueOf(orders.getString("beverage_foodtype"));
                MealType beverageMealType = MealType.valueOf(orders.getString("beverage_mealtype"));
                beverage = menu.getMenuItems().getByPLU(beverageFoodType, beverageMealType, beveragePLU);
            }

            // Adds the order. Doesn't distribute it, however.
            menu.addOrder(tableNum, orderID, custName, state, food, beverage, false);
        }
        orders.close();
    }

    /**
     * Records an order into the database.
     *
     * @param tableNumber  The table number the order was placed at.
     * @param customerName The customer's name.
     * @param food         The food ordered, if any.
     * @param beverage     The beverage ordered, if any.
     * @return The ID number for the order.
     */
    public int newOrder(int tableNumber, String customerName, MenuItem food, MenuItem beverage) {
        try {
            PreparedStatement pstmt = prepare("INSERT INTO `orders` (`table_number`, `order_status`, `customer_name`, `food`, `beverage`) VALUES (?, ?, ?, ?, ?)");
            pstmt.setInt(1, tableNumber);
            pstmt.setString(2, OrderState.WAITING.toString());
            pstmt.setString(3, customerName);
            pstmt.setInt(4, food == null ? -1 : food.getPLU());
            pstmt.setInt(5, beverage == null ? -1 : beverage.getPLU());
            pstmt.executeUpdate();

            // Get the generated order ID and return that. We need it!
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int result = rs.getInt(1);
            rs.close();
            pstmt.close();

            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(String.format("!! Error inserting menu item: %s: %s", e.getClass().getTypeName(), e.getMessage()));
        }
    }

    /**
     * Updates an existing order's status. Since this is an SQL statement,
     * all we need is the order's ID to update the state. Woohoo!
     *
     * @param orderID  The unique ID of the order.
     * @param newState The state of the order.
     * @throws SQLException The query may be invalid.
     */
    public void updateOrder(int orderID, OrderState newState) throws SQLException {
        PreparedStatement pstmt = prepare("UPDATE `orders` SET `order_status`=? WHERE `order_id`=?");
        pstmt.setString(1, newState.toString());
        pstmt.setInt(2, orderID);
        pstmt.executeUpdate();
        pstmt.close();
    }

    /**
     * Records a non-existent menu-item.
     *
     * @param foodType The menu item's food type.
     * @param mealType The menu item's meal type.
     * @param menuItem The menu item.
     */
    public void newMenuItem(MenuItemType foodType, MealType mealType, MenuItem menuItem) {
        try {
            PreparedStatement pstmt = prepare("INSERT INTO `menuItems` (`PLU`, `food_type`, `meal_type`, `item_name`, `price`, `energy`, `protein`, `carbs`, `fat`, `fibre`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, menuItem.getPLU());
            pstmt.setString(2, foodType.toString());
            pstmt.setString(3, mealType.toString());
            pstmt.setString(4, menuItem.getItemName());
            pstmt.setDouble(5, menuItem.getPrice());
            pstmt.setDouble(6, menuItem.getEnergy());
            pstmt.setDouble(7, menuItem.getProtein());
            pstmt.setDouble(8, menuItem.getCarbs());
            pstmt.setDouble(9, menuItem.getFat());
            pstmt.setDouble(10, menuItem.getFibre());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(String.format("!! Error inserting menu item: %s: %s", e.getClass().getTypeName(), e.getMessage()));
        }
    }
}