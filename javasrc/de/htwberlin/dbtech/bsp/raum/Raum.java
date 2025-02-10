package de.htwberlin.dbtech.bsp.raum;

public class Raum {
    private int rid;
    private String raumNr;
    private int anzahlSitze;

    @Override
    public String toString() {
        return "Raum [rid=" + rid + ", raumNr=" + raumNr + ", anzahlSitze=" + anzahlSitze + "]";
    }

    public Raum(int rid, String raumNr, int anzahlSitze) {
        this.rid = rid;
        this.raumNr = raumNr;
        this.anzahlSitze = anzahlSitze;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getRaumNr() {
        return raumNr;
    }

    public void setRaumNr(String raumNr) {
        this.raumNr = raumNr;
    }

    public int getAnzahlSitze() {
        return anzahlSitze;
    }

    public void setAnzahlSitze(int anzahlSitze) {
        this.anzahlSitze = anzahlSitze;
    }

}
