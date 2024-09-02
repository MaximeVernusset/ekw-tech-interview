package fr.ekwateur.vernusset;

import java.util.UUID;

public interface ReferenceGenerator {

    final static String HYPHEN = "-";
    final static String EMPY_STRING = "";

    default String generate(final String prefix) {
        return prefix + UUID.randomUUID().toString().replaceAll(HYPHEN, EMPY_STRING).substring(0, 9);
    }

}
