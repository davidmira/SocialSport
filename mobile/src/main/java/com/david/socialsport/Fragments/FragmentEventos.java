package com.david.socialsport.Fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.socialsport.Adapters.AdapterEventos;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.Pantallas.VerEvento;
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

import static com.david.socialsport.R.id.map;

/**
 * Created by david on 03/04/2017.
 */

public class FragmentEventos extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();

    AdapterEventos adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterEventos(getContext());
        View rootView = inflater.inflate(R.layout.tab_fragment_eventos, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listaEventos);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), VerEvento.class);
                intent.putExtra("eventoId",adapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refrescar_eventos);
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
        swipeRefreshLayout.setRefreshing(true);

        miReferencia.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
                };
                Map<String, Object> eventosId = dataSnapshot.child("evento").getValue(t);
                if (eventosId != null) {
                    for (Map.Entry<String, Object> entry : eventosId.entrySet()) {
                        String deporte = dataSnapshot.child("evento").child(entry.getKey()).child("deporte").getValue(String.class);
                        String localizacion = dataSnapshot.child("evento").child(entry.getKey()).child("localizacion").getValue(String.class);
                        String ubicacionEvento = dataSnapshot.child("evento").child(entry.getKey()).child("ubicacionEvento").getValue(String.class);
                        String tipoLugar = dataSnapshot.child("evento").child(entry.getKey()).child("tipoLugar").getValue(String.class);
                        Float precio = dataSnapshot.child("evento").child(entry.getKey()).child("precio").getValue(Float.class);
                        //Date fecha_hora = dataSnapshot.child("evento").child(entry.getKey()).child("fechaHora").getValue(Date.class);
                        for (String id : eventosId.keySet()) {
                            Evento evento = dataSnapshot.child("evento").child(id).getValue(Evento.class);
                            if (evento != null) {
                                evento.setDeporte(deporte);
                                evento.setLocalizacion(localizacion);
                                evento.setUbicacionEvento(ubicacionEvento);
                                evento.setTipoLugar(tipoLugar);
                                evento.setPrecio(precio);
                                evento.setId(entry.getKey());
                                //evento.setFecha_hora(fecha_hora);

                               adapter.add(evento);
                            } else {
                                dataSnapshot.child("evento").child(entry.getKey()).child(id).getRef().removeValue();
                            }
                        }
                    }
                    adapter.sort(new Comparator<Evento>() {
                        @Override
                        public int compare(Evento o1, Evento o2) {
                            return o1.getDeporte().compareTo(o2.getDeporte());
                        }
                    });
                    //adapter.notifyDataSetChanged();
                } else {
                    System.out.println("NO HAY EVENTOS DISPONIBLES");
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