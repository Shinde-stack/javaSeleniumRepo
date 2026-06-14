package com.framework.core.context;

import java.sql.Connection;

/**
 * Holds DB connection per test execution.
 */
public class DbContext {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
