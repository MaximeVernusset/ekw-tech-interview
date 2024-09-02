package fr.ekwateur.vernusset;

import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class Client {

    private static final String PREFIX_CLIENT_REF = "EKW";

    private static final ReferenceGenerator referenceGenerator = new ClientReferenceGenerator();

    private final String reference;
    private final Set<Contract> contracts;

    public Client() {
        this.reference = referenceGenerator.generate(PREFIX_CLIENT_REF);
        this.contracts = new HashSet<>();
    }

    public String getReference() {
        return reference;
    }

    public Iterator<Contract> getContractsIterator() {
        return contracts.iterator();
    }

    public Contract subscribe(final Energy energy, final Instant subscriptionDate) {
        return subscribe(energy, subscriptionDate, null);
    }

    public Contract subscribe(final Energy energy, final Instant subscriptionDate, final Instant terminationDate) {
        final Contract contract = ContractFactory.newContract(this, energy, subscriptionDate, terminationDate);
        if (!this.contracts.add(contract)) {
            throw new RuntimeException("Contract already exists: " + contract);
        }
        return contract;
    }
}
