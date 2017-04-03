package com.david.socialsport.Fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.david.socialsport.Adapters.AdapterEventos;
import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.Objetos.Usuario;
import com.david.socialsport.R;

import java.util.ArrayList;

/**
 * Created by david on 03/04/2017.
 */

public class FragmentEventos extends Fragment{
    ArrayList<Evento> eventos;
    Usuario usuario;


    AdapterEventos adapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.tab_fragment_eventos, container, false);
        adapter = new AdapterEventos(getContext(), eventos, usuario);
        listView = (ListView) rootView.findViewById(R.id.listView);
        //listView.setAdapter(adapter);

        return rootView;
    }

}
