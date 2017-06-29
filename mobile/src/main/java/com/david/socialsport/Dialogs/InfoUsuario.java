package com.david.socialsport.Dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.Pantallas.PantallaChat;
import com.david.socialsport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by david on 28/06/2017.
 */

public class InfoUsuario extends AppCompatActivity {

    ActionBar menuBar;

    String userID;
    ImageView imagenUsuario;
    TextView nombreUsuario;
    Button botonMensaje, botonInformacion;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuBar = getSupportActionBar();
        setContentView(R.layout.dialog_info_usuario);

        userID = getIntent().getStringExtra("userID");

        imagenUsuario = (ImageView) findViewById(R.id.imagenUsuario);
        nombreUsuario = (TextView) findViewById(R.id.nombreUsuario);
        botonMensaje = (Button) findViewById(R.id.botonMensaje);
        botonInformacion = (Button) findViewById(R.id.botonInformacion);

        botonMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PantallaChat.class);
                intent.putExtra("userID", userID);
                getApplicationContext().startActivity(intent);
            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Cargamos el usuario en el que hemos pinchado
                Usuario usuario = dataSnapshot.child("usuario").child(userID).getValue(Usuario.class);

                //Cargamos nombre de usuario
                nombreUsuario.setText(usuario.getNombre());
                //Ponemos la imagen de usuario
                Glide.with(getApplicationContext()).load(usuario.getImagen()).into(imagenUsuario);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
