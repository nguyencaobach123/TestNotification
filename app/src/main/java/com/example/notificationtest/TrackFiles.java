package com.example.notificationtest;

public class TrackFiles {
    private String title;
    private String artist;
    private int thumbnail;

    public TrackFiles(String title, String artist, int thumbnail) {
        this.title = title;
        this.artist = artist;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
