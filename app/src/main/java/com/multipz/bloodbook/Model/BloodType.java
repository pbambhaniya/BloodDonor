package com.multipz.bloodbook.Model;

/**
 * Created by Admin on 20-11-2017.
 */

public class BloodType {
    private String bloodtype;
    private int id;

    public BloodType(int id, String bloodtype) {
        this.id = id;
        this.bloodtype = bloodtype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }
}
