package com.example.jordi.fitplus;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jordi.fitplus.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SlideMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    TextView full_name_et, email_et, toolbarTitle;

    private ViewPager mViewPager;

    SectionsStatePagerAdapter adapter;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title_tv);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        final Menu menu = navigationView.getMenu();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        full_name_et = headerView.findViewById(R.id.full_name_tv);
        email_et = headerView.findViewById(R.id.email_et);

        DatabaseReference reference = myRef.child("Users").child(currentUser.getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);

                full_name_et.setText(user.name + " " + user.lastName);
                email_et.setText(user.email);

                if (user.type.equals("Trainer") || user.type.equals("Admin")) {
                    menu.findItem(R.id.nav_calendar).setVisible(true);
                    menu.findItem(R.id.nav_add_training).setVisible(true);
                    menu.findItem(R.id.nav_settings).setVisible(true);
                    menu.findItem(R.id.nav_sign_off).setVisible(true);
                } else if (user.type.equals("User") || user.type.equals("Admin")) {
                    menu.findItem(R.id.nav_calendar).setVisible(true);
                    menu.findItem(R.id.nav_reservations).setVisible(true);
                    menu.findItem(R.id.nav_history).setVisible(true);
                    menu.findItem(R.id.nav_settings).setVisible(true);
                    menu.findItem(R.id.nav_sign_off).setVisible(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        reference.addValueEventListener(postListener);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.scenario, new CalendarFragment()).commit();
        toolbarTitle.setText(getString(R.string.calendar));
        menu.findItem(R.id.nav_calendar).setChecked(true);
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
        getMenuInflater().inflate(R.menu.slide_menu, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            fragmentManager.beginTransaction().replace(R.id.scenario, new CalendarFragment()).commit();
            toolbarTitle.setText(getString(R.string.calendar));
        } else if (id == R.id.nav_reservations) {
            fragmentManager.beginTransaction().replace(R.id.scenario, new ReservationsFragment()).commit();
            toolbarTitle.setText(getString(R.string.reservations));
        } else if (id == R.id.nav_history) {
            fragmentManager.beginTransaction().replace(R.id.scenario, new HistoryFragment()).commit();
            toolbarTitle.setText(getString(R.string.history));
        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction().replace(R.id.scenario, new SettingsFragment()).commit();
            toolbarTitle.setText(getString(R.string.settings));
        } else if (id == R.id.nav_sign_off) {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
