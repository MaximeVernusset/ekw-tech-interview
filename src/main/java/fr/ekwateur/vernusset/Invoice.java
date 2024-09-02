package fr.ekwateur.vernusset;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Invoice {

    private final String clientReference;
    private final Instant date;
    private final Instant beginPeriod;

    private final Instant endPeriod;
    private final Map<String, Float> amountPerContract;

    public Invoice(final String clientReference, final Instant beginPeriod, final Instant endPeriod) {
        this.date = Instant.now();
        this.clientReference = clientReference;
        this.beginPeriod = beginPeriod;
        this.endPeriod = endPeriod;
        this.amountPerContract = new HashMap<>();
    }

    public void addAmount(final String contractReference, final float amount) {
        amountPerContract.put(contractReference, amount);
    }

    public float getTotalAmount() {
        return amountPerContract.values().stream().reduce(0.0F, Float::sum);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "clientReference='" + clientReference + '\'' +
                ", date=" + date +
                ", beginPeriod=" + beginPeriod +
                ", endPeriod=" + endPeriod +
                ", totalAmount=" + getTotalAmount() +
                ", amountPerContract=" + amountPerContract +
                '}';
    }

}
