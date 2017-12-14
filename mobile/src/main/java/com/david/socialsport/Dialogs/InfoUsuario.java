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

public class InfoUsuario extends AppCompatActivity {

    ActionBar menuBar;

    String userID, userActivoID, nombreUsuarioActivo;
    Boolean peticionAmistad;
    Date ahoraDate;

    ImageView imagenUsuario;
    TextView nombreUsuario;
    Button botonMensaje, botonInformacion, botonAmigo;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuBar = getSupportActionBar();
        setContentView(R.layout.dialog_info_usuario);

        userID = getIntent().getStringExtra("userID");
        userActivoID=firebaseUser.getUid();


        imagenUsuario = (ImageView) findViewById(R.id.imagenUsuario);
        nombreUsuario = (TextView) findViewById(R.id.nombreUsuario);
        botonMensaje = (Button) findViewById(R.id.botonMensaje);
        botonInformacion = (Button) findViewById(R.id.botonInformacion);
        botonAmigo = (Button) findViewById(R.id.botonAnadir);

        ahoraDate = new Date();
        ahoraDate.setHours(ahoraDate.getHours());

        botonInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PantallaInfoUsuario.class);
                intent.putExtra("usuarioID", userID);
                System.out.println(userID);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });

        botonMensaje.setOnClickListener(new View.OnClickListener() {
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
        botonAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peticionAmistad=true;
                String mensajeEscrito =nombreUsuarioActivo+" "+getString(R.string.solicitud_amistad);
                Comentarios peticion = new Comentarios(userActivoID, userID, mensajeEscrito, ahoraDate, peticionAmistad);
                anadirAmigo(peticion);
                botonAmigo.setFocusable(false);
                botonAmigo.setBackgroundColor(getResources().getColor(R.color.transparenteGris));
                botonAmigo.setText(R.string.peticion_enviada);
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


    public void anadirAmigo(Comentarios peticion){
        String key = myRef.child("mensaje").push().getKey();
        myRef.child("mensaje").child(key).setValue(peticion);

        String keyUsuarioPedir = myRef.child("usuario").child(userID).getKey();
        myRef.child("usuario").child(keyUsuarioPedir).child("peticion").child(userActivoID).setValue(true);

        String keyUsuarioPide = myRef.child("usuario").child(userActivoID).getKey();
        myRef.child("usuario").child(keyUsuarioPide).child("solicitud").child(userActivoID).setValue(true);

        myRef.child("usuario").child(userID).child("mensaje").child("recibido").child(key).setValue(true);
        myRef.child("usuario").child(userActivoID).child("mensaje").child("enviado").child(key).setValue(true);
    }
}
