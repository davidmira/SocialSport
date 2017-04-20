package com.david.socialsport.Pantallas;

import android.os.Bundle;

import com.david.socialsport.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

public class CrearEvento extends AppCompatActivity implements OnMapReadyCallback, PlaceSelectionListener {

    private GoogleMap mMap;

    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    ActionBar menuBar;
    LinearLayout mapa;
    Button mostraMapa;
    int click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        menuBar = getSupportActionBar();
        menuBar.setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.crearEvento));

        mapa = (LinearLayout) findViewById(R.id.mapa);
        mapa.setVisibility(View.GONE);

        mostraMapa = (Button) findViewById(R.id.mostraMapa);
        mostraMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click++;
                if (click % 2 == 0 || mapa.getVisibility() == View.VISIBLE) {
                    mapa.setVisibility(View.GONE);
                } else {
                    if (mapa.getVisibility() == View.GONE) {
                        mapa.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        miUbicacion();
    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 14);

        if (marcador != null)
            marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .draggable(true));

        mMap.animateCamera(miUbicacion);
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);
        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };


    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locListener);
    }

    /**
     * @param location Añade un marcador en la posición buscada
     */
    private void buscarLocalizacion(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        agregarMarcador(currentLatitude, currentLongitude);
    }


    public void onLocationChanged(Location location) {
        buscarLocalizacion(location);
    }

    /**
     * @param place Controlamos lo que ocurre al seleccionar una ubicación en el campo de autocompletado
     */
    @Override
    public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        //Log.i(TAG, "Place: " + place.getName());
        LatLng autoCompleteLatLng = place.getLatLng();
        Location newLoc = new Location("New");
        newLoc.setLatitude(autoCompleteLatLng.latitude);
        newLoc.setLongitude(autoCompleteLatLng.longitude);
        onLocationChanged(newLoc);
    }

    @Override
    public void onError(Status status) {

        // TODO: Handle the error.
        // Log.i(TAG, "An error occurred: " + status);
    }

}
