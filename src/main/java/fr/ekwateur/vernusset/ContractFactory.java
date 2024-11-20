package fr.ekwateur.vernusset;

import java.time.Instant;
import java.util.Map;
import java.util.function.Predicate;

public class ContractFactory {

    private static final Predicate<PrivateClient> PREDICATE_PRIVATE_CLIENT = (c) -> true;
    private static final Predicate<ProfessionalClient> PREDICATE_PROFESSIONAL_CLIENT_REVENUE_LESS_100000 = (c) -> c.getAnnualRevenue() < 100000;

    private static final Map<Energy, Map<Class<? extends Client>, Map<Predicate<? extends Client>, Float>>> PRICE_MATRIX = Map.of(
            Energy.ELECTRICITY, Map.of(
                    PrivateClient.class, Map.of(
                            PREDICATE_PRIVATE_CLIENT, 0.133F
                    ),
                    ProfessionalClient.class, Map.of(
                            PREDICATE_PROFESSIONAL_CLIENT_REVENUE_LESS_100000, 0.112F,
                            PREDICATE_PROFESSIONAL_CLIENT_REVENUE_LESS_100000.negate(), 0.110F
                    )
            ),
            Energy.GAS, Map.of(
                    PrivateClient.class, Map.of(
                            PREDICATE_PRIVATE_CLIENT, 0.108F
                    ),
                    ProfessionalClient.class, Map.of(
                            PREDICATE_PROFESSIONAL_CLIENT_REVENUE_LESS_100000, 0.117F,
                            PREDICATE_PROFESSIONAL_CLIENT_REVENUE_LESS_100000.negate(), 0.112F
                    )
            )
    );

    public static Contract newContract(final Client client, final Energy energy, final Instant subscriptionDate, final Instant terminationDate) {
        return new Contract(
                energy,
                getKwhPrice(client, energy),
                client.getReference(),
                subscriptionDate,
                terminationDate);
    }

    private static float getKwhPrice(final Client client, final Energy energy) {
        return PRICE_MATRIX
                .get(energy)
                .get(client.getClass())
                .entrySet()
                .stream()
                .filter(entry -> evaluatePriceCondition((Predicate<Client>) entry.getKey(), client))
                .findFirst()
                .orElseThrow()
                .getValue();
    }

    private static boolean evaluatePriceCondition(final Predicate<Client> condition, final Client client) {
        return condition.test(client);
    }
}
