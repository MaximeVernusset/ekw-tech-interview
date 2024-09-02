package fr.ekwateur.vernusset;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Contract {

    private static final String PREFIX_FORMAT_CONTRACT_REF = "EKW%s-";
    private static final ReferenceGenerator referenceGenerator = new ContractReferenceGenerator();

    private final String reference;
    private final String clientReference;
    private final Energy type;
    private final float kwtPrice;
    private final Instant subscriptionDate;
    private final Map<Instant, MeterReading> meterReadings;
    private Instant terminationDate;

    public Contract(final Energy type, final float kwtPrice, final String clientReference, final Instant subscriptionDate) {
        this.reference = referenceGenerator.generate(String.format(PREFIX_FORMAT_CONTRACT_REF, type));
        this.clientReference = clientReference;
        this.type = type;
        this.kwtPrice = kwtPrice;
        this.subscriptionDate = subscriptionDate;
        this.terminationDate = null;
        this.meterReadings = new TreeMap<>();
    }

    public Contract(final Energy type, final float kwtPrice, final String clientReference, final Instant subscriptionDate, final Instant terminationDate) {
        this(type, kwtPrice, clientReference, subscriptionDate);
        this.terminationDate = terminationDate;
    }

    public String getReference() {
        return reference;
    }

    public float getKwtPrice() {
        return kwtPrice;
    }

    public Instant getSubscriptionDate() {
        return subscriptionDate;
    }

    public Instant getTerminationDate() {
        return terminationDate;
    }

    public List<MeterReading> getMeterReadingsBetween(final Instant begin, final Instant end) {
        return this.meterReadings
                .entrySet()
                .stream()
                .filter(e -> e.getKey().toEpochMilli() >= begin.toEpochMilli() && e.getKey().toEpochMilli() <= end.toEpochMilli())
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
    }

    public void addMeterReading(final Instant dateReading, final int indexReading) {
        meterReadings.put(dateReading, new MeterReading(reference, dateReading, indexReading));
    }

    public void terminate(final Instant terminationDate) {
        this.terminationDate = terminationDate;
    }
}
