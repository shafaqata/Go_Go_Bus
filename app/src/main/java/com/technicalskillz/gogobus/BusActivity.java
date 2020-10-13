package com.technicalskillz.gogobus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.technicalskillz.gogobus.Bus.BusList;
import com.technicalskillz.gogobus.Bus.NextBus;
import com.technicalskillz.gogobus.Bus.NextBusSimple;
import com.technicalskillz.gogobus.Bus.RecyclerViewAdapterBus;
import com.technicalskillz.gogobus.Bus.Service;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float GEOFENCE_RADIUS = 100;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";
    RecyclerView recyclerView;
    RecyclerViewAdapterBus adapterBus;
    Toolbar toolbar;
    MapView mapView;
    GoogleMap gMap;
    LatLng latLngBus;

    private static final int REQUEST_CODE_PERMISIION = 10101;
    private static final int ERROR_CODE = 1111;
    boolean allReadyAnimate = false;
    boolean mLocationPermisisionGranter;
    LatLng currentLatln, destinationLatLn;
    FusedLocationProviderClient mLocationClient;
    LocationCallback mLocationCallback;
    Button btnDistance;
    boolean notificationAllow = false;
    double StationDistance;
    MediaPlayer mediaPlayer;
    Switch aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Buses of a Station");
        mediaPlayer = MediaPlayer.create(this, R.raw.alaram);
        aSwitch = findViewById(R.id.switch1);

        mapView = findViewById(R.id.mapView);


        latLngBus = new LatLng(Double.valueOf(getIntent().getStringExtra("Latitude").trim()), Double.valueOf(getIntent().getStringExtra("Longitude").trim()));
        initMap();

        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(BusActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Toast.makeText(BusActivity.this, locationResult.getLastLocation().getLatitude()+"", Toast.LENGTH_SHORT).show();
                    Location location = locationResult.getLastLocation();
                    currentLatln = new LatLng(location.getLatitude(), location.getLongitude());
                    GotoMyLocation(location.getLatitude(), location.getLongitude());
                    double distance = SphericalUtil.computeDistanceBetween(currentLatln, latLngBus);
                    if (distance != 0.0) {
                        StationDistance = distance / 1000;
                        if (StationDistance < .1) {
                            if (notificationAllow) {
                                mediaPlayer.start();
                            } else {
//                                if (mediaPlayer!=null)
//                                {
//                                    mediaPlayer.stop();
//                                }
                            }

                        }
                        Log.d("my Distance", "onClick: " + distance / 1000 + "KM");
                        //extDetail.setText("Distance: " + distance / 1000 + "KM");
                    } else {
                        Log.d("my Distance", "onClick:  no found ");
                    }


                }
            }
        };


        if (isGpsEnabled()) {
            getLocationUpdate();
        } else {
            Toast.makeText(this, "Please Enable GPS Location", Toast.LENGTH_SHORT).show();
        }

        LoadBuses(getIntent().getStringExtra("BusStopCode"));
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    notificationAllow = true;
                } else {
                    notificationAllow = false;
                }
            }
        });


    }

    private void initMap() {

        if (isServicesOK()) {
            if (PermissionOK()) {
                Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
            } else {
                RequestPermission();
            }
        }
    }

    private boolean isServicesOK() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            Toast.makeText(this, "Google Play Services OK", Toast.LENGTH_SHORT).show();
            return true;
        } else if (apiAvailability.isUserResolvableError(result)) {
            Dialog dialog = apiAvailability.getErrorDialog(this, result, ERROR_CODE, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Dialog is Cancelled By User", Toast.LENGTH_SHORT).show();

                }

            });
            dialog.show();
        } else {
            Toast.makeText(this, "Google Play Service is required for Google maps", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private boolean PermissionOK() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISIION);
        }
    }

    private void LoadBuses(String BusID) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=" + BusID)
                .method("GET", null)
                .addHeader("AccountKey", "dDFDbRrMR1ujXVKA0+ZvIA==")
                .addHeader("accept", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                BusActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String res = response.body().string();
                    BusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //for Next Bus
                            Gson gson = new Gson();

                            //for Next Bus
                            BusList list = gson.fromJson(res, BusList.class);
                            List<Service> bus = list.getServices();
                            List<NextBusSimple> newBus = new ArrayList<>();
                            String allServicesCode = "Service Code: ";
                            for (int i = 0; i < bus.size(); i++) {
                                allServicesCode = allServicesCode + bus.get(i).getServiceNo().toString() + ", ";

                            }
                            GotoSationLocation(allServicesCode);
                            adapterBus = new RecyclerViewAdapterBus(bus, BusActivity.this);
                            adapterBus.notifyDataSetChanged();
                            recyclerView.setAdapter(adapterBus);
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISIION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            mLocationPermisisionGranter = true;
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gMap.setMyLocationEnabled(true);
        addCircle(latLngBus, GEOFENCE_RADIUS);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    boolean isGpsEnabled() {
        LocationManager locationManage = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        return locationManage.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void GotoSationLocation(String servicecode) {

        //  LatLng latLng = new LatLng(1.436923, 103.786516);
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(latLngBus, 15);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLngBus);
        markerOptions.title(servicecode);
        markerOptions.draggable(false);
        //gMap.addMarker(markerOptions);

        gMap.animateCamera(cameraUpdate1);

        gMap.clear();
        addMarker(latLngBus);
        addCircle(latLngBus, GEOFENCE_RADIUS);

    }

    private void GotoMyLocation(double latitude, double longitude) {

        //  LatLng latLng = new LatLng(1.436923, 103.786516);
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(latLngBus, 15);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLngBus);
        markerOptions.title("I am Here");
        markerOptions.draggable(false);
        //gMap.addMarker(markerOptions);

        gMap.animateCamera(cameraUpdate1);

        gMap.clear();
        addMarker(latLngBus);
        addCircle(latLngBus, GEOFENCE_RADIUS);

    }


    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        gMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);
        gMap.addCircle(circleOptions);
    }

    private void getLocationUpdate() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}