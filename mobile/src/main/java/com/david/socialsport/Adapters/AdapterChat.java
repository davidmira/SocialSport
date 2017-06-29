package com.david.socialsport.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.socialsport.Objetos.Chat;
import com.david.socialsport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by david on 29/06/2017.
 */

public class AdapterChat extends ArrayAdapter<Chat> {
    private Chat chat;
    private TextView nombreUsuarioMensaje, horaMensaje, textoMensaje;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    private String nombreUsuario, mensaje;

    public AdapterChat(@NonNull Context context) {
        super(context, 0, new ArrayList<Chat>());
    }

    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_chat, parent, false);
        }

        chat = getItem(position);
        final DatabaseReference mensajes = myRef.child("mensaje").child(chat.getIdChat());

        nombreUsuarioMensaje = (TextView) convertView.findViewById(R.id.chat_nombre_usuario);
        horaMensaje = (TextView) convertView.findViewById(R.id.chat_hora);
        textoMensaje = (TextView) convertView.findViewById(R.id.chat_texto_usuario);



        mensajes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombreUsuario=dataSnapshot.child("miembros").getValue(String.class);
                mensaje =(dataSnapshot.child("mensaje").getValue(String.class));
                if(chat!=null) {

                    //nombreUsuarioMensaje.setText(chat.getMensajes());
                    //textoMensaje.setText(chat.getMensajes());

                    nombreUsuarioMensaje.setText(nombreUsuario);
                    textoMensaje.setText(mensaje);

                    Date fechaHora = chat.getHora();
                    horaMensaje.setText(new SimpleDateFormat("HH:mm").format(fechaHora));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return convertView;
    }
}
