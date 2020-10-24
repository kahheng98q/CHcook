package com.example.chcook.KahHeng.EndUser.Domain;

public class Report {
    private String date;
    private String reason;
    private String username;
    private String videoId;
    private String videoName;

    public Report(String date, String reason, String username, String videoId, String videoName) {
        this.date = date;
        this.reason = reason;
        this.username = username;
        this.videoId = videoId;
        this.videoName = videoName;
    }
    public String getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public String getUsername() {
        return username;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoName() {
        return videoName;
    }




}
