package de.htwberlin.dbtech.aufgaben.ue04;

import java.io.File;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.CoolingSystemException;
import de.htwberlin.dbtech.utils.DbCred;
import de.htwberlin.dbtech.utils.DbUnitUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoolingServicePlSqlTest {
    private static final Logger L = LoggerFactory.getLogger(CoolingServicePlSqlTest.class);
    private static IDatabaseConnection dbTesterCon = null;

    private static final ICoolingService cs = new CoolingServicePlSql();

    @BeforeClass
    public static void setUp() {
        L.debug("setUp: start");
        try {
            IDatabaseTester dbTester = new JdbcDatabaseTester(DbCred.driverClass, DbCred.url, DbCred.user, DbCred.password,
                    DbCred.schema);
            dbTesterCon = dbTester.getConnection();
            IDataSet pre = new CsvDataSet(new File("test-data/ue03-04/pre"));
            dbTester.setDataSet(pre);
            DatabaseOperation.CLEAN_INSERT.execute(dbTesterCon, pre);
            cs.setConnection(dbTesterCon.getConnection());
        } catch (Exception e) {
            DbUnitUtils.closeDbUnitConnectionQuietly(dbTesterCon);
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void tearDown() {
        L.debug("tearDown: start");
        DbUnitUtils.closeDbUnitConnectionQuietly(dbTesterCon);
    }

    /**
     * SampleID existiert nicht.
     */
    @org.junit.Test(expected = CoolingSystemException.class)
    public void testCoolingService01() {
        Integer nonexistentSampleId = 999;
        Integer validDiameter = 1;
        cs.transferSample(nonexistentSampleId, validDiameter);
    }

    /**
     * Kein Tablett mit passenden Durchmesser vorhanden.
     */
    @org.junit.Test(expected = CoolingSystemException.class)
    public void testCoolingService02() {
        Integer validSampleId = 1;
        Integer diameterTooLarge = 999;
        cs.transferSample(validSampleId, diameterTooLarge);
    }

    /**
     * Ablaufdatum Probe zu gross. Kein freies Tablett richtiger Gr��e vorhanden.
     */
    @org.junit.Test(expected = CoolingSystemException.class)
    public void testCoolingService03() {
        Integer sampleIdWithExpirationToFarInFuture = 1;
        Integer validDiameter = 1;
        cs.transferSample(sampleIdWithExpirationToFarInFuture, validDiameter);
    }

    /**
     * Ablaufdatum Probe nicht zu gross. Alle passenden Tabletts voll. Kein freies
     * Tablett richtiger Groesse vorhanden
     */
    @org.junit.Test(expected = CoolingSystemException.class)
    public void testCoolingService04() {
        Integer sampleIdWithExpirationOk = 2;
        Integer diameterPossible = 1;
        cs.transferSample(sampleIdWithExpirationOk, diameterPossible);
    }

    /**
     * Ablaufdatum Probe zu gross. Freies Tablett richtiger Groesse vorhanden
     *
     */
    @org.junit.Test
    public void testCoolingService05() throws DatabaseUnitException {
        Integer sampleIdWithExpirationToFarInFuture = 3;
        Integer validDiameter = 2;
        cs.transferSample(sampleIdWithExpirationToFarInFuture, validDiameter);

        // Lade tatsaechliche Daten aus der Datenbank
        QueryDataSet databaseDataSet = new QueryDataSet(dbTesterCon);
        String sql1 = "select * from Tray where TrayID=2 order by TrayId";
        databaseDataSet.addTable("Tray", sql1);
        String sql2 = "select * from Place where TrayID=2 order by TrayId, PlaceNo";
        databaseDataSet.addTable("Place", sql2);
        ITable actualTableTray = databaseDataSet.getTable("Tray");
        ITable actualTablePlace = databaseDataSet.getTable("Place");

        // Lade erwartete Daten
        IDataSet expectedDataSet = new CsvDataSet(new File("test-data/ue03-04/post01"));
        ITable expectedTableTray = expectedDataSet.getTable("Tray");
        ITable expectedTablePlace = expectedDataSet.getTable("Place");

        Assertion.assertEquals(expectedTableTray, actualTableTray);
        Assertion.assertEquals(expectedTablePlace, actualTablePlace);
    }

    /**
     * Ablaufdatum Probe ok. Alle passenden Tabletts voll. Freies Tablett richtiger
     * Gr��e vorhanden.
     */
    @org.junit.Test
    public void testCoolingService06() throws DatabaseUnitException {
        Integer validSampleId = 5;
        Integer validDiameter = 3;
        cs.transferSample(validSampleId, validDiameter);

        // Lade tatsaechliche Daten aus der Datenbank
        QueryDataSet databaseDataSet = new QueryDataSet(dbTesterCon);
        String sql1 = "select * from Tray where TrayID in (3,4) order by TrayId";
        databaseDataSet.addTable("Tray", sql1);
        String sql2 = "select * from Place where TrayID in (3,4) order by TrayId, PlaceNo";
        databaseDataSet.addTable("Place", sql2);
        ITable actualTableTray = databaseDataSet.getTable("Tray");
        ITable actualTablePlace = databaseDataSet.getTable("Place");

        // Lade erwartete Daten
        IDataSet expectedDataSet = new CsvDataSet(new File("test-data/ue03-04/post02"));
        ITable expectedTableTray = expectedDataSet.getTable("Tray");
        ITable expectedTablePlace = expectedDataSet.getTable("Place");

        Assertion.assertEquals(expectedTableTray, actualTableTray);
        Assertion.assertEquals(expectedTablePlace, actualTablePlace);
    }

    /**
     * Ablaufdatum fuer 2 Proben ok. Platz am Ende frei.
     *
     */
    @org.junit.Test
    public void testCoolingService07() throws DatabaseUnitException {
        Integer validSampleId1 = 7;
        Integer validSampleId2 = 8;
        Integer validDiameter = 4;
        cs.transferSample(validSampleId1, validDiameter);
        cs.transferSample(validSampleId2, validDiameter);

        // Lade tatsaechliche Daten aus der Datenbank
        QueryDataSet databaseDataSet = new QueryDataSet(dbTesterCon);
        String sql1 = "select * from Tray where TrayID=5 order by TrayId";
        databaseDataSet.addTable("Tray", sql1);
        String sql2 = "select * from Place where TrayID=5 order by TrayId, PlaceNo";
        databaseDataSet.addTable("Place", sql2);
        ITable actualTableTray = databaseDataSet.getTable("Tray");
        ITable actualTablePlace = databaseDataSet.getTable("Place");

        // Lade erwartete Daten
        IDataSet expectedDataSet = new CsvDataSet(new File("test-data/ue03-04/post03"));
        ITable expectedTableTray = expectedDataSet.getTable("Tray");
        ITable expectedTablePlace = expectedDataSet.getTable("Place");

        Assertion.assertEquals(expectedTableTray, actualTableTray);
        Assertion.assertEquals(expectedTablePlace, actualTablePlace);
    }

    /**
     * Ablaufdatum fuer 2 Proben ok. Platz zwischendrin frei.
     *
     */
    @org.junit.Test
    public void testCoolingService08() throws DatabaseUnitException {
        Integer validSampleId1 = 11;
        Integer validSampleId2 = 12;
        Integer validDiameter = 5;
        cs.transferSample(validSampleId1, validDiameter);
        cs.transferSample(validSampleId2, validDiameter);

        // Lade tatsaechliche Daten aus der Datenbank
        QueryDataSet databaseDataSet = new QueryDataSet(dbTesterCon);
        String sql1 = "select * from Tray where TrayID=6 order by TrayId";
        databaseDataSet.addTable("Tray", sql1);
        String sql2 = "select * from Place where TrayID=6 order by TrayId, PlaceNo";
        databaseDataSet.addTable("Place", sql2);
        ITable actualTableTray = databaseDataSet.getTable("Tray");
        ITable actualTablePlace = databaseDataSet.getTable("Place");

        // Lade erwartete Daten
        IDataSet expectedDataSet = new CsvDataSet(new File("test-data/ue03-04/post04"));
        ITable expectedTableTray = expectedDataSet.getTable("Tray");
        ITable expectedTablePlace = expectedDataSet.getTable("Place");

        Assertion.assertEquals(expectedTableTray, actualTableTray);
        Assertion.assertEquals(expectedTablePlace, actualTablePlace);
    }

}
