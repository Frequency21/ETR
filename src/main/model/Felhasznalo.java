package main.model;

public class Felhasznalo {
    private String etr_kod;
    private String vnev;
    private String knev;
    private String email;
    private String password;

    public Felhasznalo() {
    }

    public Felhasznalo(String vnev, String knev, String email, String password) {
        this.vnev = vnev;
        this.knev = knev;
        this.email = email;
        this.password = password;
    }

    public Felhasznalo(String etr_kod, String vnev, String knev, String email, String password) {
        this.etr_kod = etr_kod;
        this.vnev = vnev;
        this.knev = knev;
        this.email = email;
        this.password = password;
    }

    public String getEtr_kod() {
        return etr_kod;
    }

    public void setEtr_kod(String etr_kod) {
        this.etr_kod = etr_kod;
    }

    public String getVnev() {
        return vnev;
    }

    public void setVnev(String vnev) {
        this.vnev = vnev;
    }

    public String getKnev() {
        return knev;
    }

    public void setKnev(String knev) {
        this.knev = knev;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
