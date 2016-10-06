package ayry.com.ary_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kearn on 18/04/2016.
 */
public class ResultsEventSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    EventsSearchAdapter adapter;
    ObjectRequestHolder objHolder;

    List<EventsModel> eventsList;
    DrawerLayout mDrawerLayot;

    private ArrayList<EventsModel> listOfEvents;
    private ArrayList<EventsModel> tempEventList;
    private ArrayList<EventsModel> listOfEventsArabic;
    private ArrayList<EventsModel> tempEventListArabic;
    private boolean arabicTransBtnClick;
    private boolean englishTransBtnClick;
    private TextView userName, userEmail;
    ProgressDialog progressDialog;
    ProgressDialog rerefreshDialog;
    DetailsUserStoreLocal userLocaldetails;
    String catagory, date, location;

    /* used for parsing json */
    //Tags for parsing json
    private static final String tagId = "id";
    private static final String tagCatagory = "catagory";
    private static final String tagtitle = "title";
    private static final String tagLocation = "location";
    private static final String tagpTime = "eventTime";
    private static final String tagDate = "eventDate";
    private static final String tagLat = "latitude";
    private static final String tagLong = "longitude";
    String url = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Pull-NewsFeed/SearchEvents.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_search_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);


        // Sets up the navigation drawer and adds an event listener so when the drawer toggle
        mDrawerLayot = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayot, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayot.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();
        catagory = bundle.getString("event_catagory");
        date = bundle.getString("event_date");
        location = bundle.getString("event_location");


        Log.d("bundle", catagory + date + location);


        //Inflates the nav_header layout as the header and then access the elements in the nav_header to populate with user details in the drawer
        View navHeader = navigationView.getHeaderView(0);
        userName = (TextView) navHeader.findViewById(R.id.usernameTV);
        userEmail = (TextView) navHeader.findViewById(R.id.emailTV);


        eventsList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.mRecylerView);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventsSearchAdapter(this);

        recyclerView.setAdapter(adapter);

        ObjectRequestHolder objHolder = new ObjectRequestHolder();

        EventsModel event = new EventsModel(catagory,date,location);

        objHolder.setEvent(event);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*

    userLocaldetails = new DetailsUserStoreLocal(this);

            declares the arraylist ot hold the reived text and also the translated *

    listOfEvents = new ArrayList<>();
    tempEventList = new ArrayList<>();
    listOfEventsArabic = new ArrayList<>();
    tempEventListArabic = new ArrayList<>();


         Instantiates the progress dialog so it can be shown on the translate request to




    rerefreshDialog = new ProgressDialog(this);
    rerefreshDialog .setCancelable(false);
    rerefreshDialog .setTitle("Refreshing");
    rerefreshDialog .setMessage("Please wait....");
}



    private void displayUserDetails() {
        User user = userLocaldetails.UserLoggedIn();

        //set text views
        // View header = navigationView.

        //displayUsernameTV.setText(user.getUserName());
        //displayUseremailTV.setText(user.getEmail());
        userName.setText(user.getName());
        userEmail.setText(user.getEmail());


        Log.i("user Loggedin", user.getUserName() + user.getEmail());


    } */
    }



    public void updateShopListEnglish(){
        if(arabicTransBtnClick == false) {
            if (!(listOfEvents== null)) {
                adapter.clear();
                tempEventList.clear();

                for (int i = 0; i < listOfEvents.size(); i++) {
                    tempEventList.add(listOfEvents.get(i));
                    EventsModel event = tempEventList.get(i);
                    Log.d("Data tempLst", "data in temp list after update called");
                    Log.d("data", event.getTitle() + " " + event.getTime());
                }

                adapter.addAll(tempEventList);
                progressDialog.hide();
                rerefreshDialog.hide();
            }


        }



    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_refresh) {
            //check which button has been click i.e. to refresh data in arabic or in english dependent on which is pressed
            if(englishTransBtnClick == true){
                rerefreshDialog.show();
                retrieveShopList();
            }else if(arabicTransBtnClick == true){
                rerefreshDialog.show();
                retrieveShopList();
                convertToArabic();
            }else{
                //default case as onStart english data is loaded so this takes care of the case were user refresh without clicking a button
                retrieveShopList();
            }
        }else if(id == R.id.action_english){
            englishTransBtnClick = true;
            arabicTransBtnClick = false;
            progressDialog.show();
            retrieveShopList();
        }else if(id == R.id.action_arabic){
            arabicTransBtnClick = true;
            englishTransBtnClick = false;
            progressDialog.show();
            retrieveShopList();
            convertToArabic();
        }

        return super.onOptionsItemSelected(item);
    }
  */

    @Override
    protected void onResume(){
        super.onResume();
        searchEvents(objHolder);
    }

    private String postGetMethodUrl(){
        return url + "?category="  +catagory+ "?date=" +
                date + "?location=" + location;


    }

    /*
      * The method starts a Volley JsonArrayRequest, which is used for making network request by use of requestQueues
      * Volley also handles the request on separate worker threads and returns the parsed data back to the main thread
      * The data is recivied form the server as a jsonArray and then parsed, then used to create a shop-list item and added to a array list of shops,
      * which is then passed to the update function to update the recylerView with the recivied data
    */




        public void  searchEvents(ObjectRequestHolder requestObj){
            DB_Sever_Request dbRequest = new DB_Sever_Request(this);
            dbRequest.SearchEvents(requestObj.getEvent(), new GetUserCallBack() {
                @Override
                public void finished(ObjectRequestHolder obj) {
                   eventsList = obj.getEventList();
                   for(int i = 0; i < eventsList.size(); i++){
                       EventsModel event = eventsList.get(i);
                       Log.i("async data", event.getTitle() + event.getCatagory());
                   }
                }
            });
        }






    /* the microsoft translator api

    public void convertToArabic() {

        new MyAsyncTask() {
            protected void onPostExecute(Boolean result) {

                progressDialog.hide();
                rerefreshDialog.hide();

                if (!(listOfEventsArabic == null)) {
                    adapter.clear();
                    tempEventListArabic.clear();

                    for (int i = 0; i < listOfEventsArabic.size(); i++) {
                        tempEventListArabic.add(listOfEventsArabic.get(i));
                        EventsModel event = tempEventListArabic.get(i);

                    }

                    adapter.addAll(tempEventListArabic);
                }
            }

        }.execute();



    }

class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
    @Override
    protected Boolean doInBackground(Void... arg0) {
        Translate.setClientId("Ary-Ireland-app");
        Translate.setClientSecret("OKuiH1Dq2uXViCmt7Mh5GC8nBE1IJA5UE7YAde1H6dQ=");
        try {

            for(int i = 0; i < listOfEvents.size(); i++){
                EventsModel eventAr = listOfEvents.get(i);
                String eventCat = Translate.execute(eventAr.getCatagory(), Language.ENGLISH, Language.ARABIC);
                String eventTitle = Translate.execute(eventAr.getTitle(), Language.ENGLISH, Language.ARABIC);
                String eventLocation = Translate.execute(eventAr.getLocatiion(), Language.ENGLISH, Language.ARABIC);


                EventsModel eventArabic = new EventsModel(eventAr.getId(), eventCat, eventTitle, eventLocation, eventAr.getTime(), eventAr.getDate(), eventAr.getLat(), eventAr.getLongattl());
                listOfEventsArabic.add(eventArabic);
            }

        } catch (Exception e) {
            Log.d("Error translating",e.toString());
        }

        return true;
    }
}

    private boolean authenticate() {
        Log.i("getLoggedIn value", "" + userLocaldetails.getLoggedIn());
        return userLocaldetails.getLoggedIn();
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            //display logged in or start main activity
            displayUserDetails();
        } else {
            //starts loginIn activity
            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);
        }

    }


    @Override
    protected void onResume(){
        super.onResume();
        retrieveShopList();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            // calls  the map activity, reason for using seperate activity is due to maps do not work with sliding activities
            Intent intent = new Intent(this, MapsActivityPage.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

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

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


*/


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}