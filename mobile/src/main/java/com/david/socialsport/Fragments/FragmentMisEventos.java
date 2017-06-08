package com.david.socialsport.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.david.socialsport.Adapters.AdapterEventos;
import com.david.socialsport.Adapters.AdapterMisEventos;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * Created by david on 03/04/2017.
 */

public class FragmentMisEventos extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();

    AdapterMisEventos adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterMisEventos(getContext());

        View rootView = inflater.inflate(R.layout.tab_fragment_mis_eventos, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listaMisEventos);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refrescar_mis_eventos);
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


        final Date cincoDiasDate = new Date();
        cincoDiasDate.setDate(cincoDiasDate.getDate()-5);

        miReferencia.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                //GenericType que permite extraer maps de firebase
                GenericTypeIndicator<Map<String, Boolean>> t = new GenericTypeIndicator<Map<String, Boolean>>() {
                };
                //Se accede al usuario con el id de usuario, y se guardan en un hashmap su codigos de evento (que vinen acompañados de un boolean que usare para indicar
                //si esta suscrito)
                Map<String, Boolean> eventosId = dataSnapshot.child("usuario").child(idUsuario).child("evento").getValue(t);
                if (eventosId != null) { //se comprueba que no sea nulo, si fuese nulo el usuario no tendria ningun evento y no se hace nada
                    for (Map.Entry<String, Boolean> entry : eventosId.entrySet()) { //se recorren las claves de los eventos y sus valores
                        if (entry.getValue()) {//si el valor es true el usuaio esta dentro del evento y se procede, se cogen los id de los eventos y se añaden a una lista
                            /*String deporte = dataSnapshot.child("evento").child(entry.getKey()).child("deporte").getValue(String.class);
                            String localizacion = dataSnapshot.child("evento").child(entry.getKey()).child("localizacion").getValue(String.class);
                            String ubicacionEvento = dataSnapshot.child("evento").child(entry.getKey()).child("ubicacionEvento").getValue(String.class);
                            String tipoLugar = dataSnapshot.child("evento").child(entry.getKey()).child("tipoLugar").getValue(String.class);
                            Float precio = dataSnapshot.child("evento").child(entry.getKey()).child("precio").getValue(Float.class);
//                            Date fecha_hora = dataSnapshot.child("evento").child(entry.getKey()).child("fechaHora").getValue(Date.class);*/

                            for (String id : eventosId.keySet()) {
                                Evento evento = dataSnapshot.child("evento").child(id).getValue(Evento.class);
                                if (evento != null) {
                                   /* evento.setDeporte(deporte);
                                    evento.setLocalizacion(localizacion);
                                    evento.setUbicacionEvento(ubicacionEvento);
                                    evento.setTipoLugar(tipoLugar);
                                    evento.setPrecio(precio);*/
                                   // evento.setFecha_hora(fecha_hora);
                                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                    adapter.add(evento);
                                } else {
                                    dataSnapshot.child("evento").child(entry.getKey()).child(id).getRef().removeValue();
                                }
                            }
                        }
                    }
                    adapter.sort(new Comparator<Evento>() {
                        @Override
                        public int compare(Evento o1, Evento o2) {
                            return o1.getDeporte().compareTo(o2.getDeporte());
                        }
                    });
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println("NO HAY EVENTOS PARA ESTE JUGADOR");
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    /* if (evento.getFecha_hora_menos1900().before(cincoDiasDate)){//si el evento ha pasado hace más de cinco días se borra en el historial de usuario
        dataSnapshot.child("usuario").child(id).child("evento").child(id).getRef().removeValue();
    }*/
}
