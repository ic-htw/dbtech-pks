package de.htwberlin.dbtech.bsp.intro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.bsp.raum.Raum;
import de.htwberlin.dbtech.exceptions.DataException;
import de.htwberlin.dbtech.exceptions.RaumException;
import de.htwberlin.dbtech.utils.DbCred;

public class Jdbc {
    private static final Logger L = LoggerFactory.getLogger(Jdbc.class);

    public static void main(String[] args) {
        L.info("Start");

        try (Connection c = DriverManager.getConnection(DbCred.url, DbCred.user, DbCred.password)) {
            L.info("Verbindungsaufbau erfolgreich");
            // Aufruf der eigentlichen Anwendungsfunktion
            selectAll(c);
            // selectOne(c, 1);
            // insert(c);
            // delete(c);
        } catch (SQLException e) {
            L.error("Verbindungsaufbau gescheitert", e);
        } catch (DataException e) {
            L.error("DataException");
        }
        L.info("Ende");
    }

    static void selectAll(Connection c) {
        L.info("Start selectAll");
        String sql = "select RID, RaumNr from Raum";
        L.info(sql);
        try (Statement stmt = c.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Integer rid = rs.getInt("RID");
                    String raumNr = rs.getString("RaumNr");
                    System.out.printf("|%3d|%6s|%n", rid, raumNr);
                }
            }
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
        L.info("Ende selectAll");
    }

    static void selectOne(Connection c, int rid) {
        L.info("Start selectOne");
        String sql = String.join(" ",
                "select RID, RaumNr",
                "from Raum",
                "where rid=?");
        L.info(sql);
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, rid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String raumNr = rs.getString("RaumNr");
                    System.out.printf("|%3d|%6s|%n", rid, raumNr);
                } else {
                    throw new RaumException("rid existiert nicht in db: " + rid);
                }
            }
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
        L.info("Ende selectOne");
    }

    static void insert(Connection c) {
        L.info("Start insert");
        String sql = String.join(" ",
                "insert into raum(rid, raumnr, anzahlsitze)",
                "values (7, 'A030', 10)");
        L.info(sql);
        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
        L.info("Ende insert");
    }

    static void delete(Connection c) {
        L.info("Start delete");
        String sql = "delete from raum where RID = 7";
        L.info(sql);
        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
        L.info("Ende delete");
    }

    static List<String> createList(Connection c) {
        L.info("Start createList");
        List<String> l = new LinkedList<>();
        String sql = "select * from raum";
        L.info(sql);
        try (Statement stmt = c.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    l.add(rs.getString("RaumNr"));
                }
            }
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
        L.info(l.toString());
        L.info("Ende createList");
        return l;
    }

    static Raum createObject(Connection c, int rid) {
        L.info("Start createObject");
        String sql = String.join(" ",
                "select RID, RaumNr, AnzahlSitze",
                "from Raum ",
                "where rid=?");
        L.info(sql);
        Raum r;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, rid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String raumNr = rs.getString("RaumNr");
                    int anzahlSitze = rs.getInt("AnzahlSitze");
                    r = new Raum(rid, raumNr, anzahlSitze);
                } else {
                    throw new RaumException("rid existiert nicht in db: " + rid);
                }
            }
        } catch (SQLException e) {
            L.error("", e);
            throw new DataException(e);
        }
        L.info(r.toString());
        L.info("Ende createObject");
        return r;
    }

}
