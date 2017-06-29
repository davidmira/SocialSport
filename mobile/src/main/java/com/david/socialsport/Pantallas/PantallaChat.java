package com.david.socialsport.Pantallas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.david.socialsport.Adapters.AdapterChat;
import com.david.socialsport.Objetos.Chat;
import com.david.socialsport.Objetos.Comentarios;
import com.david.socialsport.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by david on 29/06/2017.
 */

public class PantallaChat extends AppCompatActivity{

    private FirebaseListAdapter<Chat> adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ListView listView;

    EditText escribirComentario;
    String idUsuario, comentarioUsuario;
    String userID;

    Chat chatUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lista_chat);

        //adapter = new AdapterChat(getApplication());

        userID = getIntent().getStringExtra("userID");

        listView = (ListView) findViewById(R.id.listaChat);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(android.R.id.empty));


        escribirComentario = (EditText) findViewById(R.id.escribir_comentario);

        //Creamos el comentario al presionar botón enviar
        escribirComentario.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2; //seleccionamos el boton del EditText a la derecha
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (escribirComentario.getRight() - escribirComentario.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        comentarioUsuario = escribirComentario.getText().toString();

                     /*   idUsuario = userID;

                        escribirComentario.setText("");

                        Date ahoraDate = new Date();
                        ahoraDate.setHours(ahoraDate.getHours());

                        chatUsuario = new Chat(idUsuario, comentarioUsuario, ahoraDate);

                        crearChat(chatUsuario);
*/

                        Date ahoraDate = new Date();
                        ahoraDate.setHours(ahoraDate.getHours());

                        FirebaseDatabase.getInstance()
                                .getReference()
                                .push()
                                .setValue(new Chat(
                                        FirebaseAuth.getInstance()
                                                .getCurrentUser()
                                                .getDisplayName(), comentarioUsuario, ahoraDate)
                                );



                        return true;
                    }
                }
                return false;
            }
        });

        adapter = new FirebaseListAdapter<Chat>(this, Chat.class,
                R.layout.ficha_chat, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Chat model, int position) {
                // Get references to the views of message.xml

                TextView nombreUsuarioMensaje = (TextView) v.findViewById(R.id.chat_nombre_usuario);
                TextView horaMensaje = (TextView) v.findViewById(R.id.chat_hora);
                TextView textoMensaje = (TextView) v.findViewById(R.id.chat_texto_usuario);

                // Set their text
                textoMensaje.setText(model.getMensajes());
                nombreUsuarioMensaje.setText(model.getMiembros());

                // Format the date before showing it
              //  horaMensaje.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                //        model.getHora()));
            }
        };

        listView.setAdapter(adapter);
    }

    private void crearChat(Chat chat) {
        String key = myRef.child("mensaje").push().getKey();
        myRef.child("mensaje").child(key).setValue(chat);
    }

}
