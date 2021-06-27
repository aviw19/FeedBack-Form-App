package com.example.formimplementation;

public class DataSet {
    private String s;
    private String s2;
    private int id;

    public DataSet(String description, String input, int id) {
        this.s=description;
        this.s2=input;
        this.id=id;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
