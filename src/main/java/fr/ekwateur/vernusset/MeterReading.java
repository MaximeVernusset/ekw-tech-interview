package fr.ekwateur.vernusset;

import java.time.Instant;

public class MeterReading implements Comparable<MeterReading> {

    private final String contractReference;
    private final Instant date;
    private final int index;

    public MeterReading(final String contractReference, final Instant date, final int index) {
        this.contractReference = contractReference;
        this.date = date;
        this.index = index;
    }

    public Instant getDate() {
        return date;
    }

    public String getContractReference() {
        return contractReference;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(final MeterReading reading) {
        return this.date.compareTo(reading.getDate());
    }
}
