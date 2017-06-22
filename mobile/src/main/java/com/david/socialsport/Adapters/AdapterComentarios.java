package com.david.socialsport.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.socialsport.Fragments.FragmentEventos;
import com.david.socialsport.Objetos.Comentarios;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;


/**
 * Created by david on 20/06/2017.
 */

public class AdapterComentarios extends ArrayAdapter<Comentarios> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private String nombre, imagen;
    public AdapterComentarios(@NonNull Context context) {
        super(context, 0, new ArrayList<Comentarios>());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_comentarios, parent, false);
        }

        final Comentarios comentarios = getItem(position);

        final DatabaseReference usuario = myRef.child("usuario").child(comentarios.getIdUsuario());

        final View finalConvertView = convertView;
        usuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombre=dataSnapshot.child("nombre").getValue(String.class);
                imagen =(dataSnapshot.child("imagen").getValue(String.class));
                if(comentarios!=null) {
                    TextView nombreUsuario = (TextView) finalConvertView.findViewById(R.id.textViewNombreUsuario);
                    nombreUsuario.setText(nombre);

                    ImageView imagenUsuaio = (ImageView) finalConvertView.findViewById(R.id.imagenUsuario);
                    Picasso.with(getContext()).load(imagen).into(imagenUsuaio);

                    TextView comentario = (TextView) finalConvertView.findViewById(R.id.textViewComentario);
                    comentario.setText(comentarios.getComentario());


                    TextView fecha_hora = (TextView) finalConvertView.findViewById(R.id.textViewFechaHora);

                    Date fechaHora = comentarios.getFecha_hora();
                    fecha_hora.setText(new SimpleDateFormat("dd MMM").format(fechaHora)+" "+getContext().getString(R.string.a_las)+" "+ new SimpleDateFormat("HH:mm").format(fechaHora));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });





        return convertView;
    }
}
