package com.david.socialsport.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.david.socialsport.Adapters.AdapterMisEventos;
import com.david.socialsport.Dialogs.VerComentarios;
import com.david.socialsport.Dialogs.VerUsuarios;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.Pantallas.CrearEvento;
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
 * Created by david on 03/04/2017.
 */

public class FragmentMisEventos extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    String userID;
    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
    DatabaseReference miReferencia = database.getReference();

    AdapterMisEventos adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyText;
    ListView listView;

    FloatingActionButton  borrar, compartir;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterMisEventos(getContext(), savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return inflater.inflate(R.layout.lista_fragment_mis_eventos, container, false);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        View rootView = inflater.inflate(R.layout.lista_fragment_mis_eventos, container, false);

        emptyText = (TextView) rootView.findViewById(R.id.empty_mis);
        emptyText.setVisibility(View.INVISIBLE);

        listView = (ListView) rootView.findViewById(R.id.listaMisEventos);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(R.id.empty_mis));

        //Botón + flotante inferior
        FloatingActionButton botonAñadirEvento = (FloatingActionButton) rootView.findViewById(R.id.boton_anadir_partidos);
        botonAñadirEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(getContext(), CrearEvento.class));
            }
        });

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
                final AdapterMisEventos adapter = ((AdapterMisEventos) listView.getAdapter());
                if (view.findViewById(R.id.expandible).getVisibility() == View.VISIBLE) {
                    adapter.collapseItem(view);

                } else {
                    adapter.collapseCurrent(listView);
                    if (adapter.expandItem(position, view)) {
                        view.findViewById(R.id.eliminar_but).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                eliminarSuscripcionEvento(position);
                            }
                        });
                        view.findViewById(R.id.ver_participantes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                verUsuarios(position);
                            }
                        });
                        view.findViewById(R.id.ver_comentarios).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                verComentarios(position);
                            }
                        });
                        listView.setSelection(position);
                        //listView.smoothScrollToPosition(position);

                        miReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot evento = dataSnapshot.child("evento").child(adapter.getItem(position).getId()).child("usuarios").child(userID);

                                    borrar = (FloatingActionButton) getView().findViewById(R.id.eliminar_but);
                                    compartir = (FloatingActionButton) getView().findViewById(R.id.compartir_but);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
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
        if (swipeRefreshLayout == null) return;
        swipeRefreshLayout.setRefreshing(true);
        emptyText.setVisibility(View.INVISIBLE);
        ((AdapterMisEventos) listView.getAdapter()).collapseCurrent(listView);

        final Date cincoDiasDate = new Date();
        cincoDiasDate.setDate(cincoDiasDate.getDate() - 5);
        miReferencia.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                //GenericType que permite extraer maps de firebase
                GenericTypeIndicator<Map<String, Boolean>> t = new GenericTypeIndicator<Map<String, Boolean>>() {
                };
                //Se accede al usuario con el id de usuario, y se guardan en un hashmap su codigos de evento (que vinen acompañados de un boolean que usare para indicar
                //si esta suscrito)
                Map<String, Boolean> eventosId = dataSnapshot.child("usuario").child(userID).child("evento").getValue(t);
                if (eventosId != null) { //se comprueba que no sea nulo, si fuese nulo el usuario no tendria ningun evento y no se hace nada
                  // for (Map.Entry<String, Boolean> entry : eventosId.entrySet()) { //se recorren las claves de los eventos y sus valores
                     //   if (entry.getValue()) {//si el valor es true el usuaio esta dentro del evento y se procede, se cogen los id de los eventos y se añaden a una lista
                            for (final String id : eventosId.keySet()) {
                                Evento evento = dataSnapshot.child("evento").child(id).getValue(Evento.class);
                                if (evento != null) {
                                    evento.setId(id);
                                    if (evento.getFecha_hora_menos1900().before(cincoDiasDate)) { //si el evento esta 5 días en el pasado se borra
                                        dataSnapshot.child("evento").child(id).getRef().removeValue();
                                        dataSnapshot.child("usuario").child(userID).child("evento").child(id).getRef().removeValue();
                                    }
                                    adapter.add(evento);

                                    //inicializamos botones y colores de las tarjetas
                                    miReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            DataSnapshot evento = dataSnapshot.child("evento").child(id).child("usuarios").child(userID);

                                                borrar = (FloatingActionButton) getView().findViewById(R.id.eliminar_but);
                                                compartir = (FloatingActionButton) getView().findViewById(R.id.compartir_but);


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                  //  dataSnapshot.child("evento").child(entry.getKey()).child(id).getRef().removeValue();
                                }
                          //  }
                        }
                   // }
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
    public void verUsuarios(int position) {
        Intent intent = new Intent(getContext(), VerUsuarios.class);
        intent.putExtra("eventoID", adapter.getItem(position).getId());
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void verComentarios(int position) {
        Intent intent = new Intent(getContext(), VerComentarios.class);
        intent.putExtra("eventoID", adapter.getItem(position).getId());
        intent.putExtra("userID", userID);
        startActivity(intent);
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
                            //miReferencia.child("evento").child(e.getId()).child("usuarios").removeValue();
                            miReferencia.child("evento").child(e.getId()).child("usuarios").child(userID).removeValue();

                            borrar = (FloatingActionButton) getView().findViewById(R.id.eliminar_but);
                            compartir = (FloatingActionButton) getView().findViewById(R.id.compartir_but);
                            adapter.notifyDataSetChanged();
                            //tarjeta.setBackgroundColor(getResources().getColor(R.color.rowBackgroun));
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
