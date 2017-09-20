package com.ingenious.equakes.model;

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private Long mDateTime;
    private double mLongitude;
    private double mLatitude;

    public Earthquake() {
        mMagnitude = 0.0;
        mLocation = "";
        mDateTime = 0L;
        mLongitude = 0.0;
        mLatitude = 0.0;
    }

    public Earthquake(double mMagnitude, String mLocation, Long mDateTime, double mLongitude, double mLatitude) {
        this.mMagnitude = mMagnitude;
        this.mLocation = mLocation;
        this.mDateTime = mDateTime;
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;

    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public Long getmDateTime() {
        return mDateTime;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmMagnitude(double mMagnitude) {
        this.mMagnitude = mMagnitude;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmDateTime(Long mDateTime) {
        this.mDateTime = mDateTime;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }
}
