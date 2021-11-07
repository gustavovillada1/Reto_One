package com.example.reto_one;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UbicacionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {



    private GoogleMap mMap;
    private Marker markerDraw;
    private Geocoder geocoder;
    private LatLng ubicacion_negocio;
    private LocationManager manager;
    private double lat,lng;
    private String location;



    private FloatingActionButton floatingActionButton;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        geocoder= new Geocoder(this);
        floatingActionButton=(FloatingActionButton) findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hacer_callback();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapF);
        mapFragment.getMapAsync(this);
    }



    private void hacer_callback() {
        Intent data = new Intent();

        data.putExtra("latitud",lat);
        data.putExtra("longitud",lng);
        data.putExtra("location",location);

        setResult(RESULT_OK, data);
        finish();
    }

    @SuppressLint("MissingPermission")
    private Location setInitialPos(){
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        lat = 3.342028232124455;
        lng = -76.530660280504;

        ubicacion_negocio= new LatLng(lat,lng);
        Location loc = setInitialPos();
        if(loc!=null){
            this.ubicacion_negocio = new LatLng(loc.getLatitude(),loc.getLongitude());
        }

        location= obtenerDireccion(lat,lng);

        // Add a marker in Sydney and move the camera
        ubicacion_negocio = new LatLng(lat, lng);


        markerDraw = googleMap.addMarker(new MarkerOptions()
                .position(ubicacion_negocio)
                .title("Mi ubicación")
                .draggable(true)
        );


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion_negocio, 15));


        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);

    }


    private String obtenerDireccion(double lat, double lng){
        Address address;
        Address address1;
        String location="";
        try {
            List<Address> addressList = geocoder.getFromLocation(lat,lng,2);
            address= addressList.get(0);
            address1= addressList.get(1);
            location = address.getAddressLine(address1.getMaxAddressLineIndex());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

        if(marker.equals(markerDraw)){
            Toast.makeText(getApplicationContext(),"Arrastrando...",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMarkerDrag(Marker marker) {

        if(marker.equals(markerDraw)){
            String newTitle = String.format(Locale.getDefault(),
                    getString(R.string.marker_detail_latlng),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude);

            setTitle(newTitle);
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if(marker.equals(markerDraw)){
            Toast.makeText(getApplicationContext(),"Fin de arrastre...",Toast.LENGTH_SHORT).show();

            lat=marker.getPosition().latitude;
            lng=marker.getPosition().longitude;
            location=obtenerDireccion(lat,lng);
            marker.setTitle(location);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(marker.equals(markerDraw)){


         }
        return false;
    }
}