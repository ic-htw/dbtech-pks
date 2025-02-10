package de.htwberlin.dbtech.bsp.auktion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.utils.DbCred;
import de.htwberlin.dbtech.utils.JdbcUtils;

public class AuktionMain {
    private static final Logger L = LoggerFactory.getLogger(AuktionMain.class);

    public static void main(String[] args) {
        IAuktionsService as = new AuktionsService();

        try (Connection connection = JdbcUtils.getConnectionViaDriverManager(DbCred.url, DbCred.user, DbCred.password)) {
            as.setConnection(connection);
            Integer aid = 1;
            BigDecimal angebotspreis = BigDecimal.valueOf(2);
            as.biete(aid, angebotspreis);
        } catch (SQLException e) {
            L.error("Verbindungsaufbau gescheitert", e);
        }

    }

}
