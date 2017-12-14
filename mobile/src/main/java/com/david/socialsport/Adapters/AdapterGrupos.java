package com.david.socialsport.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.david.socialsport.Dialogs.InfoUsuarioAmigo;
import com.david.socialsport.Objetos.Grupo;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by david on 10/07/2017.
 */

public class AdapterGrupos extends ArrayAdapter<Grupo> {
    public AdapterGrupos(@NonNull Context context, ArrayList<Grupo> grupo) {
        super(context, 0, grupo);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_grupos, parent, false);
        }

        Grupo grupo = getItem(position);

        TextView nombre = (TextView) convertView.findViewById(R.id.nombre_grupo);
        nombre.setText(grupo.getNombre());

        ImageView imagenGrupo = (ImageView) convertView.findViewById(R.id.imagen_grupo);
        if (grupo.getImagen() != null && !grupo.getImagen().isEmpty()) {
            Picasso.with(getContext()).load(grupo.getImagen()).into(imagenGrupo);
        } else {
            imagenGrupo.setImageResource(R.drawable.ic_group);
        }

        return convertView;
    }

}
