package com.example.chcook;

public class User {
    private String favorite;
    private String gmail;
    private String history;
    private String like;
    private String video;
    private String type;

    public User() {
    }
    public User(String type) {
        this.type=type;
    }
    public User(String favorite, String gmail, String history, String like, String video,String type) {
        this.favorite = favorite;
        this.gmail = gmail;
        this.history = history;
        this.like = like;
        this.video = video;
        this.type=type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getFavorite() {
        return favorite;
    }
    public String getGmail() {
        return gmail;
    }
    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
    public String getVideo() {
        return video;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getLike() {
        return like;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getHistory() {
        return history;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
