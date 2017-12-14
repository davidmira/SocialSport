package com.david.socialsport.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.david.socialsport.Adapters.AdapterAmigos;
import com.david.socialsport.Adapters.AdapterGrupos;
import com.david.socialsport.Dialogs.DialogMensaje;
import com.david.socialsport.Dialogs.VerComentarios;
import com.david.socialsport.Dialogs.VerComentariosGrupo;
import com.david.socialsport.Objetos.Grupo;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.Pantallas.CrearEvento;
import com.david.socialsport.Pantallas.PantallaCrearGrupo;
import com.david.socialsport.Pantallas.PantallaInfoUsuario;
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

/**
 * Created by david on 10/07/2017.
 */

public class FragmentGrupos extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();
    AdapterGrupos adapter;

    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyText;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterGrupos(getContext(), new ArrayList<Grupo>());
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return inflater.inflate(R.layout.lista_grupos, container, false);

        View rootView = inflater.inflate(R.layout.lista_grupos, container, false);


        emptyText = (TextView) rootView.findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);

        listView = (ListView) rootView.findViewById(R.id.listaGrupos);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refrescar_grupos);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });

        FloatingActionButton botonNuevoGrupo = (FloatingActionButton) rootView.findViewById(R.id.boton_crear_grupo);
        botonNuevoGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PantallaCrearGrupo.class);
                getContext().startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), VerComentariosGrupo.class);
                intent.putExtra("grupoID", adapter.getItem(position).getId());
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onRefresh() {
        if (swipeRefreshLayout == null) return;
        swipeRefreshLayout.setRefreshing(true);
        emptyText.setVisibility(View.INVISIBLE);
        miReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        @Override

        public void onDataChange(DataSnapshot dataSnapshot) {
            adapter.clear();
            GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
            };
            Map<String, Object> grupoId = dataSnapshot.child("usuario").child(userID).child("grupo").getValue(t);
            if (grupoId != null) {
                for (final String id : grupoId.keySet()) {
                    Grupo grupo = dataSnapshot.child("grupo").child(id).getValue(Grupo.class);
                    if (grupo != null) {
                        grupo.setId(id);
                        adapter.add(grupo);

                    } else {
                    }
                }
                adapter.sort(new Comparator<Grupo>() {
                    @Override
                    public int compare(Grupo o1, Grupo o2) {
                        return o2.getId().compareTo(o1.getId());
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

}
