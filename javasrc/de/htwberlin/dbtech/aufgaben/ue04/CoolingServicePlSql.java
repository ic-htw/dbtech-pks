package de.htwberlin.dbtech.aufgaben.ue04;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.CoolingSystemException;
import de.htwberlin.dbtech.exceptions.DataException;

public class CoolingServicePlSql implements ICoolingService {
    private static final Logger L = LoggerFactory.getLogger(CoolingServicePlSql.class);
    private Connection connection;


    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection useConnection() {
        if (connection == null) {
            throw new DataException("Connection not set");
        }
        return connection;
    }

    @Override
    public void transferSample(Integer sampleId, Integer diameterInCM) {
        L.info("transferSample: sampleId: " + sampleId + ", diameterInCM: " + diameterInCM);
        try (CallableStatement cStmt = useConnection().prepareCall("{call cooling_service.transfer_sample(?, ?)}")) {
            cStmt.setInt(1, sampleId);
            cStmt.setInt(2, diameterInCM);
            cStmt.executeUpdate();
        } catch (SQLException e) {
            L.info("Error code: " + e.getErrorCode());
            if (e.getErrorCode() == 20002) {
                throw new CoolingSystemException();
            } else {
                throw new DataException(e);
            }
        }
    }
}
