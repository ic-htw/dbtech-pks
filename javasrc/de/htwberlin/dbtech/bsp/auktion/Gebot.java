package de.htwberlin.dbtech.bsp.auktion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;

import de.htwberlin.dbtech.exceptions.ServiceException;

public class Gebot {
    private Integer gid;
    private Integer aid;
    private Timestamp gebotsZeitpunkt;
    private BigDecimal gebotsPreis;

    private Connection connection = null;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @SuppressWarnings("unused")
    private Connection useConnection() {
        if (connection == null) {
            throw new ServiceException("Service hat keine Connection");
        }
        return connection;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Timestamp getGebotsZeitpunkt() {
        return gebotsZeitpunkt;
    }

    public void setGebotsZeitpunkt(Timestamp gebotsZeitpunkt) {
        this.gebotsZeitpunkt = gebotsZeitpunkt;
    }

    public BigDecimal getGebotsPreis() {
        return gebotsPreis;
    }

    public void setGebotsPreis(BigDecimal gebotsPreis) {
        this.gebotsPreis = gebotsPreis;
    }

    public void insert() {
        // SQL-Code zum Speichern dieses Gebots in der Datenbank
    }

    public void update() {
        // SQL-Code zum Aendern dieses Gebots in der Datenbank
    }

    public void delete() {
        // SQL-Code zum Loeschen dieses Gebots in der Datenbank
    }
}
