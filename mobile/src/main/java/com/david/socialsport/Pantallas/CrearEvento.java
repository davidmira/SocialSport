package com.david.socialsport.Pantallas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrearEvento extends AppCompatActivity implements OnMapReadyCallback, PlaceSelectionListener {

    private GoogleMap mMap;

    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    ActionBar menuBar;
    LinearLayout mapa;
    int click;

    private LatLng coordenadas;


    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    Button crear, mostraMapa;
    static EditText tipoLugar, comentario, precio, fecha, hora;
    static AutoCompleteTextView deporte;
    String ubicacionEvento;

    static int evYear, evMonth, evDay, evHour, evMinute;
    static String evLocalizacion;


    private static final int OPEN_REQUEST_CODE = 41;


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

        tipoLugar = (EditText) findViewById(R.id.crear_tipo);
        comentario = (EditText) findViewById(R.id.crear_comentario);
        precio = (EditText) findViewById(R.id.crear_coste);
        fecha = (EditText) findViewById(R.id.crear_fecha);
        hora = (EditText) findViewById(R.id.crear_hora);

        // Inicializar y rellenar un String Array desde un recurso de aplicación
        // String Array - Recurso XML que proporciona una matriz de cadena
        String[] LISTADEPORTES = getResources().getStringArray(R.array.lista_deportes);
        //Establecemos un Array para autocompletar
        deporte = (AutoCompleteTextView) findViewById(R.id.crear_deporte);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, LISTADEPORTES);
        deporte.setAdapter(adapter);
        //Definie el umbral de Autocompletar
        deporte.setThreshold(1);


        crear = (Button) findViewById(R.id.crear_aceptar);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String deporteC = deporte.getText().toString();
                String tipoLugarC = tipoLugar.getText().toString();
                Float precioC = Float.valueOf(precio.getText().toString());
                String fechaC = fecha.getText().toString();
                String horaC = hora.getText().toString();
                String comentarioC = ((EditText) findViewById(R.id.crear_comentario)).getText().toString();

                if (deporte.getText() != null && !deporte.getText().toString().trim().isEmpty()) {
                    if (fechaC.isEmpty() || horaC.isEmpty() || deporteC.isEmpty() || precioC == null) {
                        Snackbar.make(v, "Te olvidas de algun dato importante!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                    Date eventoDate = new Date(evYear - 1900, evMonth, evDay, evHour, evMinute);
                    Date ahoraDate = new Date();
                    ahoraDate.setHours(ahoraDate.getHours() - 3);
                    if (eventoDate.before(ahoraDate)) {
                        Snackbar.make(v, "No todo el mundo puede viajar al pasado!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        System.out.println(ahoraDate);
                        return;
                    }

                    Evento evento = new Evento(deporteC, ubicacionEvento, coordenadas, tipoLugarC, precioC, eventoDate, comentarioC);
                    crearEvento(evento);
                } else {
                    Snackbar.make(v, "Revisa la información introducida", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

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

    private void crearEvento(Evento evento) {
        String key = myRef.child("evento").push().getKey();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        myRef.child("evento").child(key).setValue(evento);
        myRef.child("evento").child(key).child("usuario").child(userID).setValue(true);
        myRef.child("usuario").child(userID).child("evento").child(key).setValue(true);

        myRef.child("evento").child(key).child("creadoPor").setValue(firebaseUser.getDisplayName());

        ProgressDialog.show(CrearEvento.this, "Creando", "Espera un poco ansioso...");
        Toast.makeText(getBaseContext(), "EXITO", Toast.LENGTH_LONG).show();
        finish();


    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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

        evLocalizacion = (String) place.getName();
        ubicacionEvento = evLocalizacion;
        coordenadas = autoCompleteLatLng;


    }

    @Override
    public void onError(Status status) {

        // TODO: Handle the error.
        // Log.i(TAG, "An error occurred: " + status);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            evDay = day;
            evMonth = month;
            evYear = year;
            fecha.setText(SimpleDateFormat.getDateInstance().format(new Date(year - 1900, month, day)));
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            evMinute = minute;
            evHour = hourOfDay;
            hora.setText(SimpleDateFormat.getTimeInstance().format(new Date(1, 1, 1900, hourOfDay, minute)));
        }
    }


}
