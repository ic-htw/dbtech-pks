package de.htwberlin.dbtech.aufgaben.ue03;

import java.sql.Connection;

import de.htwberlin.dbtech.exceptions.CoolingSystemException;
import de.htwberlin.dbtech.exceptions.DataException;

public interface ICoolingService {

    /**
     * Speichert die uebergebene Datenbankverbindung in einer Instanzvariablen.
     *
     * @author Ingo Classen
     */
    void setConnection(Connection connection);

    /**
     * Sucht ein Tablett, das die Probe aufnehmen kann und erzeugt einen
     * entsprechenden Datensatz in der Tabelle Place. Folgende Bedingungen muessen
     * erfuellt sein:
     * <ul>
     * <li>Der Durchmesser muss passen.</li>
     * <li>Die Probe soll auf das Tablett mit dem kleinsten Ablaufdatum kommen,
     * das groesser als das Ablaufdatum der Probe ist.</li>
     * <li>Auf dem ausgewaehlten Tablett soll die Probe auf den kleinsten freien
     * Platz kommen. Da stellt sicher, dass Luecken gefuellt werden.</li>
     * <li>Gibt es kein passendes Tablett, so wird ein leeres Tablett mit
     * passendem Durchmesser genommen und dessen Ablaufdatum auf das Ablaufdatum
     * der Probe plus 30 Tage gesetzt.</li>
     * <li>Gibt es kein leeres Tablett mit passendem Durchmesser mehr, so wird
     * eine Ausnahme ausgeloest.</li>
     * </ul>
     *
     * @param sampleId     Primaerschluessel der Probe.
     * @param diameterInCM Durchmesser des Probenroehrchens. Wird durch einen Sensor
     *                     gemessen.
     * @throws CoolingSystemException falls kein passendes Tablett gefunden werden kann.
     * @throws DataException          bei allen Datenbankfehlern.
     */
    void transferSample(Integer sampleId, Integer diameterInCM);
}
