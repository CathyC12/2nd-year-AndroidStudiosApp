package ayry.com.ary_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsfeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    NewsfeedRecylerViewAdapter adapter;

    List<EventsModel> eventsList;
    DrawerLayout mDrawerLayot;

    private ArrayList<EventsModel> listOfEvents;
    private ArrayList<EventsModel> tempEventList;
    private ArrayList<EventsModel> listOfEventsArabic;
    private ArrayList<EventsModel> tempEventListArabic;
    private boolean arabicTransBtnClick;
    private boolean englishTransBtnClick;
    private TextView userName,userEmail;
    ProgressDialog progressDialog;
    ProgressDialog rerefreshDialog;
    DetailsUserStoreLocal userLocaldetails;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEvents);
        setSupportActionBar(toolbar);


        // Sets up the navigation drawer and adds an event listener so when the drawer toggle
        mDrawerLayot = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayot, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayot.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Inflates the nav_header layout as the header and then access the elements in the nav_header to populate with user details in the drawer
        View navHeader = navigationView.getHeaderView(0);
        userName = (TextView) navHeader.findViewById(R.id.usernameTV);
        userEmail = (TextView) navHeader.findViewById(R.id.emailTV);



        eventsList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsfeedRecylerViewAdapter(this);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NewsfeedRecylerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventsModel e = eventsList.get(position);
                Toast.makeText(getApplicationContext(), e.getTitle() + " was clicked", Toast.LENGTH_LONG).show();
            }
        });

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        userLocaldetails = new DetailsUserStoreLocal(this);

            /* declares the arraylist ot hold the reived text and also the translated */
        listOfEvents = new ArrayList<>();
        tempEventList = new ArrayList<>();
        listOfEventsArabic = new ArrayList<>();
        tempEventListArabic = new ArrayList<>();


        /* Instantiates the progress dialog so it can be shown on the translate request to */
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Translating");
        progressDialog.setMessage("Please wait....");


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


    /*
      * The method starts a Volley JsonArrayRequest, which is used for making network request by use of requestQueues
      * Volley also handles the request on separate worker threads and returns the parsed data back to the main thread
      * The data is recivied form the server as a jsonArray and then parsed, then used to create a shop-list item and added to a array list of shops,
      * which is then passed to the update function to update the recylerView with the recivied data
    */


    public void retrieveShopList() {

        String url = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Pull-NewsFeed/PullNewsfeedData.php";
        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //check the response from the server
                        Log.i("New Response", response.toString());

                        if (!(listOfEvents == null)) {
                            listOfEvents.clear();
                        }

                        if(!(tempEventList == null)){
                            tempEventList.clear();
                        }

                        if(!(listOfEventsArabic == null)){
                            listOfEventsArabic.clear();
                        }

                        if(!(tempEventListArabic == null)){
                            tempEventListArabic.clear();
                        }


                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject shopObj = (JSONObject) response.get(i);

                                int id = Integer.parseInt(shopObj.getString(tagId));
                                String eventCat = shopObj.getString(tagCatagory);
                                String eventTitle = shopObj.getString(tagtitle);
                                String eventLocation = shopObj.getString(tagLocation);
                                String eventTime =shopObj.getString(tagpTime);
                                String eventDate  = shopObj.getString(tagDate);
                                Double eventLat = Double.parseDouble(shopObj.getString(tagLat));
                                Double eventLong = Double.parseDouble(shopObj.getString(tagLong));





                                EventsModel event = new EventsModel(id, eventCat, eventTitle, eventLocation,eventTime,eventDate,eventLat,eventLong);
                                listOfEvents.add(event);

                            }

                            // convertToArabic();
                            updateShopListEnglish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("JSONError", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Pulling ShopData", "Error" + error.getMessage());
            }
        });

        //adding request to the request queue which is kept in a singleton as to make the request queue last for application lifecycle
        AppController.getInstance().addToRequestQueue(request);
    }



    /* the microsoft translator api */

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



}
