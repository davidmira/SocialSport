package com.david.socialsport.Fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.david.socialsport.R;

/**
 * Created by david on 03/04/2017.
 */


public class PagerAdapter extends FragmentStatePagerAdapter {
    int numeroTabs;


    public PagerAdapter(FragmentManager fm, int numeroTabs) {
        super(fm);
        this.numeroTabs = numeroTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentEventos tab1 = new FragmentEventos();
                return tab1;
            case 1:
                FragmentMisEventos tab2 = new FragmentMisEventos();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numeroTabs;
    }
}