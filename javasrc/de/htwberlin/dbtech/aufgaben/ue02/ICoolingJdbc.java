package de.htwberlin.dbtech.aufgaben.ue02;

import java.sql.Connection;
import java.util.List;

import de.htwberlin.dbtech.exceptions.CoolingSystemException;

public interface ICoolingJdbc {

    /**
     * Speichert die uebergebene Datenbankverbindung in einer Instanzvariablen.
     */
    void setConnection(Connection connection);

    /**
     * Liefert eine Liste mit den Bezeichnungen der Probearten.
     *
     * @return Liste von Strings mit den Bezeichnungen der Probearten.
     */
    List<String> getSampleKinds();

    /**
     * Liefert das Proben-Objekt, das zu einer sampleId gehoert.
     *
     * @param sampleId z.B. 1.
     * @return das Proben-Objekt mit der sampleId.
     * @throws CoolingSystemException wenn keine Probe mit der id existiert.
     */
    Sample findSampleById(Integer sampleId);

    /**
     * Fuegt einen neuen Proben-Datensatz in die Datenbank ein. Das Ablaufdatum
     * berechnet sich aus dem aktuellen Datum plus Anzahl der gueltigen Tage fuer
     * die Probenart.
     *
     * @param sampleId     Primaerschuessel der Probe.
     * @param sampleKindId Fremdschluessel auf die Probenart.
     * @throws CoolingSystemException wenn der Primaerschuessel bereits existiert.
     * @throws CoolingSystemException wenn keine Probenart mit der sampleKindId existiert.
     */
    void createSample(Integer sampleId, Integer sampleKindId);

    /**
     * Entsorgt alle Proben eines Tabletts.
     *
     * @param trayId Primaerschuessel des Tabletts.
     * @throws CoolingSystemException wenn der Primaerschuessel nicht existiert.
     */
    void clearTray(Integer trayId);

}
