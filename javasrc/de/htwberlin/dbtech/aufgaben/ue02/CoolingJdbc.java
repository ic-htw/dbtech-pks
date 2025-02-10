package de.htwberlin.dbtech.aufgaben.ue02;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.DataException;

public class CoolingJdbc implements ICoolingJdbc {

    private static final Logger L = LoggerFactory.getLogger(CoolingJdbc.class);
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
    public List<String> getSampleKinds() {
        L.info("getSampleKinds: start");
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Sample findSampleById(Integer sampleId) {
        L.info("findSampleById: sampleId: " + sampleId);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createSample(Integer sampleId, Integer sampleKindId) {
        L.info("createSample: sampleId: " + sampleId + ", sampleKindId: " + sampleKindId);
        // TODO Auto-generated method stub

    }

    @Override
    public void clearTray(Integer trayId) {
        L.info("clearTray: trayId: " + trayId);
        // TODO Auto-generated method stub

    }

}
