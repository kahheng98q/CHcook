package com.example.chcook.Domain;

public class Report {
    private String date;
    private String reason;
    private String username;
    private String videoId;
    private String videoName;

    public String getReportId() {
        return reportId;
    }

    private String reportId;

    public Report(String reportId,String date, String reason, String username, String videoId, String videoName) {
        this.date = date;
        this.reportId=reportId;
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
