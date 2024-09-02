package fr.ekwateur.vernusset;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.time.Instant;
import java.util.Map;

public class ContractFactory {

    public static final String COND_ANNUAL_REVENUE = "annualRevenue";

    private static final ScriptEngine JS_ENGINE;
    private static final Map<Energy, Map<Class<? extends Client>, Map<String, Float>>> PRICE_MATRIX = Map.of(
            Energy.ELECTRICITY, Map.of(
                    PrivateClient.class, Map.of(
                            "true", 0.133F
                    ),
                    ProfessionalClient.class, Map.of(
                            COND_ANNUAL_REVENUE + " < 100000", 0.112F,
                            COND_ANNUAL_REVENUE + " >= 100000", 0.110F
                    )
            ),
            Energy.GAS, Map.of(
                    PrivateClient.class, Map.of(
                            "true", 0.108F
                    ),
                    ProfessionalClient.class, Map.of(
                            COND_ANNUAL_REVENUE + " < 100000", 0.117F,
                            COND_ANNUAL_REVENUE + " >= 100000", 0.112F
                    )
            )
    );

    static {
        final ScriptEngineManager manager = new ScriptEngineManager();
        JS_ENGINE = manager.getEngineByName("js");
    }

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
                .filter(entry -> evaluatePriceCondition(entry.getKey(), client))
                .findFirst()
                .orElseThrow()
                .getValue();
    }

    private static boolean evaluatePriceCondition(final String condition, final Client client) {
        String conditionToEvaluate = condition;
        if (client instanceof ProfessionalClient pro) {
            conditionToEvaluate = conditionToEvaluate.replaceAll(COND_ANNUAL_REVENUE, Double.toString(pro.getAnnualRevenue()));
        }

        try {
            return (boolean) JS_ENGINE.eval(conditionToEvaluate);
        } catch (final ScriptException sex) { // :p
            throw new RuntimeException(sex);
        }
    }
}
