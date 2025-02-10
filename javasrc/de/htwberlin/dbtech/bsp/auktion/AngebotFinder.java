package de.htwberlin.dbtech.bsp.auktion;

import java.sql.Connection;

import de.htwberlin.dbtech.exceptions.ServiceException;

public class AngebotFinder {
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

    public Angebot findById(Integer aid) {
        Angebot a = new Angebot();
        a.setConnection(useConnection());
        // Sql-Code zum Befuellen des Abgebotsobjekts
        return a;
    }

}
