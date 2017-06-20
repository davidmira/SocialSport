package com.david.socialsport.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by david on 03/04/2017.
 */

public class AdapterEventos extends ArrayAdapter<Evento> implements OnMapReadyCallback {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private Bundle savedInstanceState;
    private int currentPosition = -1;
    ImageView icono;
    Evento evento;
    TextView deporte, localizacion, ubicacionEvento, precio, fecha, hora;
    String userID;

    public AdapterEventos(@NonNull Context context, Bundle savedInstanceState) {
        super(context, 0, new ArrayList<Evento>());
        this.savedInstanceState = savedInstanceState;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_eventos, parent, false);
        }

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        evento = getItem(position);

        deporte = (TextView) convertView.findViewById(R.id.evento_deporte);
        localizacion = (TextView) convertView.findViewById(R.id.evento_localizacion);
        ubicacionEvento = (TextView) convertView.findViewById(R.id.evento_ubicacion);
        precio = (TextView) convertView.findViewById(R.id.evento_precio);
        fecha = (TextView) convertView.findViewById(R.id.evento_fecha);
        hora = (TextView) convertView.findViewById(R.id.evento_hora);

        //Cargamos la imagen del evento según el deporte que le corresponda
        icono = (ImageView) convertView.findViewById(R.id.evento_icono);
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://socialsport-e98f4.appspot.com").child("iconos").child(evento.getDeporte().toLowerCase() + ".png");
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                icono.setImageBitmap(redimensionarImagenMaximo(bitmap, icono.getWidth(), icono.getHeight()));
            }
        });


        deporte.setText(evento.getDeporte());
        ubicacionEvento.setText(evento.getUbicacionEvento());
        precio.setText(evento.getPrecio().toString() + " €");


        Date fechaHora = evento.getFecha_hora();
        fecha.setText(new SimpleDateFormat("dd/MM/yy").format(fechaHora));
        hora.setText(new SimpleDateFormat("HH:mm").format(fechaHora));


        MapView mapView = (MapView) convertView.findViewById(R.id.evento_mapa);
        mapView.onCreate(savedInstanceState);

        return convertView;

    }

    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth) {
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    public boolean expandItem(int position, View view) {
        Evento e = getItem(position);
        currentPosition = position;

        if (!(e.getComentario() == null || e.getComentario().isEmpty())) {
            TextView comentario = ((TextView) view.findViewById(R.id.evento_informacion));
            comentario.setText(e.getComentario());
            comentario.setVisibility(View.VISIBLE);
        }

        if (!(e.getLatitude() == 0 && e.getLongitude() == 0)) {
            MapView map = ((MapView) view.findViewById(R.id.evento_mapa));
            map.onResume();
            map.getMapAsync(this);

            map.setVisibility(View.VISIBLE);
        }


        view.findViewById(R.id.evento_opciones).setVisibility(View.VISIBLE);
        LinearLayout expandible = (LinearLayout) view.findViewById(R.id.expandible);
        expandible.setVisibility(View.VISIBLE);

        return true;

    }

    public void collapseItem(View view) {
        view.findViewById(R.id.evento_informacion).setVisibility(View.GONE);
        view.findViewById(R.id.expandible).setVisibility(View.GONE);
        view.findViewById(R.id.evento_opciones).setVisibility(View.GONE);
        currentPosition = -1;
    }

    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void collapseCurrent(ListView listView) {
        if (currentPosition < 0) return;
        View view = getViewByPosition(currentPosition, listView);
        collapseItem(view);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Evento e = getItem(currentPosition);
        LatLng coords = e.getCoordenadas();
        googleMap.addMarker(new MarkerOptions().position(coords).title(e.getUbicacionEvento())).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 15));
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}
