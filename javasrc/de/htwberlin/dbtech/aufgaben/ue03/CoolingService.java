package de.htwberlin.dbtech.aufgaben.ue03;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.DataException;

public class CoolingService implements ICoolingService {
    private static final Logger L = LoggerFactory.getLogger(CoolingService.class);
    private Connection connection;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @SuppressWarnings("unused")
    private Connection useConnection() {
        if (connection == null) {
            throw new DataException("Connection not set");
        }
        return connection;
    }

    @Override
    public void transferSample(Integer sampleId, Integer diameterInCM) {
        L.info("transferSample: sampleId: " + sampleId + ", diameterInCM: " + diameterInCM);
        // TODO Auto-generated method stub

    }

}
