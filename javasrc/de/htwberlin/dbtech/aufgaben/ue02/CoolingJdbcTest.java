package de.htwberlin.dbtech.aufgaben.ue02;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.RowFilterTable;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.dataset.filter.IRowFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.exceptions.CoolingSystemException;
import de.htwberlin.dbtech.utils.DbCred;
import de.htwberlin.dbtech.utils.DbUnitUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoolingJdbcTest {
    private static final Logger L = LoggerFactory.getLogger(CoolingJdbcTest.class);
    private static IDatabaseConnection dbTesterCon = null;
    private static IDataSet pre = null;

    private static final ICoolingJdbc cj = new CoolingJdbc();

    @BeforeClass
    public static void setUp() {
        L.debug("setup: start");
        try {
            IDatabaseTester dbTester = new JdbcDatabaseTester(DbCred.driverClass, DbCred.url, DbCred.user, DbCred.password,
                    DbCred.schema);
            dbTesterCon = dbTester.getConnection();
            pre = new CsvDataSet(new File("test-data/ue02"));
            dbTester.setDataSet(pre);
            DatabaseOperation.CLEAN_INSERT.execute(dbTesterCon, pre);
            cj.setConnection(dbTesterCon.getConnection());
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
     * Test von getSampleKinds. Test, ob die richtige Liste der Probearten
     * zurueckgeliefert wird.
     *
     */
    @org.junit.Test
    public void aaaGetSampleKinds1() {
        String[] actualSampleKinds = new String[3];
        List<String> sampleKinds = cj.getSampleKinds();
        for (int i = 0; i < sampleKinds.size(); i++) {
            actualSampleKinds[i] = sampleKinds.get(i);
        }
        String[] expectedSampleKinds = {"Blood", "Serum", "Urine"};
        Assert.assertArrayEquals(expectedSampleKinds, actualSampleKinds);
    }

    /**
     * Test von findSampleById. Test, ob die richtige Probe zurueckgeliefert wird.
     *
     */
    @org.junit.Test
    public void bbbfindSampleById1() {
        Sample sample = cj.findSampleById(1);
        LocalDate d = LocalDate.of(2017, 3, 24);
        Assert.assertEquals(d, sample.getExpirationDate());
    }

    /**
     * Test von findSampleById. SampleID existiert nicht.
     *
     */
    @org.junit.Test(expected = CoolingSystemException.class)
    public void bbbfindSampleById2() {
        cj.findSampleById(999);
    }

    /**
     * Test von createSample. SampleID existiert bereits.
     *
     */
    @org.junit.Test(expected = CoolingSystemException.class)
    public void cccCreateSample1() {
        cj.createSample(1, 1);
    }

    /**
     * Test von createSample. SampleKindID existiert nicht.
     *
     */
    @org.junit.Test(expected = CoolingSystemException.class)
    public void cccCreateSample2() {
        cj.createSample(1, 999);
    }

    /**
     * Test von createSample. Test, ob der richtige Datensatz in die Tabelle
     * eingefuegt wurde.
     *
     */
    @org.junit.Test
    public void cccCreateSample3() throws DatabaseUnitException {
        int sampleKindId = 1; // for blood
        int validNoOfDays = 4; // for blood

        cj.createSample(99, sampleKindId);

        // Hole Daten aus der Tabelle Sample, ausser Datensatz 99
        QueryDataSet databaseDataSet = new QueryDataSet(dbTesterCon);
        String sql = "select * from Sample where SampleID<>99 order by SampleID";
        databaseDataSet.addTable("Sample", sql);

        ITable actualTable = databaseDataSet.getTable("Sample");
        ITable expectedTable = pre.getTable("Sample");

        // Vergleiche, dass sich nichts veraendert hat
        Assertion.assertEquals(expectedTable, actualTable);

        // Pruefe, dass der neue Datensatz mit dem richtigen Ablaufdatum
        // eingefuegt wurde
        Sample sample = cj.findSampleById(99);
        LocalDate d = LocalDate.now().plusDays(validNoOfDays);
        Assert.assertEquals("wrong date", d, sample.getExpirationDate());
    }

    /**
     * Test von clearTray. TrayID existiert nicht.
     *
     */
    @org.junit.Test(expected = CoolingSystemException.class)
    public void dddClearTray1() {
        cj.clearTray(999);
    }

    /**
     * Test von clearTray Entsorgung von Tablett 6, das keine Proben enthaelt. Test,
     * ob nichts geloescht wurde.
     *
     */
    @org.junit.Test
    public void dddClearTray2() throws DatabaseUnitException {
        cj.clearTray(6);

        // Hole Daten aus der Tabelle Sample, ausser Datensatz 99
        QueryDataSet databaseDataSet = new QueryDataSet(dbTesterCon);
        String sql = "select * from Sample where SampleID<>99 order by SampleID";
        databaseDataSet.addTable("Sample", sql);

        ITable actualTable = databaseDataSet.getTable("Sample");
        ITable expectedTable = pre.getTable("Sample");

        // Vergleiche, dass sich nichts veraendert hat
        Assertion.assertEquals(expectedTable, actualTable);
    }

    /**
     * Test von clearTray. Entsorgung von Tablett 9, das Proben enthaelt. Test, ob
     * die richtige Datensaetze geloescht wurden.
     *
     */
    @org.junit.Test
    public void dddClearTray3() throws DatabaseUnitException {
        cj.clearTray(9);

        // Hole Daten aus der Tabelle Sample, ausser Datensatz 99
        // Dieser Datensatz wurde durch einen anderen Test eingefuegt
        // und ist daher nich im feedDataSet
        QueryDataSet databaseDataSet = new QueryDataSet(dbTesterCon);
        String sql = "select * from Sample where SampleID<>99 order by SampleID";
        databaseDataSet.addTable("Sample", sql);
        ITable actualTable = databaseDataSet.getTable("Sample");

        // Filtere die geloeschen Datensaetze aus dem feedDataSet
        ITable sampleTable = pre.getTable("Sample");
        IRowFilter rowFilter = rowValueProvider -> {
            Object columnValue;
            try {
                columnValue = rowValueProvider.getColumnValue("SampleID");
            } catch (DataSetException e) {
                throw new RuntimeException(e);
            }
            Integer sampleId = Integer.parseInt((String) columnValue);
            return !Arrays.asList(8, 9, 10).contains(sampleId);
        };
        RowFilterTable expectedTable = new RowFilterTable(sampleTable, rowFilter);

        // Vergleiche, dass sich nichts veraendert hat
        Assertion.assertEquals(expectedTable, actualTable);
    }

}
