package com.ingenious.equakes;

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private Long mDateTime;
    private double mLongitude;
    private double mLatitude;


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

}
