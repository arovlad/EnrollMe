package exceptions;

/**
 * Wird eingewurft, wenn der Anzahl der Kredite wurde ubergestiegen.
 */
public class CreditsOverflowException extends Exception {
    public CreditsOverflowException(){
        System.out.println("Der Zahl der Kredite wurde ubergeschritten.");
    }
}
