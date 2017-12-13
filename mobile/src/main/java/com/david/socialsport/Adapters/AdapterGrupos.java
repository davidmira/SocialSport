package com.david.socialsport.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.david.socialsport.Objetos.Grupo;

import java.util.ArrayList;

/**
 * Created by david on 10/07/2017.
 */

public class AdapterGrupos extends ArrayAdapter<Grupo> {

    private Bundle savedInstanceState;
    public AdapterGrupos(@NonNull Context context, Bundle savedInstanceState) {
        super(context, 0, new ArrayList<Grupo>());
        this.savedInstanceState = savedInstanceState;
    }
}
