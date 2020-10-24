package com.example.chcook.Domain;

public class Favorite {
    private String favoriteId="";
    private String favoriteDate="";
    private Videos videos=new Videos();

    public Favorite(String favoriteId, String favoriteDate, Videos videos) {
        this.favoriteId = favoriteId;
        this.favoriteDate = favoriteDate;
        this.videos = videos;
    }

    public Favorite() {
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getFavoriteDate() {
        return favoriteDate;
    }

    public void setFavoriteDate(String favoriteDate) {
        this.favoriteDate = favoriteDate;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }
}
