package com.demo;

import java.sql.Date;

public class Donation {
    private int id;
    private String firstName;
    private String lastName;
    private String quantity;
    private String type;
    private Date date;

    public Donation () {
    }

    public Donation (String firstName, String lastName, String quantity, String type, Date date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.quantity = quantity;
        this.type = type;
        this.date = date;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}