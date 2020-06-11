package com.example.ecommerceapplication.Model;

public class OrderList {

    private String cost;
    private String note;
    private String orderId;
    private String userId;
    private String address;

    public OrderList(){

    }

    public OrderList(String cost, String note, String orderId, String userId, String address) {
        this.cost = cost;
        this.note = note;
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
