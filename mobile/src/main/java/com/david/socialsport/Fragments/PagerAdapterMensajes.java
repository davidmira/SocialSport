package com.david.socialsport.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by david on 30/06/2017.
 */

public class PagerAdapterMensajes extends FragmentStatePagerAdapter {
    private int numeroTabs;


    public PagerAdapterMensajes(FragmentManager fm, int numeroTabs) {
        super(fm);
        this.numeroTabs = numeroTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragmentMensajesRecibidos();
            case 1:
                return new FragmentMensajesEnviados();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numeroTabs;
    }
}
