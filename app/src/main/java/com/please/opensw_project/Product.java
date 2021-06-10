package com.please.opensw_project;

import android.location.Location;

public class Product {
    public String mLocation;
    public long price;
    public double latitude;
    public double longitude;
    public float distance;

    Product(String m, long p, double x, double y){
        this.mLocation = m;
        this.price = p;
        this.latitude = x;
        this.longitude = y;

        double mlonggitude = this.latitude;
        double mlatitude = this.longitude;


        Location mLocation = new Location("mLocation");
        mLocation.setLatitude(mlatitude);
        mLocation.setLongitude(mlonggitude);
        this.distance = InfoActivity.myLocation.distanceTo(mLocation);
    }

    @Override
    public String toString(){
        return this.mLocation + "//" + this.price;
    }
}
