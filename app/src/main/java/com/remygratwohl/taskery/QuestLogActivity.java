package com.remygratwohl.taskery;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.remygratwohl.taskery.Adapters.QuestAdapter;
import com.remygratwohl.taskery.database.DatabaseHelper;
import com.remygratwohl.taskery.models.Character;
import com.remygratwohl.taskery.models.Quest;
import com.remygratwohl.taskery.models.SessionManager;

import java.util.ArrayList;

public class QuestLogActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static RecyclerView recyclerView;
    private static DatabaseHelper db;
    private ArrayList<Quest> quests;
    private static QuestAdapter adapter;
    private static DrawerLayout drawer;


    // Drawer UI
    private static SwitchCompat filter_daily_switch;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Quests");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuestCreatorActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_the_right,R.anim.slide_to_the_left);
            }
        });

        // Initialize he Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_view_right);
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.filter_daily){
                    filter_daily_switch.setChecked(!filter_daily_switch.isChecked());
                    Log.d("LOG","TEST");
                }

                return true;
            }
        });

        MenuItem item = rightNavigationView.getMenu().findItem(R.id.filter_daily);
        filter_daily_switch = (SwitchCompat) MenuItemCompat.getActionView(item).findViewById(R.id.action_toggle_dailies);


        filter_daily_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, (filter_daily_switch.isChecked()) ? "is checked!!!" : "not checked!!!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

       /* DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
        SessionManager sm = new SessionManager(getApplicationContext());

        Character c = dbh.getCharacter(sm.retrieveSessionsUser().getEmail());
        Log.d("LOG",c.toString());
        */

       db = new DatabaseHelper(getApplicationContext());
       quests = db.getQuests();

       recyclerView = (RecyclerView) findViewById(R.id.questRecyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       recyclerView.setItemAnimator(new DefaultItemAnimator());

        quests = new ArrayList<Quest>();
        quests.clear();
        quests.addAll(db.getQuests());

       adapter = new QuestAdapter(quests);
       recyclerView.setAdapter(adapter);




    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            // Do nothing
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        quests.clear();

        db = new DatabaseHelper(getApplicationContext());
        quests.addAll(db.getQuests());
        Log.d("LOG","Size " + db.getQuests().size());

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quest_log, menu);
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
            return true;
        }else if(id == R.id.action_openRightDrawer){
            drawer.openDrawer(GravityCompat.END);
            return true;
        }else if (id == R.id.action_logout){
            SessionManager sManager = new SessionManager(getApplicationContext());
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

            sManager.deleteUserSession(); // Delete user session
            dbHelper.deteleteDB(getApplicationContext()); // Wipe Database

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
