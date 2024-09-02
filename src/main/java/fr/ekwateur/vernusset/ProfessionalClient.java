package fr.ekwateur.vernusset;

public class ProfessionalClient extends Client {

    private final String siretNumber;
    private final String companyName;
    private double annualRevenue;

    public ProfessionalClient(final String siretNumber, final String companyName, final double annualRevenue) {
        super();
        this.siretNumber = siretNumber;
        this.companyName = companyName;
        this.annualRevenue = annualRevenue;
    }

    public double getAnnualRevenue() {
        return this.annualRevenue;
    }

    void updateAnnualRevenue(final double newAnnualRevenue) {
        this.annualRevenue = newAnnualRevenue;
    }

}
