package com.david.socialsport.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.socialsport.R;

/**
 * Created by david on 03/04/2017.
 */

public class FragmentMisEventos extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_mis_eventos, container, false);
    }
}
