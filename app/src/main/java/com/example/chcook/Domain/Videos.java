package com.example.chcook.Domain;

public class Videos {

    private String videoID;
    private String name;
    private String desc;
    private String view;
    private String video;
    private String date;
    private String image;
    private String banned;
    private String category;
    private User user;



    public Videos() {
    }


    public Videos(String videoID,String name, String video, String date) {
        this.videoID = videoID;
        this.name = name;
        this.video = video;
        this.date = date;
    }
    public Videos(String videoId,String name, String date, String image, String desc){
        this.videoID = videoId;
        this.name= name;
        this.date = date;
        this.image = image;
        this.desc = desc;
    }

    public Videos(String videoID, String name, String view, String date, String video,User user) {
        this.videoID = videoID;
        this.name = name;
        this.view = view;
        this.date = date;
        this.video = video;
        this.user=user;
    }
    public Videos(String videoID, String name, String view, String date, String image,User user,String desc) {
        this.videoID = videoID;
        this.name = name;
        this.view = view;
        this.date = date;
        this.image = image;
        this.user=user;
        this.desc=desc;
    }
    public Videos(String banned, String name, String category, String date, String desc,String view,String videoID,String image) {
        this.name = name;
        this.category = category;
        this.banned =banned;
        this.date = date;
        this.desc=desc;
    }


    public User getUser() {
        return user;
    }

    public String getCategory() {
        return category;
    }


    public void setUser(User user) {
        this.user = user;
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


    public void setCategory(String category) {
        this.category = category;
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
