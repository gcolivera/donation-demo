package com.demo;

public class Donation {
    private String name;
    private String quantity;
    private String type;
    private String date;

    public Donation () {
    }

    public Donation (String name, String quantity, String type, String date) {
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.date = date;
    }

    //getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

}