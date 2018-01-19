package com.example.yashchauhan.mng;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import gov.nasa.worldwind.NavigatorEvent;
import gov.nasa.worldwind.NavigatorListener;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Camera;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.Color;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.OmnidirectionalSightline;
import gov.nasa.worldwind.shape.Placemark;
import gov.nasa.worldwind.shape.PlacemarkAttributes;
import gov.nasa.worldwind.shape.ShapeAttributes;

//import android.app.Fragment;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link OmnidirectionalSightlineFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link OmnidirectionalSightlineFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class OmnidirectionalSightlineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView dist,dddd,latlon;
    markers mark;
    protected ImageView crosshairs;
    private long lastEventTime;
    private Camera camera = new Camera();
    private LookAt lookAt = new LookAt();
    private AnimatorSet animatorSet;
    private boolean crosshairsActive;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public Placemark sightlinePlacemark;
    //  OmnidirectionalSightline sightline = new OmnidirectionalSightline(position, range);
    public OmnidirectionalSightline sightline;

    public OmnidirectionalSightlineFragment() {
        // Required empty public constructor
    }

    private WorldWindow wwd;


    /**
     * Creates a new WorldWindow (GLSurfaceView) object.
     */
    public WorldWindow createWorldWindow() {
        // Create the WorldWindow (a GLSurfaceView) which displays the globe.
        this.wwd = new WorldWindow(getContext());

        ShapeAttributes viewableRegions = new ShapeAttributes();
        viewableRegions.setInteriorColor(new Color(0f, 0.5f, 0f, 0.5f));

        ShapeAttributes blockedRegions = new ShapeAttributes();
        blockedRegions.setInteriorColor(new Color(0.1f, 0.1f, 0.1f, 0.8f));

        Position pos = new Position(46.202, -122.190, 500.0);
        this.sightline = new OmnidirectionalSightline(pos, 10000.0);
        this.sightline.setAttributes(viewableRegions);
        this.sightline.setOccludeAttributes(blockedRegions);
        this.sightlinePlacemark = new Placemark(pos);
        this.sightlinePlacemark.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        this.sightlinePlacemark.getAttributes().setImageSource(ImageSource.fromResource(R.drawable.aircraft_fixwing));
        this.sightlinePlacemark.getAttributes().setImageScale(2);
        this.sightlinePlacemark.getAttributes().setDrawLeader(true);



        // Create a layer for the Sightline
        RenderableLayer sightlineLayer = new RenderableLayer();
        sightlineLayer.addRenderable(sightline);
        sightlineLayer.addRenderable(sightlinePlacemark);
        this.wwd.getLayers().addLayer(sightlineLayer);

        this.wwd.getLayers().addLayer(new BackgroundLayer());
        this.wwd.getLayers().addLayer(new BlueMarbleLandsatLayer());
        this.wwd.getLayers().addLayer(sightlineLayer);
      //  this.wwd.getLayers().addLayer(sightlinePlacemark);
        return this.wwd;
    }

    /**
     * Gets the WorldWindow (GLSurfaceView) object.
     */
    public WorldWindow getWorldWindow() {
        return this.wwd;
    }


    protected void createPlacemark(Position position, RenderableLayer layer) {
        Placemark placemark = new Placemark(position);
        placemark.getAttributes().setImageSource(ImageSource.fromResource(R.drawable.aircraft_fixwing));
        placemark.getAttributes().setImageScale(2);
        placemark.getAttributes().setDrawLeader(true);
        placemark.setHighlightAttributes(new PlacemarkAttributes(placemark.getAttributes()).setImageScale(4));
        layer.addRenderable(placemark);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_omnidirectional_sightline, container, false);
        mark=new markers();
        latlon=(TextView) rootView.findViewById(R.id.textView4);
        this.crosshairs = (ImageView) rootView.findViewById(R.id.globe_crosshairs1);
        FrameLayout globeLayout = (FrameLayout) rootView.findViewById(R.id.globe);
        dist=(TextView) rootView.findViewById(R.id.textView2);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(this.crosshairs, "alpha", 0f).setDuration(1500);
        fadeOut.setStartDelay((long) 500);
        this.animatorSet = new AnimatorSet();
        this.animatorSet.play(fadeOut);



        dddd=(TextView) rootView.findViewById(R.id.textView3);

//        latlon.setText("is this working");
        mark.test();
        dist.setText(mark.test());
        // Add the WorldWindow view object to the layout that was reserved for the globe.
        globeLayout.addView(this.createWorldWindow());
        lookAt = new LookAt().set(46.202, -122.190, 500, WorldWind.ABSOLUTE, 2e4 /*range*/, 0 /*heading*/, 45 /*tilt*/, 0 /*roll*/);
        wwd.getNavigator().setAsLookAt(wwd.getGlobe(), lookAt);


        NavigatorListener listener = new NavigatorListener() {
            @Override
            public void onNavigatorEvent(WorldWindow wwd, NavigatorEvent event) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastEventTime;
                int eventAction = event.getAction();
                boolean receivedUserInput = (eventAction == WorldWind.NAVIGATOR_MOVED && event.getLastInputEvent() != null);

                // Update the status overlay views whenever the navigator stops moving,
                // and also it is moving but at an (arbitrary) maximum refresh rate of 20 Hz.
                if (eventAction == WorldWind.NAVIGATOR_STOPPED || elapsedTime > 50) {

                    // Get the current navigator state to apply to the overlays
                    event.getNavigator().getAsLookAt(wwd.getGlobe(), lookAt);
                    event.getNavigator().getAsCamera(wwd.getGlobe(), camera);

                    // Update the overlays
                    updateOverlayContents( camera);
                  //  updateOverlayColor(eventAction);

                    lastEventTime = currentTime;
                }
                Log.d("listener","posiotion 1");
                showCrosshairs();
                // Show the crosshairs while the user is gesturing and fade them out after the user stops

            }
        };
        this.getWorldWindow().addNavigatorListener(listener);

        return rootView;

    }


    protected void showCrosshairs() {
        if (this.animatorSet.isStarted()) {
            this.animatorSet.cancel();
        }
        this.crosshairs.setAlpha(1.0f);
        this.crosshairsActive = true;
    }

    protected void updateOverlayContents( Camera camera) {
        latlon.setText((int) lookAt.latitude + " , " + (int) lookAt.longitude+" , "+(int) camera.altitude);
        Log.d("text","position 2");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
