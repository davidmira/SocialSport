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
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by david on 19/06/2017.
 */

public class AdapterUsuarios extends ArrayAdapter<Usuario> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public AdapterUsuarios(@NonNull Context context) {
        super(context, 0, new ArrayList<Usuario>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_usuarios, parent, false);
        }

        Usuario usuario = getItem(position);

        TextView nombre = (TextView) convertView.findViewById(R.id.textViewNombreUsuario);
        nombre.setText(usuario.getNombre());
        ImageView imagen = (ImageView) convertView.findViewById(R.id.imagenUsuario);
        Picasso.with(getContext()).load(usuario.getImagen()).into(imagen);

        //Se pasa administrador al usuario que ha creado el evento
        TextView admin = (TextView) convertView.findViewById(R.id.textViewAdmin);
        admin.setText(usuario.getAdmin());


        return convertView;
    }
}

