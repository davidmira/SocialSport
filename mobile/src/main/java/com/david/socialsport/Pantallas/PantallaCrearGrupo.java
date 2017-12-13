package com.david.socialsport.Pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.david.socialsport.Adapters.AdapterBuscarAmigos;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by david on 13/12/17.
 */

public class PantallaCrearGrupo extends AppCompatActivity {

    private String grupoId, usuarioId, nombreGrupo, imagenGRupo;

    EditText textGrupo;
    CircleImageView imagenCircleGrupo;
    FloatingActionButton botonGuardar;

    ActionBar menuBar;

    AdapterBuscarAmigos adapter;


    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuBar = getSupportActionBar();
        if (menuBar != null) {
            menuBar.setDisplayHomeAsUpEnabled(true);

        }

        textGrupo=(EditText) findViewById(R.id.nombreGRupo);
        imagenCircleGrupo=(CircleImageView) findViewById(R.id.imagenGrupo);
        botonGuardar=(FloatingActionButton) findViewById(R.id.boton_guardar);

        adapter = new AdapterBuscarAmigos(getBaseContext(),new ArrayList<Usuario>());
        ListView listView = (ListView) findViewById(R.id.lista_buscar_amigos);
        listView.setAdapter(adapter);
        cargarAmigos();

    }
    public void cargarAmigos() {
        miReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                adapter.clear();
                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
                };
                Map<String, Object> amigosId = dataSnapshot.child("usuario").child(userID).child("amigos").getValue(t);
                if (amigosId != null) {
                    for (final String id : amigosId.keySet()) {
                        Usuario usuario = dataSnapshot.child("usuario").child(id).child("amigos").getValue(Usuario.class);
                        if (usuario != null) {
                            usuario.setIdAmigo(id);
                            adapter.add(usuario);

                        } else {
                        }
                    }
                    adapter.sort(new Comparator<Usuario>() {
                        @Override
                        public int compare(Usuario o1, Usuario o2) {
                            return o2.getIdAmigo().compareTo(o1.getIdAmigo());
                        }
                    });
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
