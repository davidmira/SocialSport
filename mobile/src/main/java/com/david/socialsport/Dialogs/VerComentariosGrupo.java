package com.david.socialsport.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.david.socialsport.Adapters.AdapterComentarios;
import com.david.socialsport.Objetos.Comentarios;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 20/06/2017.
 */

public class VerComentariosGrupo extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    SwipeRefreshLayout swipeRefreshLayout;
    private AdapterComentarios adapter;
    private String grupoID;
    private String userID;
    Comentarios comentariosUsuario;
    String idUsuari, comentarioUsuario;
    TextView emptyText;

    EditText escribirComentario;

    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lista_comentarios);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        adapter = new AdapterComentarios(getApplication());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        grupoID = getIntent().getStringExtra("grupoID");
        userID = getIntent().getStringExtra("userID");




        emptyText = (TextView) findViewById(R.id.empty);

        listView = (ListView) findViewById(R.id.listaComentarios);
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

                        idUsuari = userID;

                        escribirComentario.setText("");

                        Date ahoraDate = new Date();
                        ahoraDate.setHours(ahoraDate.getHours());

                        comentariosUsuario = new Comentarios(idUsuari, comentarioUsuario, ahoraDate);

                        crearComentario(comentariosUsuario);
                        onRefresh();
                        return true;
                    }
                }
                return false;
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refrescar_comentarios);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout == null) return;
        swipeRefreshLayout.setRefreshing(true);
        emptyText.setVisibility(View.INVISIBLE);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                DataSnapshot comentarios = dataSnapshot.child("grupo").child(grupoID).child("comentarios");
                if (comentarios.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("grupo").child(grupoID).child("comentarios")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    GenericTypeIndicator<Map<String, Comentarios>> t = new GenericTypeIndicator<Map<String, Comentarios>>() {
                                    };

                                    Map<String, Comentarios> comentarios = dataSnapshot.getValue(t);

                                    if (comentarios != null) {
                                        for (Comentarios c : comentarios.values()) {
                                            emptyText.setVisibility(View.INVISIBLE);
                                            adapter.add(c);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                    adapter.sort(new Comparator<Comentarios>() {
                        @Override
                        public int compare(Comentarios o1, Comentarios o2) {
                            return o2.getFecha_hora().compareTo(o1.getFecha_hora());

                        }
                    });
                    adapter.notifyDataSetChanged();
                } else {
                    emptyText.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void crearComentario(Comentarios comentarios) {
        String key = myRef.child("grupo").child(grupoID).child("comentarios").push().getKey();
        myRef.child("grupo").child(grupoID).child("comentarios").child(key).setValue(comentarios);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
