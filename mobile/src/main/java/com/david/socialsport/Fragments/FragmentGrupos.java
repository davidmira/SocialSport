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
import android.widget.ListView;
import android.widget.TextView;

import com.david.socialsport.Adapters.AdapterAmigos;
import com.david.socialsport.Pantallas.CrearEvento;
import com.david.socialsport.Pantallas.PantallaCrearGrupo;
import com.david.socialsport.Pantallas.PantallaInfoUsuario;
import com.david.socialsport.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by david on 10/07/2017.
 */

public class FragmentGrupos extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    AdapterAmigos adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyText;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        adapter = new AdapterAmigos(getContext(), savedInstanceState);
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

        return rootView;
    }


    @Override
    public void onRefresh() {
        if (swipeRefreshLayout == null) return;
        swipeRefreshLayout.setRefreshing(true);
        emptyText.setVisibility(View.INVISIBLE);


    }
}
