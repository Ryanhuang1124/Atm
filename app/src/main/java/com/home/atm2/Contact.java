package com.home.atm2;

import java.util.ArrayList;

public class Contact {
    String name;
    int id;
    ArrayList<String> phone ;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    public Contact(ArrayList<String> phone) {
        this.phone = phone;
    }

    public Contact(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
