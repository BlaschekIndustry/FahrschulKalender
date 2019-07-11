package gui;

import javafx.beans.property.SimpleStringProperty;

public class TableRow {
    private String mo;
    private String tu;
    private String we;
    private String th;
    private String fr;
    private String sa;

    public TableRow(String mo, String tu, String we, String th, String fr, String sa){
        this.mo = mo;
        this.tu = tu;
        this.we = we;
        this.th = th;
        this.fr = fr;
        this.sa = sa;
    }

    public String getTu() {
        return tu;
    }

    public void setTu(String tu) {
        this.tu = tu;
    }

    public String getWe() {
        return we;
    }

    public void setWe(String we) {
        this.we = we;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getSa() {
        return sa;
    }

    public void setSa(String sa) {
        this.sa = sa;
    }

    public String getMo() {
        return mo;
    }

    public void setMo(String mo){
        this.mo = mo;
    }
}
