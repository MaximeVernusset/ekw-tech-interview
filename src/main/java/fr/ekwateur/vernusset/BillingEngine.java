package fr.ekwateur.vernusset;

import java.time.Instant;
import java.util.List;

public class BillingEngine {

    public static Invoice computeBill(final Client client, final Instant startPeriod, final Instant endPeriod) {
        final Invoice invoice = new Invoice(client.getReference(), startPeriod, endPeriod);
        client.getContractsIterator().forEachRemaining(contract -> {
            if (isActiveDuringBillPeriod(contract, startPeriod, endPeriod)) {
                invoice.addAmount(contract.getReference(), computeContractBillAmount(contract, startPeriod, endPeriod));
            }
        });
        return invoice;
    }

    private static boolean isActiveDuringBillPeriod(final Contract contract, final Instant startPeriod, final Instant endPeriod) {
        return startPeriod.toEpochMilli() >= contract.getSubscriptionDate().toEpochMilli()
                && (null == contract.getTerminationDate() || endPeriod.toEpochMilli() <= contract.getTerminationDate().toEpochMilli());
    }

    private static float computeContractBillAmount(final Contract contract, final Instant startPeriod, final Instant endPeriod) {
        final List<MeterReading> meterReadingsInPeriod = contract.getMeterReadingsBetween(startPeriod, endPeriod);
        final float consumptionForPeriod = meterReadingsInPeriod.getLast().index() - meterReadingsInPeriod.getFirst().index();
        return consumptionForPeriod * contract.getKwtPrice();
    }

}
