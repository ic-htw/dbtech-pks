package de.htwberlin.dbtech.bsp.intro;

import java.io.File;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.dbtech.utils.DbCred;
import de.htwberlin.dbtech.utils.DbUnitUtils;

public class MainDbUnit {
    private static final Logger L = LoggerFactory.getLogger(MainDbUnit.class);

    public static void main(String[] args) throws Exception {
        L.info("main - start");
        IDataSet dataSet = new CsvDataSet(new File("test-data/raum/pre"));
        IDatabaseTester dbTester = new JdbcDatabaseTester(DbCred.driverClass, DbCred.url, DbCred.user, DbCred.password,
                DbCred.schema);
        dbTester.setDataSet(dataSet);
//    m1(dbTester);
        m2(dbTester);
        L.info("main - ende");
    }

    static void m1(IDatabaseTester dbTester) {
        L.info("m1 - start");
        IDatabaseConnection dbTesterCon = null;
        try {
            dbTesterCon = dbTester.getConnection();
            DatabaseOperation.CLEAN_INSERT.execute(dbTesterCon, dbTester.getDataSet());
        } catch (Exception e) {
            DbUnitUtils.closeDbUnitConnectionQuietly(dbTesterCon);
            throw new RuntimeException(e);
        } finally {
            DbUnitUtils.closeDbUnitConnectionQuietly(dbTesterCon);
        }
        L.info("m1 - ende");
    }

    static void m2(IDatabaseTester dbTester) throws Exception {
        L.info("m2 - start");
        File pre = new File("test-data/raum/pre");
        IDataSet dataSet = new CsvDataSet(pre);
        dbTester.setDataSet(dataSet);
        ITable table = dataSet.getTable("Raum");
        int noOfRows = table.getRowCount();
        for (int i = 0; i < noOfRows; i++) {
            Object sampleId = table.getValue(i, "RaumNr");
            System.out.println(sampleId);
        }
        L.info("m2 - ende");
    }

}
