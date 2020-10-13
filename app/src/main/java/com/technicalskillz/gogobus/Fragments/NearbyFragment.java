package com.technicalskillz.gogobus.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.technicalskillz.gogobus.BusStation.RecyclerviewAdapterStation;
import com.technicalskillz.gogobus.BusStation.Station;
import com.technicalskillz.gogobus.BusStation.StationList;
import com.technicalskillz.gogobus.BusStation.Value;
import com.technicalskillz.gogobus.MainActivity;
import com.technicalskillz.gogobus.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NearbyFragment extends Fragment implements OnMapReadyCallback {

    Context ctx;
    RecyclerView recyclerView;
    RecyclerviewAdapterStation adapterStation;
    MapView mapView;
    GoogleMap gMap;

    private static final int REQUEST_CODE_PERMISIION = 10101;
    private static final int ERROR_CODE = 1111;
    boolean allReadyAnimate = false;
    boolean mLocationPermisisionGranter;
    LatLng currentLatln;
    List<Value> stations = null;


    //location
    FusedLocationProviderClient mLocationClient;
    LocationCallback mLocationCallback;


    public NearbyFragment() {
        // Required empty public constructor
    }

    public NearbyFragment(Context context) {
        // Required empty public constructor
        ctx = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        mapView = view.findViewById(R.id.mapView);
        initMap();
        setHasOptionsMenu(true);

        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        mLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(ctx, "Location not found", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    Location location = locationResult.getLastLocation();
                    currentLatln = new LatLng(location.getLatitude(), location.getLongitude());
                    // Toast.makeText(ctx, location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                    GotoMyLocation(location.getLatitude(), location.getLongitude());


                }
            }
        };

        if (isGpsEnabled()) {
            getLocationUpdate();
        } else {
            Toast.makeText(ctx, "Please Enable GPS Location", Toast.LENGTH_SHORT).show();
        }


        LoadStaion();
        return view;
    }

    private void CalculateDistance() {
        if (stations.size() > 0) {
            List<Station> station = new ArrayList<>();

            for (int i = 0; i < stations.size(); i++) {
                Station station1;

                LatLng latLngStaion = new LatLng(stations.get(i).getLatitude(), stations.get(i).getLongitude());

                if (latLngStaion != null && currentLatln != null) {
                    double distance = SphericalUtil.computeDistanceBetween(currentLatln, latLngStaion) / 1000;
                    station1 = new Station(distance, stations.get(i).getBusStopCode(), stations.get(i).getRoadName(), stations.get(i).getDescription()
                            , stations.get(i).getLatitude(), stations.get(i).getLongitude());
                    station.add(station1);
                }
                // Log.d("myDistance", "CalculateDistance: " + station.get(i).getBusStopCode() + "=" + station.get(i).getDistance() + "");
            }
            Collections.sort(station, new Comparator<Station>() {
                @Override
                public int compare(Station a1, Station a2) {
                    return Double.compare(a1.getDistance(), a2.getDistance());
                }
            });
            Log.d("totalStation", "Total Station: " + station.size());

            adapterStation = new RecyclerviewAdapterStation(station, ctx);
            recyclerView.setAdapter(adapterStation);
//            for (int j = 0; j < station.size(); j++) {
//                Log.d("myDistance", "CalculateDistance: " + station.get(j).getBusStopCode() + "=" + station.get(j).getDistance() + "");
//            }
        }
    }


    private void LoadStaion() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://datamall2.mytransport.sg/ltaodataservice/BusStops")
                .method("GET", null)
                .addHeader("AccountKey", "dDFDbRrMR1ujXVKA0+ZvIA==")
                .addHeader("accept", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String res = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            StationList list = gson.fromJson(res, StationList.class);
                            stations = list.getValue();
                            CalculateDistance();


                        }
                    });
                }

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initMap() {

        if (isServicesOK()) {
            if (PermissionOK()) {
                Toast.makeText(ctx, "Map is Ready", Toast.LENGTH_SHORT).show();
            } else {
                RequestPermission();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.refresh)
        {
            LoadStaion();
            Toast.makeText(ctx, "call", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private boolean isServicesOK() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(ctx);
        if (result == ConnectionResult.SUCCESS) {
            Toast.makeText(ctx, "Google Play Services OK", Toast.LENGTH_SHORT).show();
            return true;
        } else if (apiAvailability.isUserResolvableError(result)) {
            Dialog dialog = apiAvailability.getErrorDialog(getActivity(), result, ERROR_CODE, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(ctx, "Dialog is Cancelled By User", Toast.LENGTH_SHORT).show();

                }

            });
            dialog.show();
        } else {
            Toast.makeText(ctx, "Google Play Service is required for Google maps", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private boolean PermissionOK() {
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermission() {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISIION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISIION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(ctx, "Permission Granted", Toast.LENGTH_SHORT).show();
            mLocationPermisisionGranter = true;
        } else {
            Toast.makeText(ctx, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
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
        LocationManager locationManage = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        return locationManage.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void getLocationUpdate() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void GotoMyLocation(final double lat, double lng) {


        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(latLng, 4);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("I am Here");
        markerOptions.draggable(false);
        gMap.addMarker(markerOptions);

        gMap.animateCamera(cameraUpdate1);

    }


}