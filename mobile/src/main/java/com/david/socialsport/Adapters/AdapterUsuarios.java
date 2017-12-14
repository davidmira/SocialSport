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
import com.david.socialsport.Dialogs.VerUsuarios;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by david on 19/06/2017.
 */

public class AdapterUsuarios extends ArrayAdapter<Usuario> {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public AdapterUsuarios(@NonNull Context context) {
        super(context, 0, new ArrayList<Usuario>());
    }

    @Override
    public View getView(@NonNull final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_usuarios, parent, false);
        }

        Usuario usuario = getItem(position);

        System.out.println("nombre: "+ usuario.getIdAmigo());
        TextView nombre = (TextView) convertView.findViewById(R.id.textViewNombreUsuario);
        nombre.setText(usuario.getNombre());
        ImageView imagen = (ImageView) convertView.findViewById(R.id.imagenUsuario);
        Picasso.with(getContext()).load(usuario.getImagen()).into(imagen);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoUsuarios(position);
            }
        });

        //Se pasa administrador al usuario que ha creado el evento
        TextView admin = (TextView) convertView.findViewById(R.id.textViewAdmin);
        admin.setText(usuario.getAdmin());


        return convertView;
    }

    private void infoUsuarios(int position) {
        Intent intent = new Intent(getContext(), InfoUsuario.class);
        intent.putExtra("userID", getItem(position).getId());
        getContext().startActivity(intent);
    }
}

