package model;

public abstract class Person {
    private String firstName;
    private String lastName;

    /**
     * Stellt die Klasse ein.
     *
     * @param firstName Vorname der Person.
     * @param lastName  Nachname der Person.
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gibt die Vorname zuruck.
     *
     * @return Vorname der Person
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Gibt die Nachname zuruck.
     *
     * @return Nachname der Person
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Gibt der ganzen Name zuruck.
     *
     * @return ganzen Name der Person
     */
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Stellt die Vorname ein.
     *
     * @param firstName Vorname der Person
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Stellt die Nachname ein.
     *
     * @param lastName Nachname der Person
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * gibt die Representierung der Person als String zuruck
     *
     * @return die Representierung der Person als String
     */
    public String toString() {
        return getName();
    }
}