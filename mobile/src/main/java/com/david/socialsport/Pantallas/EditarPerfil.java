package com.david.socialsport.Pantallas;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by david on 27/06/2017.
 */

public class EditarPerfil extends AppCompatActivity {

    String userID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ActionBar menuBar;

    String imgURL;

    CircleImageView imagenUsuario;
    EditText nombreUsuario, correoUsuario, apellidosUsuario;


    Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuBar = getSupportActionBar();
        menuBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_editar_perfil);

        userID = getIntent().getStringExtra("usuarioID");
        myRef = database.getReference().child("usuario").child(userID);

        imagenUsuario = (CircleImageView) findViewById(R.id.imagenUsuario);
        imagenUsuario.setEnabled(false);

        nombreUsuario = (EditText) findViewById(R.id.editTextNombre);
        nombreUsuario.setEnabled(false);

        apellidosUsuario = (EditText) findViewById(R.id.editTextApellidos);
        apellidosUsuario.setEnabled(false);

        correoUsuario = (EditText) findViewById(R.id.editTextCorreo);
        correoUsuario.setEnabled(false);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                String nombreCompleto = firebaseUser.getDisplayName();
                String espacio = " ";
                String nombre = nombreCompleto.substring(0, nombreCompleto.indexOf(espacio));
                String apellidos = nombreCompleto.substring(nombreCompleto.indexOf(espacio) + 1, nombreCompleto.length());

               // Usuario person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String imgDir = String.valueOf(firebaseUser.getPhotoUrl());
                String email = firebaseUser.getEmail();
                mostrarDatos(nombre, apellidos, imgDir, email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void mostrarDatos(String nombre, String apellidos, String img, String email) {


        imgURL = img;
        nombreUsuario.setText(nombre);
        apellidosUsuario.setText(apellidos);
        correoUsuario.setText(email);
        if (img != null && !img.isEmpty()) {
            Glide.with(this).load(img).into(imagenUsuario);
        } else {
            imagenUsuario.setImageResource(R.drawable.user_rojo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
