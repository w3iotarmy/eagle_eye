package org.droidplanner.android.activities.helpers;

/**
 * Created by Borhan Uddin on 6/28/2018.
 */
public class LatLngAltModel {
    public double lat=0.00;
    public double lng=0.00;
    public double alt=0;
    public LatLngAltModel(double lat, double lng, double alt)
    {
        this.lat=lat;
        this.lng=lng;
        this.alt=alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAlt() {
        return alt;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
