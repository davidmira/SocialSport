package com.david.socialsport.Dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.david.socialsport.Adapters.AdapterEventos;
import com.david.socialsport.Adapters.AdapterUsuarios;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by david on 20/06/2017.
 */

public class VerUsuarios extends Activity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private AdapterUsuarios adapter;
    private String eventoID;
    private String userID;
    ListView listView;
    TextView admin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lista_usuarios);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        adapter = new AdapterUsuarios(getApplication());

        eventoID = getIntent().getStringExtra("eventoID");
        userID = getIntent().getStringExtra("userID");

        listView = (ListView) findViewById(R.id.listaUsuarios);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(android.R.id.empty));

        admin = (TextView) findViewById(R.id.textViewAdmin);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                GenericTypeIndicator<Map<String, Boolean>> t = new GenericTypeIndicator<Map<String, Boolean>>() {
                };
                Map<String, Boolean> usuariosID = dataSnapshot.child("evento").child(eventoID).child("usuarios").getValue(t);
                if (usuariosID != null) {
                    for (Map.Entry<String, Boolean> entry : usuariosID.entrySet()) {
                        DataSnapshot usuarioEvento = dataSnapshot.child("usuario").child(entry.getKey()).child("evento").child(eventoID);
                        if (usuarioEvento.exists()) {
                            if (usuarioEvento.getValue(Boolean.class)) {
                                Usuario usuario = dataSnapshot.child("usuario").child(entry.getKey()).getValue(Usuario.class);
                                usuario.setId(entry.getKey());
                                //Para el usuario que ha creado el evento le pasamos el parámetro de administrador
                                if(dataSnapshot.child("usuario").child(entry.getKey()).child("id").getValue()
                                        .equals(dataSnapshot.child("evento").child(eventoID).child("creadoPor").getValue())){
                                    System.out.println(usuario.getNombre());
                                    usuario.setAdmin(getString(R.string.administrador));
                                }
                                adapter.add(usuario);

                            }
                        } else {
                            dataSnapshot.child("evento").child(eventoID).child("usuario").child(entry.getKey()).getRef().removeValue();
                        }
                    }
                }
                adapter.sort(new Comparator<Usuario>() {
                    @Override
                    public int compare(Usuario o1, Usuario o2) {
                        return o1.getNombre().compareTo(o2.getNombre());

                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();
    }
}
