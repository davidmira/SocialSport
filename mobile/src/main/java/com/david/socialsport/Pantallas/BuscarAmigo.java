package com.david.socialsport.Pantallas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.socialsport.Adapters.AdapterAmigos;
import com.david.socialsport.Adapters.AdapterBuscarAmigos;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by david on 13/12/17.
 */

public class BuscarAmigo extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    AdapterBuscarAmigos adapter;
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    EditText buscarAmigo;
    ActionBar menuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_amigos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuBar = getSupportActionBar();
        if (menuBar != null) {
            menuBar.setDisplayHomeAsUpEnabled(true);

        }

        adapter = new AdapterBuscarAmigos(getBaseContext(),new ArrayList<Usuario>());
        ListView listView = (ListView) findViewById(R.id.lista_buscar_amigos);
        listView.setAdapter(adapter);

        buscarAmigo = (EditText) findViewById(R.id.buscar_amigo);

        buscarAmigo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                System.out.println("buscando");
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    buscar();
                    return true;
                }
                return false;
            }
        });
    }


    public void buscar() {
        final String busqueda = buscarAmigo.getText().toString();
        String buscarPor = "nombre";
        adapter.clear();

        final Query query = myRef.child("usuario").orderByChild(buscarPor).startAt(busqueda).endAt(busqueda + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.child("usuario").child(userID).exists())
                            continue;
                        Usuario usuario = snapshot.getValue(Usuario.class);
                        usuario.setId(snapshot.getKey());
                        adapter.add(usuario);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getBaseContext(), "No existe ningún usuario con el nombre "+busqueda, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
