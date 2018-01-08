package com.absontwikkeling.rijtjes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import layout.About;
import layout.createList;
import layout.displayList;

public class NavMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayList displayList = new displayList();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativelayout_fragment, displayList).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            layout.settings settings = new layout.settings();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment, settings).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_enter) {
            createList createList = new createList();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment, createList).commit();
        } else if (id == R.id.nav_check) {
            displayList displayList = new displayList();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment, displayList).commit();
        } /*else if (id == R.id.nav_practise) {
            Toast.makeText(this, "Trial and error, is er nog niet", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_test){
            Toast.makeText(this, "For real, is er nog niet", Toast.LENGTH_SHORT).show();
        } */ else if (id == R.id.nav_manage) {
            layout.settings settings = new layout.settings();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment, settings).commit();
        } /* else if (id == R.id.nav_share) {
            Toast.makeText(this, "Uw Facebook account is gehackt", Toast.LENGTH_SHORT).show();
        } */ else if (id == R.id.nav_about) {
            About About = new About();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment, About).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
