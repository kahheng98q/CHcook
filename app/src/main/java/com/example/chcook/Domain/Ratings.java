package com.example.chcook.Domain;

public class Ratings {
    private String rateId="";
    private double rating=0;
    private String date="";

    public Ratings() {
    }

    public Ratings(String rateId, double rating, String date) {
        this.rateId = rateId;
        this.rating = rating;
        this.date = date;
    }

    public String getRateId() {
        return rateId;
    }

    public void setRateId(String rateId) {
        this.rateId = rateId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
