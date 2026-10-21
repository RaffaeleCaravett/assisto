package com.example.security.exception;

public final class SpokenErrors {

    public static final String SESSION = "Accedi nuovamente";
    public static final String CREDENTIALS = "Email o password non corrette. Controlla i dati e riprova.";
    public static final String EMAIL_TAKEN = "Questa email è già registrata. Prova ad accedere, oppure usa un altro indirizzo.";
    public static final String FORBIDDEN = "Non hai il permesso di fare questa operazione.";
    public static final String NOT_FOUND = "Non ho trovato quello che stavi cercando.";
    public static final String BAD_REQUEST = "I dati inviati non sono validi. Controlla e riprova.";
    public static final String UNAVAILABLE = "Il servizio non è disponibile in questo momento. Riprova tra poco.";
    public static final String GENERIC = "Qualcosa è andato storto. Riprova tra poco.";
    public static final String SIGNUP_FAILED = "Non sono riuscito a completare la registrazione. Riprova tra poco.";

    private SpokenErrors() {
    }
}
