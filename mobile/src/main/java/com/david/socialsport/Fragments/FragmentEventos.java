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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.socialsport.Adapters.AdapterEventos;
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

import static com.david.socialsport.R.id.map;

/**
 * Created by david on 03/04/2017.
 */

public class FragmentEventos extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();

    AdapterEventos adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterEventos(getContext(), savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab_fragment_eventos, container, false);

        listView = (ListView) rootView.findViewById(R.id.listaEventos);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                adapter.collapseCurrent(listView);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterEventos adapter = ((AdapterEventos) listView.getAdapter());
                if (view.findViewById(R.id.expandible).getVisibility() == View.VISIBLE) {
                    adapter.collapseItem(view);
                } else {
                    adapter.collapseCurrent(listView);
                    if (adapter.expandItem(position, view)) {

                        listView.setSelection(position);
                        listView.smoothScrollToPosition(position);
                    }
                }
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
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        ((AdapterEventos) listView.getAdapter()).collapseCurrent(listView);

        final Date ahoraDate = new Date();
        ahoraDate.setHours(ahoraDate.getHours() - 2);

        miReferencia.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
                };
                Map<String, Object> eventosId = dataSnapshot.child("evento").getValue(t);
                if (eventosId != null) {
                    for (Map.Entry<String, Object> entry : eventosId.entrySet()) {
                       for (String id : eventosId.keySet()) {
                            Evento evento = dataSnapshot.child("evento").child(id).getValue(Evento.class);
                            if (evento != null) {
                                evento.setId(id);
                                if (evento.getFecha_hora_menos1900().before(ahoraDate)) { //si el evento esta en el pasado se borra
                                    dataSnapshot.child("eventos").child(id).getRef().removeValue();
                                } else {
                                    adapter.add(evento);
                                }
                            } else {
                                dataSnapshot.child("evento").child(entry.getKey()).child(id).getRef().removeValue();
                            }
                        }
                    }
                    adapter.sort(new Comparator<Evento>() {
                        @Override
                        public int compare(Evento o1, Evento o2) {
                            return o1.getFecha_hora().compareTo(o2.getFecha_hora());
                        }
                    });
                    adapter.notifyDataSetChanged();
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