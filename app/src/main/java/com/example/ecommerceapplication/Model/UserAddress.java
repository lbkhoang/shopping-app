package com.example.ecommerceapplication.Model;

public class UserAddress {
    private String address, city, state;

    public UserAddress() {

    }

    public UserAddress(String address, String city, String state) {
        this.address = address;
        this.city = city;
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
