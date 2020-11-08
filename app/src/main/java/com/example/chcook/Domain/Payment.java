package com.example.chcook.Domain;

import java.util.Date;

public class Payment {
    private String payDate;
    private Integer Price;

    private User user;
    private String userName;
    private String userEmail;
    private String userId;

    public String getPayDate() {
        return payDate;
    }

    public Integer getPrice() {
        return Price;
    }

    public String getUserId() {
        return userId;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Payment(String payDate, Integer price) {
        this.payDate = payDate;
        Price = price;

    }
    public Payment(String payDate, Integer price,String userName,String userEmail) {
        this.payDate = payDate;
        this.Price = price;
        this.userEmail=userEmail;
        this.userName=userName;

    }
    public Payment(){

    }

    public Payment(String payDate, Integer price, String userId) {
        this.payDate = payDate;
        this.Price = price;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }


}
