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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountSettingsActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DetailsUserStoreLocal userDetails;
    TextView nameTV, usernameTV, userEmailTV, userPasswordTV;
    User user;
    Button logoutBtn;
    DrawerLayout mDrawerLayot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        //sets up the applications toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);







        // Sets up the navigation drawer and adds an on Navigation drawer
        mDrawerLayot = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayot, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayot.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*
          Will get the details of the user logged in by instanciating the SharedPerferance file which holds the slogged in users details localy
         */

           nameTV = (TextView) findViewById(R.id.nameuserTV);
           usernameTV = (TextView) findViewById(R.id.username_TV);
           userEmailTV = (TextView) findViewById((R.id.userEmailTV));
           userPasswordTV = (TextView) findViewById(R.id.userPasswordTV);
           logoutBtn = (Button) findViewById(R.id.logoutBtn);

           //Instanicates the sharedPerferance class to retrive the details about the user logged in

           userDetails = new DetailsUserStoreLocal(this);

           user = userDetails.UserLoggedIn();

            Log.i("username",user.getUserName());

           //set the textView with data from the user

           nameTV.setText(user.getName());
           usernameTV.setText(user.getUserName());
           userEmailTV.setText(user.getEmail());
          // userEmailTV.setText(user.getEmail());
           userPasswordTV.setText(user.getPassword());


           logoutBtn.setOnClickListener(this);

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

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        userDetails.clearUserData();
        User user = userDetails.UserLoggedIn();
        Log.i("User details clear","Email is" + user.getEmail());
        Intent intent = new Intent(this, UserLogin.class);
        startActivity(intent);
    }
}
