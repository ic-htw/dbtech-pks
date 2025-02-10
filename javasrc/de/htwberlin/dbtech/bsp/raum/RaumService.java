package de.htwberlin.dbtech.bsp.raum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.DataException;
import de.htwberlin.dbtech.exceptions.RaumException;

public class RaumService implements IRaumService {
    private static final Logger L = LoggerFactory.getLogger(RaumService.class);
    private Connection connection;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
        L.debug("connection set");
    }

    protected Connection useConnection() {
        if (connection != null) {
            return this.connection;
        } else {
            throw new RuntimeException("Connection not existing");
        }
    }

    @Override
    public Integer findAnzahlPlaetzeInRaum(int rid) {
        String sql = "select AnzahlSitze from Raum where RID=?";
        L.info(sql);
        try (PreparedStatement ps = useConnection().prepareStatement(sql)) {
            ps.setInt(1, rid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("AnzahlSitze");
                } else {
                    throw new RaumException("rid doesn't exist in db: " + rid);
                }
            }
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
    }

}
