package com.david.socialsport.Adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;

import java.util.ArrayList;

/**
 * Created by david on 03/04/2017.
 */

public class AdapterEventos extends ArrayAdapter<Evento> {
    TextView deporte, fecha, hora, ubicacion, donde, min, max, login;
    Usuario usuario;

    public AdapterEventos(Context context, ArrayList<Evento> evento, Usuario usuario) {
        super(context, 0, evento);
        this.usuario = usuario;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Evento evento= getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_eventos, parent, false);
        }

        return convertView;

    }
}
