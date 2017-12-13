package com.david.socialsport.Pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.david.socialsport.Dialogs.InfoUsuarioAmigo;
import com.david.socialsport.Fragments.PagerAdapterAmigos;
import com.david.socialsport.Fragments.PagerAdapterMensajes;
import com.david.socialsport.R;

/**
 * Created by david on 10/07/2017.
 */

public class PantallaAmigos extends AppCompatActivity
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buscar, menu);
        return true;
    }

    private void tabs() {

        //Barra de pestañas
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.amigos));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.grupos));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Adaptador que gestiona los fragmentos
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapterAmigos adapter = new PagerAdapterAmigos
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
            case R.id.action_buscar:
                Intent intent = new Intent(getApplicationContext(), BuscarAmigo.class);
                getApplicationContext().startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}
