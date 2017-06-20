package com.david.socialsport.Pantallas;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.david.socialsport.Adapters.AdapterComentarios;
import com.david.socialsport.Adapters.AdapterUsuarios;
import com.david.socialsport.Objetos.Comentarios;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by david on 20/06/2017.
 */

public class VerComentarios extends Activity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private AdapterComentarios adapter;
    private String eventoID;
    private String userID;
    Comentarios comentariosUsuario;
    String idUsuari, comentarioUsuario;
    TextView emptyText;

    EditText escribirComentario;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterComentarios(getApplication());


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        eventoID = getIntent().getStringExtra("eventoID");
        userID = getIntent().getStringExtra("userID");

        setContentView(R.layout.lista_comentarios);

        emptyText = (TextView) findViewById(R.id.empty);

        final ListView listView = (ListView) findViewById(R.id.listaComentarios);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(android.R.id.empty));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                 DataSnapshot comentarios = dataSnapshot.child("evento").child(eventoID).child("comentarios");
                if (comentarios.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("evento").child(eventoID).child("comentarios")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    GenericTypeIndicator<Map<String, Comentarios>> t = new GenericTypeIndicator<Map<String, Comentarios>>() {
                                    };

                                    Map<String, Comentarios> comentarios = dataSnapshot.getValue(t);

                                    if (comentarios != null) {
                                        for (Comentarios c: comentarios.values()) {
                                            emptyText.setVisibility(View.INVISIBLE);
                                            adapter.add(c);
                                        }
                                    }
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }else{
                }
               /* adapter.sort(new Comparator<Comentarios>() {
                    @Override
                    public int compare(Comentarios o1, Comentarios o2) {
                        return o1.getNombre().compareTo(o2.getNombre());

                    }
                });*/
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        escribirComentario = (EditText) findViewById(R.id.escribir_comentario);
        escribirComentario.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (escribirComentario.getRight() - escribirComentario.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        comentarioUsuario = escribirComentario.getText().toString();

                        idUsuari = userID;

                        escribirComentario.setText("");

                        comentariosUsuario = new Comentarios(idUsuari, comentarioUsuario);

                        crearComentario(comentariosUsuario);
                        return true;
                    }
                }
                return false;
            }
        });

    }


    private void crearComentario(Comentarios comentarios) {
        String key = myRef.child("evento").child(eventoID).child("comentarios").push().getKey();


        myRef.child("evento").child(eventoID).child("comentarios").child(key).setValue(comentarios);
        finish();


    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
