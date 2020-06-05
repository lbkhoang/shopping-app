package com.example.ecommerceapplication.Model;

public class OrderList {

    private String cost;

    private String note;

    public OrderList(){

    }

    public OrderList(String cost, String note) {
        this.cost = cost;
        this.note = note;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
