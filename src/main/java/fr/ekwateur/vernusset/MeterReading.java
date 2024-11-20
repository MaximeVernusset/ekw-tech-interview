package fr.ekwateur.vernusset;

import java.time.Instant;

public record MeterReading(
        String contractReference,
        Instant date,
        int index)
        implements Comparable<MeterReading> {

    @Override
    public int compareTo(final MeterReading reading) {
        return this.date.compareTo(reading.date());
    }
}
