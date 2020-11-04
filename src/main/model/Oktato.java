package main.model;

public class Oktato extends Felhasznalo {
    private String tanszek;

    public Oktato(String vnev, String knev, String email, String password, String tanszek) {
        super(vnev, knev, email, password);
        this.tanszek = tanszek;
    }

    public Oktato(String etr_kod, String vnev, String knev, String email, String password, String tanszek) {
        super(etr_kod, vnev, knev, email, password);
        this.tanszek = tanszek;
    }

    public String getTanszek() {
        return tanszek;
    }

    public void setTanszek(String tanszek) {
        this.tanszek = tanszek;
    }
}
