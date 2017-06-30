package com.david.socialsport.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.david.socialsport.Objetos.Comentarios;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by david on 29/06/2017.
 */

public class AdapterMensajesPersonalesRecibidos extends ArrayAdapter<Comentarios> {

    private TextView nombreUsuarioMensaje, horaMensaje, textoMensaje;
    private CircleImageView imagenUsuarioMensaje;
    private Button botonAceptar, botonDeclinar;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private Bundle savedInstanceState;

    private String nombreUsuario, imagenUsuario;

    public AdapterMensajesPersonalesRecibidos(@NonNull Context context, Bundle savedInstanceState) {
        super(context, 0, new ArrayList<Comentarios>());
        this.savedInstanceState = savedInstanceState;
    }


    public
    @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ficha_mensaje_personal, parent, false);
        }


        final Comentarios mensaje = getItem(position);

        final DatabaseReference usuario = myRef.child("usuario").child(mensaje.getIdUsuarioRemitente());
        final View finalConvertView = convertView;
        usuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombreUsuario=dataSnapshot.child("nombre").getValue(String.class);
                imagenUsuario =(dataSnapshot.child("imagen").getValue(String.class));

                nombreUsuarioMensaje = (TextView) finalConvertView.findViewById(R.id.chat_nombre_usuario);
                nombreUsuarioMensaje.setText(nombreUsuario);

                horaMensaje = (TextView) finalConvertView.findViewById(R.id.chat_fecha_hora);
                Date fechaHora = mensaje.getFecha_hora();
                horaMensaje.setText(new SimpleDateFormat("dd MMM").format(fechaHora) + " " + getContext().getString(R.string.a_las) + " " + new SimpleDateFormat("HH:mm").format(fechaHora));

                textoMensaje = (TextView) finalConvertView.findViewById(R.id.chat_texto_usuario);
                textoMensaje.setText(mensaje.getComentario());

                imagenUsuarioMensaje = (CircleImageView) finalConvertView.findViewById(R.id.chat_imagen_usuario);
                Picasso.with(getContext()).load(imagenUsuario).into(imagenUsuarioMensaje);

                botonAceptar = (Button) finalConvertView.findViewById(R.id.boton_aceptar);
                botonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // String key = myRef.child("mensaje").getKey();
                        //myRef.child("mensaje").child(key).child("comentario").setValue(nombreUsuarioMensaje+" "+getContext().getString(R.string.ahora_amigos));
                        System.out.println((nombreUsuarioMensaje+" "+getContext().getString(R.string.ahora_amigos)));

                        //String keyUsuarioPedir = myRef.child("usuario").child(userID).getKey();
                       // myRef.child("usuario").child(keyUsuarioPedir).child("peticion").child(userActivoID).setValue(true);

                        //String keyUsuarioPide = myRef.child("usuario").child(userActivoID).getKey();
                        //myRef.child("usuario").child(keyUsuarioPide).child("solicitud").child(userActivoID).setValue(true);

                    }
                });
                botonDeclinar = (Button) finalConvertView.findViewById(R.id.boton_declinar);
                if(!mensaje.getPeticion()) {
                    botonAceptar.setVisibility(View.INVISIBLE);
                    botonDeclinar.setVisibility(View.INVISIBLE);
                }else{
                    botonAceptar.setVisibility(View.VISIBLE);
                    botonDeclinar.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        return convertView;
    }
}
