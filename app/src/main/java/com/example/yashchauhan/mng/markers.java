package com.example.yashchauhan.mng;

import gov.nasa.worldwind.geom.Position;

/**
 * Created by yashchauhan on 05/12/17.
 */

public class markers extends BasicGlobeFragment {
//    private WorldWindow wwd;
//
//    public WorldWindow createWorldWindow() {
//        // Create the WorldWindow (a GLSurfaceView) which displays the globe.
//        this.wwd = new WorldWindow(getContext());
//
//
//        this.wwd.getLayers().addLayer(new BackgroundLayer());
//        this.wwd.getLayers().addLayer(new BlueMarbleLandsatLayer());
//
//        return this.wwd;
//    }


    double lat1=34.2,lat2=34.1192744,lon1=-119.2,lon2=-119.1195850,alt1=3000,alt2=4;


    Position aircraft = new Position(lat1, lon1, alt1);           // Above Oxnard CA, altitude in meters
    Position airport = new Position(lat2, lon2, alt2); // KNTD airport, Point Mugu CA, altitude MSL

//     Compute heading and tilt angles from aircraft to airport

    double heading = aircraft.greatCircleAzimuth(airport);
    double distanceRadians = aircraft.greatCircleDistance(airport);
  //
  //  double distance = distanceRadians * globe.getRadiusAt(aircraft.latitude, aircraft.longitude);
    double distance = distanceRadians*6371000;
    double tilt = Math.toDegrees(Math.atan(distance / aircraft.altitude));
    public String DDDD;

    void caldist(){
         aircraft = new Position(lat1, lon1, alt1);           // Above Oxnard CA, altitude in meters
         airport = new Position(lat2, lon2, alt2); // KNTD airport, Point Mugu CA, altitude MSL

//     Compute heading and tilt angles from aircraft to airport

        double heading = aircraft.greatCircleAzimuth(airport);
         distanceRadians = aircraft.greatCircleDistance(airport);
        //
        //  double distance = distanceRadians * globe.getRadiusAt(aircraft.latitude, aircraft.longitude);
         distance = distanceRadians*6371000;
         tilt = Math.toDegrees(Math.atan(distance / aircraft.altitude));
         DDDD=String.valueOf(distance);

    }

    public String input(double a,double b,double c,double d,double e,double f){
        lat1=a;
        lon1=b;
        alt1=c;
        lat2=d;
        lon2=e;
        alt2=f;
        caldist();
        return DDDD;

    }


    public String test(){
        return DDDD;
    }


}
