package com.david.socialsport.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.socialsport.Dialogs.InfoUsuario;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by david on 13/12/17.
 */

public class AdapterBuscarAmigos extends ArrayAdapter<Usuario> {

    public AdapterBuscarAmigos(@NonNull Context context, ArrayList<Usuario> usuarios) {
        super(context, 0, usuarios);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_amigos, parent, false);
        }

       Usuario usuario = getItem(position);

        TextView nombreAmigo = (TextView) convertView.findViewById(R.id.amigos_nombre_usuario);
        nombreAmigo.setText(usuario.getNombre());

        ImageView imagenAmigo = (ImageView) convertView.findViewById(R.id.amigos_imagen_usuario);
        if (usuario.getImagen() != null && !usuario.getImagen().isEmpty()) {
            Picasso.with(getContext()).load(usuario.getImagen()).into(imagenAmigo);
        } else {
            imagenAmigo.setImageResource(R.drawable.ic_person_outline);
        }
        imagenAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoUsuarios(position);
            }
        });

        return convertView;
    }

    private void infoUsuarios(int position) {
        Intent intent = new Intent(getContext(), InfoUsuario.class);
        intent.putExtra("userID", getItem(position).getId());
        getContext().startActivity(intent);
    }
}
