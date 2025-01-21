package com.example.prj.netflix_analyzer.model;



import java.time.Duration;
import java.util.Objects;

public class ViewingActivity {

    private String profile;
    private String startTime;
    private Duration duration;
    private String title;
    private String videoType;
    private String year;
    private String device;
    private String country;

    public ViewingActivity(String profile, String startTime, Duration duration, String title, String videoType, String year, String device, String country) {
        this.profile = profile;
        this.startTime = startTime;
        this.duration = duration;
        this.title = title;
        this.videoType = videoType;
        this.year = year;
        this.device = device;
        this.country = country;
    }

    public ViewingActivity( ){

    }

    public String getProfile() {
        return profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoType() {
        return videoType;
    }

    public String getYear() {
        return year;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewingActivity that = (ViewingActivity) o;
        return Objects.equals(profile, that.profile) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, title);
    }

}
