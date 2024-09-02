package fr.ekwateur.vernusset;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class App 
{
    // GMT dates
    private static final Instant _01_08_2024 = Instant.ofEpochMilli(1722470400000L);
    private static final Instant _15_08_2024 = Instant.ofEpochMilli(1723680000000L);
    private static final Instant _31_08_2024 = Instant.ofEpochMilli(1725062400000L);

    public static void main(final String[] args) {
        simpleCaseForAPrivateClientWithOneElecContractBilledOnAugust2024();
        complexCaseForAProfessionalClientWithMultipleElecAndGasContractsOnDifferentTimeslicesBilledOnAugust2024();
    }

    private static void simpleCaseForAPrivateClientWithOneElecContractBilledOnAugust2024() {

        final Client maxime = new PrivateClient(Gender.MALE, "Maxime", "Vernusset");
        final Contract maximeElec = maxime.subscribe(Energy.ELECTRICITY, _01_08_2024);

        // Assuming all meter readings are consistent and present at start and end of the billing period...
        maximeElec.addMeterReading(_01_08_2024, 1903);
        maximeElec.addMeterReading(_15_08_2024, 2637);
        maximeElec.addMeterReading(_31_08_2024, 3087);

        final Invoice maximeAugust2024InvoiceForElec = BillingEngine.computeBill(maxime, _01_08_2024, _31_08_2024);
        System.out.println(maximeAugust2024InvoiceForElec);
    }

    private static void complexCaseForAProfessionalClientWithMultipleElecAndGasContractsOnDifferentTimeslicesBilledOnAugust2024() {

        final Client ekwateur = new ProfessionalClient("EKW-2016", "Ekwateur", 283_000_000);

        final Contract ekwateurElecInPast = ekwateur.subscribe(
                Energy.ELECTRICITY,
                _01_08_2024.minus(365, ChronoUnit.DAYS),
                _01_08_2024.minus(1, ChronoUnit.DAYS));
        final Contract ekwateurGasInFuture = ekwateur.subscribe(
                Energy.GAS,
                _01_08_2024.plus(31, ChronoUnit.DAYS),
                _01_08_2024.plus(365, ChronoUnit.DAYS));

        final Contract ekwateurElecActive = ekwateur.subscribe(Energy.ELECTRICITY,
                _01_08_2024,
                _01_08_2024.plus(364, ChronoUnit.DAYS));
        // Assuming all meter readings are consistent and present at start and end of the billing period...
        ekwateurElecActive.addMeterReading(_01_08_2024, 1903);
        ekwateurElecActive.addMeterReading(_31_08_2024, 3087);

        final Contract ekwateurGasActive = ekwateur.subscribe(
                Energy.GAS,
                _01_08_2024.minus(31, ChronoUnit.DAYS),
                _01_08_2024.plus(31, ChronoUnit.DAYS));
        // Assuming all meter readings are consistent and present at start and end of the billing period...
        ekwateurGasActive.addMeterReading(_01_08_2024, 1903);
        ekwateurGasActive.addMeterReading(_31_08_2024, 3087);

        final Invoice ekwateurAugust2024InvoiceForElecAndGas = BillingEngine.computeBill(ekwateur, _01_08_2024, _31_08_2024);
        System.out.println(ekwateurAugust2024InvoiceForElecAndGas);
    }
}
