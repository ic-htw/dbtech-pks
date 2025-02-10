package de.htwberlin.dbtech.bsp.auktion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;

import de.htwberlin.dbtech.exceptions.ServiceException;

public class Angebot {
    private Integer aid;
    private String artikelBez;
    private Timestamp auktionEndeZeitpunkt;
    private BigDecimal auktionMinPreis;

    // aktuellerPreis wird aus den Geboten berechnet
    // ist nicht Splate in der Tabelle Angebot
    // wird auch nicht in insert und aupdate behandelt
    private BigDecimal aktuellerPreis;

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

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getArtikelBez() {
        return artikelBez;
    }

    public void setArtikelBez(String artikelBez) {
        this.artikelBez = artikelBez;
    }

    public Timestamp getAuktionEndeZeitpunkt() {
        return auktionEndeZeitpunkt;
    }

    public void setAuktionEndeZeitpunkt(Timestamp auktionEndeZeitpunkt) {
        this.auktionEndeZeitpunkt = auktionEndeZeitpunkt;
    }

    public BigDecimal getAuktionMinPreis() {
        return auktionMinPreis;
    }

    public void setAuktionMinPreis(BigDecimal auktionMinPreis) {
        this.auktionMinPreis = auktionMinPreis;
    }

    public BigDecimal getAktuellerPreis() {
        return aktuellerPreis;
    }

    public void insert() {
        // SQL-Code zum Speichern dieses Angebots in der Datenbank
    }

    public void update() {
        // SQL-Code zum Aendern dieses Angebots in der Datenbank
    }

    public void delete() {
        // SQL-Code zum Loeschen dieses Angebots in der Datenbank
    }
}
