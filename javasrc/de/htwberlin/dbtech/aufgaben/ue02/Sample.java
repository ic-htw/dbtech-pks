package de.htwberlin.dbtech.aufgaben.ue02;

import java.time.LocalDate;

public class Sample {
    private Integer sampleId;
    private Integer sampleKindId;
    private LocalDate expirationDate;

    public Sample() {
    }

    public Sample(Integer sampleId, Integer sampleKindId, LocalDate expirationDate) {
        super();
        this.sampleId = sampleId;
        this.sampleKindId = sampleKindId;
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "Sample[sampleId=" + sampleId + ", sampleKindId=" + sampleKindId + ", expirationDate=" + expirationDate
                + "]";
    }

    public Integer getSampleId() {
        return sampleId;
    }

    public void setSampleId(Integer sampleId) {
        this.sampleId = sampleId;
    }

    public Integer getSampleKindId() {
        return sampleKindId;
    }

    public void setSampleKindId(Integer sampleKindId) {
        this.sampleKindId = sampleKindId;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

}
