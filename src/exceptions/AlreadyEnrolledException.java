package exceptions;

/**
 * Wird eingewurft wenn einen Student ist schon in einen Vorlesung eingemelted
 */
public class AlreadyEnrolledException extends Exception {
    public AlreadyEnrolledException(){
        System.out.println("Der Student ist schon an dieser Vorlesung angemeldet.");
    }
}
