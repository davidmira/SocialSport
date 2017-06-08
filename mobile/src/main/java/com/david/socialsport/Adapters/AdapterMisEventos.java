package com.david.socialsport.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by david on 16/05/2017.
 */

public class AdapterMisEventos extends ArrayAdapter<Evento> {

    public AdapterMisEventos(@NonNull Context context) {
        super(context, 0, new ArrayList<Evento>());
    }

    public View getView(int position, View convertView, ViewGroup parent) {

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

/*
        deporte.setText(evento.getDeporte());
        localizacion.setText(evento.getLocalizacion());
        ubicacionEvento.setText(evento.getUbicacionEvento());
        tipoLugar.setText(evento.getTipoLugar());
        precio.setText(evento.getPrecio().toString() + " €");
*/
       /* Date fechaHora = evento.getFecha_hora();
        fecha.setText(new SimpleDateFormat("dd/MM/yy").format(fechaHora));
        hora.setText(new SimpleDateFormat("HH:mm").format(fechaHora));
*/
        return convertView;

    }

}
