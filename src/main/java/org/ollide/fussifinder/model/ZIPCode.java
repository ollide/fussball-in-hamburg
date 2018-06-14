package org.ollide.fussifinder.model;

public class ZIPCode {

    private String plz;

    public ZIPCode() {
        // default constructor
    }

    public ZIPCode(String plz) {
        this.plz = plz;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }
}
