package com.david.socialsport.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by david on 13/12/17.
 */

public class AdapterAmigosGrupo extends ArrayAdapter<Usuario>{

    private TextView nombreUsuarioAmigo;
    private CircleImageView imagenUsuarioAmigo;
    private CheckBox check;
    ArrayList<Grupo> mylist=new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private Bundle savedInstanceState;

    private String nombreAmigo, imagenAmigo;

    public AdapterAmigosGrupo(@NonNull Context context, Bundle savedInstanceState) {
        super(context, 0, new ArrayList<Usuario>());
        this.savedInstanceState = savedInstanceState;
    }

    public
    @NonNull
    View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_amigos_grupo, parent, false);

        }
        final Usuario usuarioAmigo = getItem(position);

        final DatabaseReference usuario = myRef.child("usuario").child(usuarioAmigo.getIdAmigo());
        final View finalConvertView = convertView;


        usuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                nombreAmigo = dataSnapshot.child("nombre").getValue(String.class);
                imagenAmigo = dataSnapshot.child("imagen").getValue(String.class);

                nombreUsuarioAmigo = (TextView) finalConvertView.findViewById(R.id.amigos_nombre_usuario);
                nombreUsuarioAmigo.setText(nombreAmigo);

                check = (CheckBox) finalConvertView.findViewById(R.id.usuario_grupo_check);

                check.setTag(position);
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            String usuario2 = dataSnapshot.child("id").getValue(String.class);

                        }
                    }

                });




                imagenUsuarioAmigo = (CircleImageView) finalConvertView.findViewById(R.id.amigos_imagen_usuario);
                Picasso.with(getContext()).load(imagenAmigo).into(imagenUsuarioAmigo);

                imagenUsuarioAmigo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        infoUsuarios(position);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        return convertView;
    }

    private void infoUsuarios(int position) {
        Intent intent = new Intent(getContext(), InfoUsuarioAmigo.class);
        intent.putExtra("userID", getItem(position).getIdAmigo());
        getContext().startActivity(intent);
    }
}