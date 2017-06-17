package com.david.socialsport.Fragments;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;


/**
 * Created by david on 03/04/2017.
 */

public class FragmentEventos extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    String userID;
    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();

    AdapterEventos adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyText;
    ListView listView;
    Evento e;
    ArrayList<String> usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterEventos(getContext(), savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return inflater.inflate(R.layout.tab_fragment_eventos, container, false);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        View rootView = inflater.inflate(R.layout.tab_fragment_eventos, container, false);

        emptyText = (TextView) rootView.findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);

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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AdapterEventos adapter = ((AdapterEventos) listView.getAdapter());
                if (view.findViewById(R.id.expandible).getVisibility() == View.VISIBLE) {
                    adapter.collapseItem(view);
                } else {
                    adapter.collapseCurrent(listView);
                    if (adapter.expandItem(position, view)) {
                        view.findViewById(R.id.unirse_but).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                unirseEvento(position);
                            }
                        });
                        view.findViewById(R.id.eliminar_but).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                eliminarSuscripcionEvento(position);
                            }
                        });
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
    public void onRefresh() {
        if (swipeRefreshLayout == null) return;
        swipeRefreshLayout.setRefreshing(true);
        emptyText.setVisibility(View.INVISIBLE);
        ((AdapterEventos) listView.getAdapter()).collapseCurrent(listView);

        final Date ahoraDate = new Date();
        ahoraDate.setHours(ahoraDate.getMinutes() - 45);
        final Date cincoDiasDate = new Date();
        cincoDiasDate.setDate(cincoDiasDate.getDate() - 5);

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
                                if (evento.getFecha_hora_menos1900().before(ahoraDate)) { //si el evento esta a 45 minutos se oculta
                                    adapter.remove(evento);
                                }
                                if (evento.getFecha_hora_menos1900().before(cincoDiasDate)) {//si el evento esta 5 días en el pasado se borra de los eventos y del ususario
                                    dataSnapshot.child("evento").child(id).getRef().removeValue();
                                    dataSnapshot.child("usuario").child(userID).child("evento").child(id).getRef().removeValue();
                                }
                                if (evento.getFecha_hora_menos1900().after(ahoraDate)) {
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

    public void unirseEvento(int position) {
        e = (Evento) listView.getAdapter().getItem(position);
        AlertDialog.Builder unirse = new AlertDialog.Builder(getContext());

        unirse.setMessage(R.string.evento_participar)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unirse(e);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void unirse(Evento e) {
        miReferencia.child("usuario").child(userID).child("evento").child(e.getId()).setValue(true);
        //miReferencia.child("evento").child(e.getId()).child("usuario").setValue(userID);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        usuario=new ArrayList<>();
        usuario.add(String.valueOf(miReferencia.child("evento").child(e.getId()).child("usuarios").setValue(userID)));
        e.setUsuarios(usuario);
        System.out.println("usuarios:  "+e.getUsuarios());
       // e.usuarios.add(String.valueOf(miReferencia.child("evento").child(e.getId()).child("usuarios").setValue(userID)));

/*
String referencia= String.valueOf(miReferencia.child("evento").child(e.getId()).child("usuarios").child("id").setValue(userID));
        System.out.println("referencia:   "+referencia);
        usuarios.add(referencia);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        usuarios=new ArrayList<String>();
        usuarios.add(firebaseUser.getUid());
        e.setUsuarios(usuarios);
 */
    }

    public void eliminarSuscripcionEvento(int position) {

        final Evento e = (Evento) listView.getAdapter().getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (e.getCreadoPor().equals(userID)) {
            builder.setMessage(R.string.evento_eliminar)
                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            miReferencia.child("evento").child(e.getId()).removeValue();
                            miReferencia.child("usuario").child(userID).child("evento").child(e.getId()).removeValue();
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        } else
            builder.setMessage(R.string.evento_abandonar)
                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            miReferencia.child("usuario").child(userID).child("evento").child(e.getId()).removeValue();
                            miReferencia.child("evento").child(e.getId()).child("usuario").removeValue();
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}