package com.david.socialsport.Pantallas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.david.socialsport.Fragments.PagerAdapterMensajes;
import com.david.socialsport.R;

import java.util.Date;

/**
 * Created by david on 29/06/2017.
 */

public class PantallaMensajesPersonales extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActionBar menuBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_pantalla_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuBar = getSupportActionBar();
        if (menuBar != null) {
            menuBar.setDisplayHomeAsUpEnabled(true);
        }

        tabs();
    }

    private void tabs() {

        //Barra de pestañas
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.recibidos));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.enviados));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Adaptador que gestiona los fragmentos
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapterMensajes adapter = new PagerAdapterMensajes
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
