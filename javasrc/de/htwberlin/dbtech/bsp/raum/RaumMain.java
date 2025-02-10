package de.htwberlin.dbtech.bsp.raum;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.DataException;
import de.htwberlin.dbtech.utils.DbCred;
import de.htwberlin.dbtech.utils.JdbcUtils;

public class RaumMain {
    private static final Logger L = LoggerFactory.getLogger(RaumMain.class);

    public static void main(String[] args) {
        IRaumService rs = new RaumService();

        try (Connection connection = JdbcUtils.getConnectionViaDriverManager(DbCred.url, DbCred.user, DbCred.password)) {
            rs.setConnection(connection);
            int rid = 1;
            int anzahl = rs.findAnzahlPlaetzeInRaum(rid);
            System.out.println(anzahl);
        } catch (SQLException e) {
            L.error("Verbindungsaufbau gescheitert", e);
        } catch (DataException e) {
            L.error("DataException");
        }

    }

}
