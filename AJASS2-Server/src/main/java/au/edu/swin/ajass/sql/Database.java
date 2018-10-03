package au.edu.swin.ajass.sql;

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
     * @param user MySQL server username.
     * @param pass MySQL server password.
     * @param hostname MySQL server's hostname.
     * @param port MySQL server port.
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
}