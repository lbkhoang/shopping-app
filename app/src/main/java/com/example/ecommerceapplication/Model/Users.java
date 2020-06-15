package com.example.ecommerceapplication.Model;

public class Users
{
    private String name;
    private String phone;
    private String password;
    private String image;
    private String role;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Users()
    {

    }

    public Users(String name, String phone, String password, String image, String role, String description) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.role = role;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
