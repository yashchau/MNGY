package com.example.yashchauhan.mng;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globe.Globe;
import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;


/**
 * A simple {@link Fragment} subclass.
 */
public class Distance extends Fragment {

    private WorldWindow wwd;




    public WorldWindow createWorldWindow() {
        // Create the WorldWindow (a GLSurfaceView) which displays the globe.
        this.wwd = new WorldWindow(getContext());


        this.wwd.getLayers().addLayer(new BackgroundLayer());
        this.wwd.getLayers().addLayer(new BlueMarbleLandsatLayer());

        return this.wwd;
    }

    public Distance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_distance, container, false);

        FrameLayout globeLayout = (FrameLayout) rootView.findViewById(R.id.globe);

        Position aircraft = new Position(34.2, -119.2, 3000);           // Above Oxnard CA, altitude in meters
        Position airport = new Position(34.1192744, -119.1195850, 4.0); // KNTD airport, Point Mugu CA, altitude MSL

        Globe globe = wwd.getGlobe();
        // Compute heading and tilt angles from aircraft to airport

        double heading = aircraft.greatCircleAzimuth(airport);
        double distanceRadians = aircraft.greatCircleDistance(airport);
        double distance = distanceRadians * globe.getRadiusAt(aircraft.latitude, aircraft.longitude);
        double tilt = Math.toDegrees(Math.atan(distance / aircraft.altitude));
        String DDDD=String.valueOf(distance);



        globeLayout.addView(this.createWorldWindow());
        LookAt lookAt = new LookAt().set(46.202, -122.190, 500, WorldWind.ABSOLUTE, 2e4 /*range*/, 0 /*heading*/, 45 /*tilt*/, 0 /*roll*/);
        wwd.getNavigator().setAsLookAt(wwd.getGlobe(), lookAt);

        return rootView;

    }

}
