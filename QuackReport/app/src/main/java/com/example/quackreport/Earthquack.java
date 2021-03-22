package com.example.quackreport;

public class Earthquack {

    private double mMagnitude;
    private String mLocation;
    private long mTime;
    private String mUrl;

    public Earthquack(double mMagnitude, String mLocation, long mTime, String mUrl) {
        this.mMagnitude = mMagnitude;
        this.mLocation = mLocation;
        this.mTime = mTime;
        this.mUrl = mUrl;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliseconds() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }
}

