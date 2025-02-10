package de.htwberlin.dbtech.bsp.auktion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.DataException;
import de.htwberlin.dbtech.exceptions.ServiceException;

public class AuktionsService implements IAuktionsService {
    private static final Logger L = LoggerFactory.getLogger(AuktionsService.class);
    private Connection connection = null;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection useConnection() {
        if (connection == null) {
            throw new ServiceException("Service hat keine Connection");
        }
        return connection;
    }

    @Override
    public void biete(Integer aid, BigDecimal gebotspreis) {
        L.info("aid: " + aid + "  gebotspreis: " + gebotspreis);
        if (!existiertAidInDb(aid)) {
            throw new ServiceException("AID existiert nicht: " + aid);
        }
        if (!istAuktionNochOffen(aid)) {
            throw new ServiceException("Auktion bereits beendet");
        }
        if (!istGebotspreisOk(aid, gebotspreis)) {
            throw new ServiceException("Gebotspreis zu niedrig");
        }

        speichereGebot(aid, gebotspreis);
    }

    private boolean existiertAidInDb(Integer aid) {
        L.info("aid: " + aid);
        String sql = "select AID from Angebot where AID=?";
        L.info(sql);
        try (PreparedStatement ps = useConnection().prepareStatement(sql)) {
            ps.setInt(1, aid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
    }

    private boolean istGebotspreisOk(Integer aid, BigDecimal gebotspreis) {
        L.info("aid: " + aid + "  gebotspreis: " + gebotspreis);
        String sql = "select count(GebotsPreis) as anzahlGebote, " +
                "  coalesce(max(GebotsPreis), max(AuktionMinPreis)) as aktuellerPreis " +
                "from Angebot a left join Gebot g on a.aid=g.aid " +
                "where a.aid = ?";
        L.info(sql);
        try (PreparedStatement ps = useConnection().prepareStatement(sql)) {
            ps.setInt(1, aid);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int anzahlGebote = rs.getInt("anzahlGebote");
                BigDecimal aktuellerPreis = rs.getBigDecimal("aktuellerPreis");
                L.info("anzahlGebote: " + anzahlGebote);
                L.info("aktuellerPreis: " + aktuellerPreis);
                if (anzahlGebote == 0) {
                    // keine Gebote bedeutet "gebotspreis" muss >= AuktionMinPreis sein
                    return gebotspreis.compareTo(aktuellerPreis) >= 0;
                } else {
                    // Gebote vorhanden, dann muss "gebotspreis" echt groesser als der bisher hoechste
                    // Gebotspreis sein
                    return gebotspreis.compareTo(aktuellerPreis) > 0;
                }
            }
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
    }

    private boolean istAuktionNochOffen(Integer aid) {
        String sql = "select AuktionEndeZeitpunkt,  current_timestamp as AktuellerZeitpunkt from Angebot where AID=?";
        try (PreparedStatement ps = useConnection().prepareStatement(sql)) {
            ps.setInt(1, aid);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                Timestamp auktionEndeZeitpunkt = rs.getTimestamp("AuktionEndeZeitpunkt");
                Timestamp aktuellerZeitpunkt = rs.getTimestamp("AktuellerZeitpunkt");
                return aktuellerZeitpunkt.before(auktionEndeZeitpunkt) || aktuellerZeitpunkt.equals(auktionEndeZeitpunkt);
            }
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
    }

    private void speichereGebot(Integer aid, BigDecimal gebotspreis) {
        String sql = "insert into Gebot values " + "(?,?, current_timestamp,?)";
        Integer neueGid = neueGid();
        try (PreparedStatement ps = useConnection().prepareStatement(sql)) {
            ps.setInt(1, neueGid);
            ps.setInt(2, aid);
            ps.setBigDecimal(3, gebotspreis);
            ps.executeUpdate();
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
    }

    private Integer neueGid() {
        String sql = "select max(gid) as maxgid from gebot";
        int neueGid = 1;
        try (Statement stmt = useConnection().createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                rs.next();
                int maxGid = rs.getInt("maxgid");
                if (!rs.wasNull()) {
                    neueGid = maxGid + 1;
                }
            }
            return neueGid;
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
    }
}
