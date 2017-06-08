package com.david.socialsport.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by david on 03/04/2017.
 */

public class AdapterEventos extends ArrayAdapter<Evento> implements OnMapReadyCallback  {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private Bundle savedInstanceState;
    private int currentPosition = -1;


    public AdapterEventos(@NonNull Context context, Bundle savedInstanceState) {
        super(context, 0, new ArrayList<Evento>());
        this.savedInstanceState = savedInstanceState;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_eventos, parent, false);
        }

        Evento evento = getItem(position);

        TextView deporte = (TextView) convertView.findViewById(R.id.evento_deporte);
        TextView localizacion = (TextView) convertView.findViewById(R.id.evento_localizacion);
        TextView ubicacionEvento = (TextView) convertView.findViewById(R.id.evento_ubicacion);
        TextView tipoLugar = (TextView) convertView.findViewById(R.id.evento_tipo_lugar);
        TextView precio = (TextView) convertView.findViewById(R.id.evento_precio);
        TextView fecha = (TextView) convertView.findViewById(R.id.evento_fecha);
        TextView hora = (TextView) convertView.findViewById(R.id.evento_hora);

        ImageView icono = (ImageView) convertView.findViewById(R.id.evento_icono);


        deporte.setText(evento.getDeporte());
        ubicacionEvento.setText(evento.getUbicacionEvento());
        tipoLugar.setText(evento.getTipoLugar());
        precio.setText(evento.getPrecio().toString() + " €");


        Date fechaHora = evento.getFecha_hora();
        fecha.setText(new SimpleDateFormat("dd/MM/yy").format(fechaHora));
        hora.setText(new SimpleDateFormat("HH:mm").format(fechaHora));


        MapView mapView = (MapView) convertView.findViewById(R.id.evento_mapa);
        mapView.onCreate(savedInstanceState);

        return convertView;

    }

    public boolean expandItem(int position, View view) {
        Evento e = getItem(position);
        currentPosition = position;

        if (!(e.getComentario() == null || e.getComentario().isEmpty())) {
            TextView comentario = ((TextView) view.findViewById(R.id.evento_comentario));
            comentario.setText(e.getComentario());
            comentario.setVisibility(View.VISIBLE);
        }

        if(!(e.getLatitude() == 0 && e.getLongitude() == 0)){
            MapView map = ((MapView)view.findViewById(R.id.evento_mapa));
            map.onResume();
            map.getMapAsync(this);

            map.setVisibility(View.VISIBLE);
        }

        LinearLayout expandible = (LinearLayout) view.findViewById(R.id.expandible);
        expandible.setVisibility(View.VISIBLE);

        return true;

    }

    public void collapseItem(View view) {
        view.findViewById(R.id.evento_comentario).setVisibility(View.GONE);
        view.findViewById(R.id.expandible).setVisibility(View.GONE);
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
