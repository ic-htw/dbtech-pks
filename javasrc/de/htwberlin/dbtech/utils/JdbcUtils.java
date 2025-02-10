package de.htwberlin.dbtech.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcUtils {
    private static final Logger L = LoggerFactory.getLogger(JdbcUtils.class);

    public static void loadDriver(final String driver) {
        try {
            Class.forName(driver);
            L.info("driver <" + driver + "> loaded");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnectionViaDriverManager(final String url, final String user, final String passwd) {
        try {
            Connection connection = DriverManager.getConnection(url, user, passwd);
            L.info("connection got");
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnectionViaDataSource(final DataSource ds) {
        try {
            Connection connection = ds.getConnection();
            L.info("connection got");
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(final Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                L.info("connection closed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnectionQuietly(final Connection connection) {
        try {
            closeConnection(connection);
        } catch (Exception e) {
            // ignore exception, just log
            L.error("unhandled", e);
        }
    }

    public static void closeResultSet(final ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
                L.info("result set closed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeResultSetQuietly(final ResultSet resultSet) {
        try {
            closeResultSet(resultSet);
        } catch (RuntimeException e) {
            // ignore exception, just log
            L.error("unhandled", e);
        }
    }

    public static void closeStatement(final Statement statement) {
        try {
            if (statement != null) {
                statement.close();
                L.info("statement closed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeStatementQuietly(final Statement statement) {
        try {
            closeStatement(statement);
        } catch (RuntimeException e) {
            // ignore exception, just log
            L.error("unhandled", e);
        }
    }

}
