package de.htwberlin.dbtech.utils;

import org.dbunit.database.IDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUnitUtils {
    private static final Logger L = LoggerFactory.getLogger(DbUnitUtils.class);

    public static void closeDbUnitConnection(final IDatabaseConnection connection) {
        try {
            if (connection != null) {
                connection.close();
                L.info("connection closed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeDbUnitConnectionQuietly(final IDatabaseConnection connection) {
        try {
            closeDbUnitConnection(connection);
        } catch (Exception e) {
            // ignore exception, just log
            L.error("unhandled", e);
        }
    }

}
