package de.htwberlin.dbtech.bsp.test;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
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

import de.htwberlin.dbtech.utils.DbCred;
import de.htwberlin.dbtech.utils.DbUnitUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DbTest {
    private static final Logger L = LoggerFactory.getLogger(DbTest.class);
    private static IDatabaseConnection dbTesterCon = null;

    @BeforeClass
    public static void setUp() {
        L.debug("setUp - start");
        try {
            IDatabaseTester dbTester = new JdbcDatabaseTester(DbCred.driverClass, DbCred.url, DbCred.user, DbCred.password,
                    DbCred.schema);
            dbTesterCon = dbTester.getConnection();
            IDataSet pre = new CsvDataSet(new File("test-data/raum/pre"));
            dbTester.setDataSet(pre);
            DatabaseOperation.CLEAN_INSERT.execute(dbTesterCon, pre);
        } catch (Exception e) {
            DbUnitUtils.closeDbUnitConnectionQuietly(dbTesterCon);
            throw new RuntimeException(e);
        }
        L.debug("setUp - ende");
    }

    @AfterClass
    public static void tearDown() {
        L.debug("tearDown - start");
        DbUnitUtils.closeDbUnitConnectionQuietly(dbTesterCon);
        L.debug("tearDown - ende");
    }

    @org.junit.Test
    public void testInsertNewRoom() throws Exception {
        L.info("testInsertNewRoom - start");
        Connection c = dbTesterCon.getConnection();
        String sql = "insert into raum values(4,'A030', 30)";
        Statement s = c.createStatement();
        s.executeUpdate(sql);

        // Hole Daten aus der Tabelle Raum
        IDataSet databaseDataSet = dbTesterCon.createDataSet();
        ITable actualTable = databaseDataSet.getTable("raum");

        // Lade erwartete Daten aus der csv-Datei
        IDataSet expectedDataSet = new CsvDataSet(new File("test-data/raum/post"));
        ITable expectedTable = expectedDataSet.getTable("raum");

        // Pruefe, ob die Datenmengen uebereinstimmen
        Assertion.assertEquals(expectedTable, actualTable);
        L.info("testInsertNewRoom - ende");
    }

}
