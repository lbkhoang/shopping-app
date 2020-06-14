package com.example.ecommerceapplication.Model;

public class ConfirmedOrder {

    private String address, cost, date, note, orderId, payMethod, time, userId;

    public ConfirmedOrder(){

    }

    public ConfirmedOrder(String address, String cost, String date, String note, String orderId, String payMethod, String time, String userId) {
        this.address = address;
        this.cost = cost;
        this.date = date;
        this.note = note;
        this.orderId = orderId;
        this.payMethod = payMethod;
        this.time = time;
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
