package com.example.yashchauhan.mng;

//import android.app.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globe.BasicElevationCoverage;
import gov.nasa.worldwind.globe.Globe;
import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.Placemark;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

//import gov.nasa.worldwind.shape.Placemark;

//import gov.nasa.worldwind.shape.Placemark;

/**
 * Created by yashchauhan on 31/10/17.
 */

public class BasicGlobeFragment extends Fragment {

    public WorldWindow wwd;
    private static final double NORMAL_IMAGE_SCALE = 3.0;

    public FrameLayout globeLayout;

    private static final double HIGHLIGHTED_IMAGE_SCALE = 4.0;
    public Placemark sightlinePlacemark;
    // Globe globe = wwd.getGlobe();

    double lat1=34.2,lat2=34.1192744,lon1=-119.2,lon2=-119.1195850,alt1=3000,alt2=4;

    double x1 = alt1 * cos(lat1) * sin(lon1);
    double y1 = alt1 * sin(lat1);
    double z1 = alt1 * cos(lat1) * cos(lon1);
    double x2 = alt2 * cos(lat2) * sin(lon2);
    double y2 = alt2 * sin(lat2);
    double z2 = alt2 * cos(lat2) * cos(lon2);
    TextView dist,dist2;

    // WorldWindow wwd = super.createWorldWindow();

    RenderableLayer layer = new RenderableLayer("Placemarks");



    public BasicGlobeFragment() {
    }

    /**
     * Creates a new WorldWindow (GLSurfaceView) object.
     */
    public WorldWindow createWorldWindow() {
        // Create the WorldWindow (a GLSurfaceView) which displays the globe.
        this.wwd = new WorldWindow(getContext());

        // Setup the WorldWindow's layers.
       this.wwd.getLayers().addLayer(new BackgroundLayer());
        this.wwd.getLayers().addLayer(new BlueMarbleLandsatLayer());
        // Setup the WorldWindow's elevation coverages.
        this.wwd.getGlobe().getElevationModel().addCoverage(new BasicElevationCoverage());
        Position pos = new Position(12.9723793, 77.685245, 0);

        sightlinePlacemark = new Placemark(pos);
        this.sightlinePlacemark.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        this.sightlinePlacemark.getAttributes().setImageSource(ImageSource.fromResource(R.drawable.aircraft_fixwing));
        this.sightlinePlacemark.getAttributes().setImageScale(2);
        this.sightlinePlacemark.getAttributes().setDrawLeader(true);

        RenderableLayer sightlineLayer = new RenderableLayer();
        sightlineLayer.addRenderable(sightlinePlacemark);
        this.wwd.getLayers().addLayer(sightlineLayer);



        return this.wwd;
    }

    /**
     * Gets the WorldWindow (GLSurfaceView) object.
     */
    public WorldWindow getWorldWindow() {
        return this.wwd;
    }


    /**
     * Adds the WorldWindow to this Fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_globe, container, false);
        globeLayout = (FrameLayout) rootView.findViewById(R.id.globe);

        globeLayout.addView(this.createWorldWindow());

        dist=(TextView) rootView.findViewById(R.id.lat_value);
        dist2=(TextView)rootView.findViewById(R.id.lon_value);
        LookAt lookAt = new LookAt().set(12.9723793, 77.685245, 0, WorldWind.ABSOLUTE, 2e4 /*range*/, 0 /*heading*/, 45 /*tilt*/, 0 /*roll*/);
        wwd.getNavigator().setAsLookAt(wwd.getGlobe(), lookAt);

        Position aircraft = new Position(34.2, -119.2, 3000);           // Above Oxnard CA, altitude in meters
        Position airport = new Position(34.1192744, -119.1195850, 4.0); // KNTD airport, Point Mugu CA, altitude MSL

        double dist_self = sqrt(pow((x2-x1),2) + pow((y2-y1),2) + pow((z2-z1),2));
        String mmmm=String.valueOf(dist_self);
        dist2.setText(mmmm);
        // Compute heading and tilt angles from aircraft to airport
        Globe globe = wwd.getGlobe();
        double heading = aircraft.greatCircleAzimuth(airport);
        double distanceRadians = aircraft.greatCircleDistance(airport);
        double distance = distanceRadians * globe.getRadiusAt(aircraft.latitude, aircraft.longitude);
        double tilt = Math.toDegrees(Math.atan(distance / aircraft.altitude));
        String DDDD=String.valueOf(distance);
        dist.setText(DDDD);
        return rootView;
    }


    /**
     * Resumes the WorldWindow's rendering thread
     */
    @Override
    public void onResume() {
        super.onResume();
        this.wwd.onResume(); // resumes a paused rendering thread
    }

    /**
     * Pauses the WorldWindow's rendering thread
     */
    @Override
    public void onPause() {
        super.onPause();
        this.wwd.onPause(); // pauses the rendering thread
    }
}
