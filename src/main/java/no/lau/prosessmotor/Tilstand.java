package no.lau.prosessmotor;

/**
 * Brukes for å holde rede på tilstanden til et stev - hvordan gikk en kjøring
 */
public class Tilstand {
    public static final Tilstand OK = new Tilstand();
    public static final Tilstand FAILED = new Tilstand();
}
