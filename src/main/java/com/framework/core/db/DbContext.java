package com.framework.core.db;

import java.sql.Connection;

/**
 * Stores database resources
 * for the current test execution.
 *
 * Future:
 * - JDBC Connection
 * - Transaction metadata
 * - Query execution context
 */
public class DbContext {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Safely closes database connection.
     */
    public void closeConnection() {

        try {

            if (connection != null) {
                connection.close();
            }

        } catch (Exception ignored) {
        }
    }
}
