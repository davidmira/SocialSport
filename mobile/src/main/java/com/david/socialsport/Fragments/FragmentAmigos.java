package com.david.socialsport.Fragments;

import android.icu.lang.UScript;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.david.socialsport.Adapters.AdapterAmigos;
import com.david.socialsport.Objetos.Comentarios;
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
import java.util.List;
import java.util.Map;

/**
 * Created by david on 10/07/2017.
 */

public class FragmentAmigos  extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();
    AdapterAmigos adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyText;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterAmigos(getContext(), savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return inflater.inflate(R.layout.lista_amigos, container, false);

        View rootView = inflater.inflate(R.layout.lista_amigos, container, false);


        emptyText = (TextView) rootView.findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);

        listView = (ListView) rootView.findViewById(R.id.listaAmigos);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refrescar_amigos);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
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
