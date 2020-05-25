package exceptions;

/**
 * Wird eingewurft, wenn der Vorlesung keine Platze frei hat.
 */
public class CourseIsFullException extends Exception {
    public CourseIsFullException(){
        System.out.println("Der Vorlesung hat keine Platze frei.");
    }
}
