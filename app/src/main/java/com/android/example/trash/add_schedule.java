package com.android.example.trash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class add_schedule extends AbsRuntimePermission implements OnMapReadyCallback, com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient apiClient;
    private LocationRequest locationRequest;
    List<Address> addresses = null;
    private Marker marker;

    Button the_btn_add_sub;
    Spinner the_city_spin;
    Spinner the_bundle_spin;
    Spinner the_shift_spin;
    EditText the_title_et;

    String marker_location;

    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users = root.child("Users");
    DatabaseReference subs = root.child("subscriptions"); // gadwal el subs


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);


        the_btn_add_sub = (Button) findViewById(R.id.btn_add);

        the_city_spin = (Spinner) findViewById(R.id.city_spin);
        the_bundle_spin = (Spinner) findViewById(R.id.bundle_spin);
        the_shift_spin = (Spinner) findViewById(R.id.shift_spin);
        the_title_et = (EditText) findViewById(R.id.title_et);

        String[] city_items = new String[]{"Alexandria,Miami", "Alexandria,Glim", "Alexandria,Kafr Abdo", "Cairo,Ma'ady", "Cairo,Tagamoa el Khames", "Cairo,Madenet Nasr", "Giza,Zamalek", "Giza,Maryoteya", "Giza,Haram"};
        String[] bundle_items = new String[]{"Bronze", "Silver", "Golden"};
        String[] shift_items = new String[]{"Morning Shift", "Night Shift"};

        ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, city_items);
        ArrayAdapter<String> bundle_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bundle_items);
        ArrayAdapter<String> shift_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, shift_items);

        the_city_spin.setAdapter(city_adapter);
        the_bundle_spin.setAdapter(bundle_adapter);
        the_shift_spin.setAdapter(shift_adapter);
        the_btn_add_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker == null) {
                    Toast.makeText(getApplicationContext(), "Please Insert The Location", Toast.LENGTH_LONG).show();
                } else {
                    create_sub();
                }

            }
        });

    }

    private void create_sub() {
        SharedPreferences sharedPreferences = getSharedPreferences("users", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "14");

        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        subscriptions subscription = new subscriptions(uid,
                the_title_et.getText().toString().trim(),
                ts,
                the_shift_spin.getSelectedItem().toString(),
                "pending",
                marker_location,
                the_bundle_spin.getSelectedItem().toString());

        String sub_id = UUID.randomUUID().toString();
        String city_location = the_city_spin.getSelectedItem().toString().trim();
        String[] spliting = city_location.split(",");

        subs.child(spliting[0]).child(spliting[1]).child(sub_id).setValue(subscription);

        users.child(uid).child("subscribtions").child(sub_id).setValue(city_location);

        Toast.makeText(add_schedule.this, "done", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // paste last massage i sent
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestAppPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION}
                    , R.string.massage
                    , 50);
            return;
        }
        mMap.setMyLocationEnabled(true);
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.moveCamera(update);

                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() >= 0) {
                    marker_location = addresses.get(0).getAddressLine(0) + " & " + addresses.get(0).getAddressLine(1) + " &" + addresses.get(0).getAddressLine(2);
                }

                if (marker != null) {
                    marker.remove();
                }
                MarkerOptions options = new MarkerOptions()
                        .position(latLng);
                marker = mMap.addMarker(options);
            }
        });

    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestAppPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION}
                    , R.string.massage
                    , 50);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
            return;
        }
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(update);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    mMap.moveCamera(update);

                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() >= 0) {
                        marker_location = addresses.get(0).getAddressLine(0) + " & " + addresses.get(0).getAddressLine(1) + " &" + addresses.get(0).getAddressLine(2);
                    }

                    if (marker != null) {
                        marker.remove();
                    }
                    MarkerOptions options = new MarkerOptions()
                            .position(latLng);
                    marker = mMap.addMarker(options);
                }
            });

        }
    }

}
