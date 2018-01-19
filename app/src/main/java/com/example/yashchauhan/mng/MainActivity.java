package com.example.yashchauhan.mng;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private boolean isChecked = false;
    GoogleApiClient mGoogleApiClient;
   public TextView textView,distance;
   EditText lat1,lon1,alt1,lat2,lon2,alt2;
    Button btn;
    Location mLastLocation, mCurrentLocation;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    String mLastUpdateTime;
    markers mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        distance = (TextView) findViewById(R.id.textView3);
        lat1 = (EditText) findViewById(R.id.lat1);
        lon1 = (EditText) findViewById(R.id.lon1);
        alt1 = (EditText) findViewById(R.id.alt1);
        lat2 = (EditText) findViewById(R.id.lat2);
        lon2 = (EditText) findViewById(R.id.lon2);
        alt2 = (EditText) findViewById(R.id.alt2);
        btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                double S1 = Double.parseDouble(lat1.getText().toString());
                double S2 = Double.parseDouble(lon1.getText().toString());
                double S3 = Double.parseDouble(alt1.getText().toString());
                double S4 = Double.parseDouble(lat2.getText().toString());
                double S5 = Double.parseDouble(lon2.getText().toString());
                double S6 = Double.parseDouble(alt2.getText().toString());
                String str = mark.input(S1, S2, S3, S4, S5, S6);
                Log.d("you can do this", "onClick: btn pressed");
                distance.setText(str);

            }
        });
        double lat1 = 34.2, lat2 = 34.1192744, lon1 = -119.2, lon2 = -119.1195850, alt1 = 3000, alt2 = 4;
        mark = new markers();
        mark.input(lat1, lon1, alt1, lat2, lon2, alt2);
        distance.setText(mark.test());
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            Ruler ruler = new Ruler();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container,ruler);
            transaction.commit();

            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
            displayLocation();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }





    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            String lat = String.valueOf(latitude);
            String lon = String.valueOf(longitude);


            textView.setText(latitude + ", " + longitude);

        } else {

            textView.setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable_distance = menu.findItem(R.id.action_distance);
        MenuItem checkable_ruler = menu.findItem(R.id.action_ruler);
        MenuItem checkable_protractor = menu.findItem(R.id.action_protractor);
        checkable_distance.setChecked(isChecked);
        checkable_ruler.setChecked(isChecked);
        checkable_protractor.setChecked(isChecked);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.action_distance:
                if (!isChecked) {
                    distance.setVisibility(View.VISIBLE);

                    lat1.setVisibility(View.VISIBLE);
                    lat2.setVisibility(View.VISIBLE);
                    lon1.setVisibility(View.VISIBLE);
                    lon2.setVisibility(View.VISIBLE);
                    alt1.setVisibility(View.VISIBLE);
                    alt2.setVisibility(View.VISIBLE);

                 }else
                {
                    distance.setVisibility(View.INVISIBLE);

                    lat1.setVisibility(View.INVISIBLE);
                    lat2.setVisibility(View.INVISIBLE);
                    lon1.setVisibility(View.INVISIBLE);
                    lon2.setVisibility(View.INVISIBLE);
                    alt1.setVisibility(View.INVISIBLE);
                    alt2.setVisibility(View.INVISIBLE);}

            case R.id.action_ruler:
                Ruler ruler = new Ruler();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment2,ruler);
                transaction.commit();

        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_distance) {
            isChecked = !item.isChecked();
            item.setChecked(isChecked);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            BasicGlobeFragment globeFragment=new BasicGlobeFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayoutforfragmant,globeFragment).commit();

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            OmnidirectionalSightlineFragment LOS=new OmnidirectionalSightlineFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayoutforfragmant,LOS).commit();

        } else if (id == R.id.nav_slideshow) {

            Distance LOS=new Distance();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayoutforfragmant,LOS).commit();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("TAG", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();
        updateUI();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAG", "Firing onLocationChanged..............................................");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        // mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d("TAG", "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            textView.setText(lat + ", " + lng);
        } else {
            Log.d("TAG", "location is null ...............");
        }
    }

}
