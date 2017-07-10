package com.david.socialsport.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.david.socialsport.Objetos.Usuario;

import java.util.ArrayList;

/**
 * Created by david on 10/07/2017.
 */

public class AdapterAmigos extends ArrayAdapter<Usuario>{

    private Bundle savedInstanceState;

    public AdapterAmigos(@NonNull Context context, Bundle savedInstanceState) {
        super(context, 0, new ArrayList<Usuario>());
        this.savedInstanceState = savedInstanceState;
    }
}
