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
import com.david.socialsport.Objetos.Comentarios;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.Pantallas.PantallaInfoUsuario;
import com.david.socialsport.Pantallas.PantallaInfoUsuarioAmigo;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


/**
 * Created by david on 28/06/2017.
 */

public class InfoUsuarioAmigo extends AppCompatActivity {

    ActionBar menuBar;

    String userID, userActivoID, nombreUsuarioActivo;
    Date ahoraDate;

    ImageView imagenUsuarioAmigo;
    TextView nombreUsuarioAmigo;
    Button botonMensajeAmigo, botonInformacionAmigo, botonEliminarAmigo;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuBar = getSupportActionBar();
        setContentView(R.layout.dialog_info_usuario_amigo);

        userID = getIntent().getStringExtra("userID");
        userActivoID=firebaseUser.getUid();


        imagenUsuarioAmigo = (ImageView) findViewById(R.id.imagenUsuarioAmigo);
        nombreUsuarioAmigo = (TextView) findViewById(R.id.nombreUsuarioAmigo);
        botonMensajeAmigo = (Button) findViewById(R.id.botonMensajeAmigo);
        botonInformacionAmigo = (Button) findViewById(R.id.botonInformacionAmigo);
        botonEliminarAmigo = (Button) findViewById(R.id.botonEliminarAmigo);

        ahoraDate = new Date();
        ahoraDate.setHours(ahoraDate.getHours());

        botonMensajeAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DialogMensaje.class);
                intent.putExtra("userID", userID);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
        final DatabaseReference usuario = myRef.child("usuario").child(userActivoID);
        usuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombreUsuarioActivo=dataSnapshot.child("nombre").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        botonEliminarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarAmigo();
                botonEliminarAmigo.setFocusable(false);
                botonEliminarAmigo.setBackgroundColor(getResources().getColor(R.color.transparenteGris));
                botonEliminarAmigo.setText(R.string.amigo_eliminado);
                finish();
            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Cargamos el usuario en el que hemos pinchado
                Usuario usuario = dataSnapshot.child("usuario").child(userID).getValue(Usuario.class);

                //Cargamos nombre de usuario
                nombreUsuarioAmigo.setText(usuario.getNombre());
                //Ponemos la imagen de usuario
                Glide.with(getApplicationContext()).load(usuario.getImagen()).into(imagenUsuarioAmigo);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        botonInformacionAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PantallaInfoUsuarioAmigo.class);
                intent.putExtra("usuarioID", userID);
                getApplicationContext().startActivity(intent);
                finish();

            }
        });
    }

    public void eliminarAmigo(){
        myRef.child("usuario").child(userActivoID).child("amigos").child(userID).removeValue();
        myRef.child("usuario").child(userID).child("amigos").child(userActivoID).removeValue();
    }
}
