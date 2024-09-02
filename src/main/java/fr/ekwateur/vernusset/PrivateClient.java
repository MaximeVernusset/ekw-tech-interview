package fr.ekwateur.vernusset;

public class PrivateClient extends Client {

    private Gender gender;
    private String firstname;
    private String lastname;

    public PrivateClient(final Gender gender, final String firstname, final String lastname) {
        super();
        this.gender = gender;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
