package com.example.chcook.Domain;

public class Histories {
    private String historyId="";
    private String historyDate="";
    private Videos videos=new Videos();

    public Histories(){

    }

    public Histories(String historyId, String historyDate, Videos videos) {
        this.historyId = historyId;
        this.historyDate = historyDate;
        this.videos = videos;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }
}
