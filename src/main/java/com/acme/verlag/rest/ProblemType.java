package com.acme.verlag.rest;

/**
 * Enum für ProblemDetail.type.
 */
enum ProblemType {
    /**
     * Constraints als Fehlerursache.
     */
    CONSTRAINTS("constraints"),
    /**
     * Fehler beim Header If-Match.
     */
    PRECONDITION("precondition");

    private final String value;

    ProblemType(final String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
