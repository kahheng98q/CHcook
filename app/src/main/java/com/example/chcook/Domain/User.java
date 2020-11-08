package com.example.chcook.Domain;

public class User{
    private String status="";
    private String email="";
    private String image="";
    private String type="";
    private String name="";
    private String password="";

    public User() {

    }

    public User(String type) {
        this.type=type;
    }

    public User(String name,String email){
        this.email=email;
        this.name=name;
    }

    public User(String status, String email, String Image, String type, String name, String password) {
        this.status = status;
        this.email = email;
        this.image = Image;
        this.type = type;
        this.name = name;
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
