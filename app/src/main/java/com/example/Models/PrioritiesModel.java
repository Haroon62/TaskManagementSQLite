package com.example.Models;

public class PrioritiesModel {
    private int  prio_id;
    private String prio_name;

    public PrioritiesModel(int prio_id, String prio_name) {
        this.prio_id = prio_id;
        this.prio_name = prio_name;
    }

    public int getPrio_id() {
        return prio_id;
    }

    public void setPrio_id(int prio_id) {
        this.prio_id = prio_id;
    }

    public String getPrio_name() {
        return prio_name;
    }

    public void setPrio_name(String prio_name) {
        this.prio_name = prio_name;
    }
}
