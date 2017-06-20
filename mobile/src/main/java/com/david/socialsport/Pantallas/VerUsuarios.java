package com.david.socialsport.Pantallas;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.david.socialsport.Adapters.AdapterUsuarios;
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
    private String equipoID;
    private String userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterUsuarios(getApplication());

        equipoID = getIntent().getStringExtra("eventoID");
        userID = getIntent().getStringExtra("userID");
        setContentView(R.layout.lista_usuarios);

        final ListView listView = (ListView) findViewById(R.id.listaUsuarios);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(android.R.id.empty));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                GenericTypeIndicator<Map<String, Boolean>> t = new GenericTypeIndicator<Map<String, Boolean>>() {
                };
                Map<String, Boolean> usuariosID = dataSnapshot.child("evento").child(equipoID).child("usuarios").getValue(t);
                if (usuariosID != null) {
                    for (Map.Entry<String, Boolean> entry : usuariosID.entrySet()) {
                        DataSnapshot usuarioEvento = dataSnapshot.child("usuario").child(entry.getKey()).child("evento").child(equipoID);
                        if (usuarioEvento.exists()) {
                            if (usuarioEvento.getValue(Boolean.class)) {
                                Usuario usuario = dataSnapshot.child("usuario").child(entry.getKey()).getValue(Usuario.class);
                                usuario.setId(entry.getKey());
                                adapter.add(usuario);
                            }
                        } else {
                            dataSnapshot.child("evento").child(equipoID).child("usuario").child(entry.getKey()).getRef().removeValue();
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
