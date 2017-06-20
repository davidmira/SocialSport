package com.david.socialsport.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.socialsport.Objetos.Comentarios;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by david on 20/06/2017.
 */

public class AdapterComentarios extends ArrayAdapter<Comentarios> {


    public AdapterComentarios(@NonNull Context context) {
        super(context, 0, new ArrayList<Comentarios>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_usuarios, parent, false);
        }

        Comentarios comentarios = getItem(position);

        if(comentarios!=null) {
            TextView nombre = (TextView) convertView.findViewById(R.id.textViewNombreUsuario);
            nombre.setText(comentarios.getNombre());
            ImageView imagen = (ImageView) convertView.findViewById(R.id.imagenUsuario);
            Picasso.with(getContext()).load(comentarios.getImagen()).into(imagen);
            TextView comentario = (TextView) convertView.findViewById(R.id.textViewComentario);
            comentario.setText(comentarios.getComentario());
        }


        return convertView;
    }
}
