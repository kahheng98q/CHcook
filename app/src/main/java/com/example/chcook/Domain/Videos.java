package com.example.chcook.Domain;

import android.graphics.drawable.Drawable;

import java.net.URI;

public class Videos {

    private String videoID;
    private String name;
    private String desc;
    private String view;
    private String video;
    private String date;
    private String image;
    private String user;


    public Videos() {
    }


    public Videos(String videoID,String name, String video, String date) {
        this.videoID = videoID;
        this.name = name;
        this.video = video;
        this.date = date;
    }

    public Videos(String videoID, String name, String view, String date, String image,String user) {
        this.videoID = videoID;
        this.name = name;
        this.view = view;
        this.date = date;
        this.image = image;
        this.user=user;
    }
    public Videos(String videoID, String name, String view, String date, String image,String user,String desc) {
        this.videoID = videoID;
        this.name = name;
        this.view = view;
        this.date = date;
        this.image = image;
        this.user=user;
        this.desc=desc;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getView() {
        return view;
    }

    public String getImage() {
        return image;
    }

    public String getUser() {
        return user;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
