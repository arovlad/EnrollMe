package repository;

public class AlreadyInListException extends Exception{
    public AlreadyInListException(){
        System.out.println("Dieses Eintrag existiert schon in der Datenbank.");
    }
}
