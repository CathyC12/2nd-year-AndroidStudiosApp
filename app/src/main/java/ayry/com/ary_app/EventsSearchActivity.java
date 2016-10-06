package ayry.com.ary_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EventsSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {



    EditText eventCatagoryTV, eventDateTV, eventLocationTV;
    Button searchBtn;
    String eventCatagory, eventDate, eventLocation;
    DrawerLayout mDrawerLayot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_search);

        //sets up the applications toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Feature Coming Soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });





        // Sets up the navigation drawer and adds an on Navigation drawer
        mDrawerLayot = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayot, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayot.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //retrieves the criteria to search based on the users input,
        //it then starts a new results activity which retrieves the records from the database based on the criteria passed from this class
        eventCatagoryTV = (EditText) findViewById(R.id.events_catagory);
        eventDateTV = (EditText) findViewById(R.id.event_data);
        eventLocationTV = (EditText) findViewById(R.id.event_location);
        searchBtn = (Button) findViewById(R.id.eventSearchBtn);

        searchBtn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            // calls  the map activity, reason for using seperate activity is due to maps do not work with sliding activities
            Intent intent = new Intent(this, MapsActivityPage.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            //this is were a intent will be started for the newsfeed activity
            Intent intent = new Intent(this, NewsfeedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            // userLocaldetails.clearUserData();
            // User user = userLocaldetails.UserLoggedIn();
            // Log.i("User details clear", user.getEmail());
            Intent intent = new Intent(this, AccountSettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, EventsSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        //implement error check here to limit user error
        eventCatagory = eventCatagoryTV.getText().toString();
        eventDate = eventDateTV.getText().toString();
        eventLocation = eventLocationTV.getText().toString();
        //start a new activity and pass rhe data as arugments
        Intent i = new Intent(getApplicationContext(), ResultsEventSearchActivity.class);
        i.putExtra("event_catagory",eventCatagory);
        i.putExtra("event_date",eventDate);
        i.putExtra("event_location",eventLocation);
        startActivity(i);
    }
}
