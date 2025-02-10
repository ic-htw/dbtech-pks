package de.htwberlin.dbtech.bsp.auktion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.ServiceException;

public class AuktionsServiceDao implements IAuktionsService {
    private static final Logger L = LoggerFactory.getLogger(AuktionsServiceDao.class);

    private Connection connection = null;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection useConnection() {
        if (connection == null) {
            throw new ServiceException("Service hat keine Connection");
        }
        return connection;
    }

    private AngebotFinder angebotFinder = null;
    private Angebot angebot = null;

    @Override
    public void biete(Integer aid, BigDecimal gebotspreis) {
        L.info("aid: " + aid + "  gebotspreis: " + gebotspreis);

        angebotFinder = new AngebotFinder();
        angebotFinder.setConnection(useConnection());

        angebot = angebotFinder.findById(aid);

        if (angebot == null) {
            throw new ServiceException("AID existiert nicht: " + aid);
        }

        Timestamp aktuellerZeitpunkt = null;
        // hier Code zum Beschaffen des aktuellen Zeitpunkts

        if (angebot.getAuktionEndeZeitpunkt().before(aktuellerZeitpunkt)) {
            throw new ServiceException("Auktion bereits beendet");
        }

        if (gebotspreis.compareTo(angebot.getAktuellerPreis()) > 0) {
            throw new ServiceException("Gebotspreis zu niedrig");
        }

        Gebot gebot = new Gebot();
        gebot.setConnection(useConnection());
        Integer gid = null;
        // hier Code zum Beschaffen einer neuen gid

        gebot.setGid(gid);
        gebot.setAid(aid);
        gebot.setGebotsZeitpunkt(aktuellerZeitpunkt);
        gebot.setGebotsPreis(gebotspreis);
        gebot.insert();
    }

}
