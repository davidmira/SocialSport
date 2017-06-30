package com.david.socialsport.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.david.socialsport.Objetos.Comentarios;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by david on 29/06/2017.
 */

public class DialogMensaje extends Activity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    String userID, userActivoID, mensajeEscrito;
    Date ahoraDate;

    TextView textoMensaje;
    Button enviarMensaje;
    Boolean peticion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_comentario_personal);

        userID = getIntent().getStringExtra("userID");


        userActivoID = firebaseUser.getUid();


        textoMensaje = (TextView) findViewById(R.id.escribir_mensaje);
        enviarMensaje = (Button) findViewById(R.id.boton_enviar_mensaje);


        ahoraDate = new Date();
        ahoraDate.setHours(ahoraDate.getHours());


        enviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensajeEscrito = textoMensaje.getText().toString();
                peticion = false;
                textoMensaje.setText("");
                Comentarios mensaje = new Comentarios(userActivoID, userID, mensajeEscrito, ahoraDate, peticion);
                enviarMensaje(mensaje);
                finish();
            }
        });

    }

    public void enviarMensaje(Comentarios mensaje) {
        String key = myRef.child("mensaje").push().getKey();
        myRef.child("mensaje").child(key).setValue(mensaje);
        myRef.child("usuario").child(userID).child("mensaje").child("recibido").child(key).setValue(true);
        myRef.child("usuario").child(userActivoID).child("mensaje").child("enviado").child(key).setValue(true);
    }
}
