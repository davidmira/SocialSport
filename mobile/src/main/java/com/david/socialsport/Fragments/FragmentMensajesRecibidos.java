package com.david.socialsport.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.david.socialsport.Adapters.AdapterComentarios;
import com.david.socialsport.Adapters.AdapterEventos;
import com.david.socialsport.Adapters.AdapterMensajesPersonalesRecibidos;
import com.david.socialsport.Objetos.Comentarios;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;

/**
 * Created by david on 30/06/2017.
 */

public class FragmentMensajesRecibidos extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private String userID;
    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();

    AdapterMensajesPersonalesRecibidos adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyText;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterMensajesPersonalesRecibidos(getContext(), savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return inflater.inflate(R.layout.lista_mensaje_personal, container, false);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        View rootView = inflater.inflate(R.layout.lista_mensaje_personal, container, false);


        emptyText = (TextView) rootView.findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);

        listView = (ListView) rootView.findViewById(R.id.listaMensajes);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refrescar_mensajes);
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

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
                };
                Map<String, Object> mensajesId = dataSnapshot.child("usuario").child(userID).child("mensaje").child("recibido").getValue(t);

                if (mensajesId != null) {
                    for (final String id : mensajesId.keySet()) {
                        Comentarios mensaje = dataSnapshot.child("mensaje").child(id).getValue(Comentarios.class);
                        if (mensaje != null) {
                            mensaje.setIdComentario(id);
                            adapter.add(mensaje);

                            } else {
                                for (Map.Entry<String, Object> entry : mensajesId.entrySet()) {
                                    dataSnapshot.child("mensaje").child(entry.getKey()).child(id).getRef().removeValue();
                                }
                            }


                    }
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
}
