package ayry.com.ary_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//imports for microsoft api
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    /* Varaibles */

    private  FragmentPagerAdapter FragmentPagerAdapter;
    private DrawerLayout mDrawerLayot;
    private DetailsUserStoreLocal userLocaldetails;
    private TextView displayUsernameTV;
    private TextView displayUseremailTV;
    private ObjectRequestHolder objWrapper;
    private Button mTranlateBtn;
    private RecyclerView rvShops;
    private ShoplistAdapter adapter;
    private TextView userEmail;
    private TextView userName;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Shop_items> listOfShops;
    private ArrayList<Shop_items> tempShopList;
    private ArrayList<Shop_items> listOfShopsArabic;
    private ArrayList<Shop_items> tempShopListArabic;
    private boolean arabicTransBtnClick;
    private boolean englishTransBtnClick;
    ProgressDialog progressDialog;
    ProgressDialog rerefreshDialog;

    /* used for parsing json */
    //Tags for parsing json
    private static final String tagId = "id";
    private static final String tagName = "shopname";
    private static final String tagDesc = "shopdesc";
    private static final String tagAddress = "shopaddress";
    private static final String tagpNum = "shopnumber";
    private static final String tagOpeningTime = "openingtime";
    private static final String tagClosingTime = "closingtime";
    private static final String tagLat = "lat";
    private static final String tagLong = "longatt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        //Creates a new instance of the ObjectRequest holder which is used as a wrapper for passing objects between Async task and the activities class
        objWrapper = new ObjectRequestHolder();


        userLocaldetails = new DetailsUserStoreLocal(this);



        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swpie_refresh_layout);
        rvShops = (RecyclerView) findViewById(R.id.rvshoplist);

        adapter = new ShoplistAdapter();

        adapter.notifyDataSetChanged();
        //LinearLayoutManager is used here, this lays out elements in a similar linear fashion
        rvShops.setLayoutManager(new LinearLayoutManager(this));

        rvShops.setAdapter(adapter);

        // Event listen which listens for refresh event when one happens the UpdateList method will be called,
        // This will update the data with the new data retrieved form the server
        // sets the colors used in the refresh animation

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_bright,
                android.R.color.holo_purple
        );;

        //retrieves the data from the database, and populates the recylerView with the data received from the async task


        /* declares the arraylist ot hold the reived text and also the translated */
        listOfShops = new ArrayList<>();
        tempShopList = new ArrayList<>();
        listOfShopsArabic = new ArrayList<>();
        tempShopListArabic = new ArrayList<>();


        /* Instantiates the progress dialog so it can be shown on the translate request and swipe action*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Translating");
        progressDialog.setMessage("Please wait....");

        rerefreshDialog = new ProgressDialog(this);
        rerefreshDialog .setCancelable(false);
        rerefreshDialog .setTitle("Refreshing");
        rerefreshDialog .setMessage("Please wait....");


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    private boolean authenticate() {
        Log.i("getLoggedIn value", "" + userLocaldetails.getLoggedIn());
        return userLocaldetails.getLoggedIn();
    }

 /*
   Displays details of user logged in in the dawer header
  */
    private void displayUserDetails() {
        User user = userLocaldetails.UserLoggedIn();


        userName.setText(user.getName());
        userEmail.setText(user.getEmail());


        Log.i("user Loggedin", user.getUserName() + user.getEmail());


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




    public void dataSource(ObjectRequestHolder requestObj) {
        //sets a reference to the object in this class for for accessibility between the object in the holder ObjectRequestHolder class
        this.objWrapper = requestObj;

    }

    /*
      * could take the data retrieved from the retriveShopList method and async  and then update the recylerView from there or from the update class by keeping track if a button has been pressed or not and conditions
     */


    public void updateShopListEnglish(){
        if(arabicTransBtnClick == false) {
            if (!(listOfShops == null)) {
                adapter.clear();
                tempShopList.clear();

                for (int i = 0; i < listOfShops.size(); i++) {
                    tempShopList.add(listOfShops.get(i));
                    Shop_items shop = tempShopList.get(i);
                    Log.d("Data tempLst", "data in temp list after update called");
                    Log.d("data", shop.getTitle() + " " + shop.getDesc());
                }

                adapter.addAll(tempShopList);
                progressDialog.hide();
                rerefreshDialog.hide();
            }
        }



    }


    /* the microsoft translator api */

    public void convertToArabic() {

        new MyAsyncTask() {
            protected void onPostExecute(Boolean result) {

                progressDialog.hide();
                rerefreshDialog.hide();

                if (!(listOfShopsArabic == null)) {
                    adapter.clear();
                    tempShopListArabic.clear();

                    for (int i = 0; i < listOfShopsArabic.size(); i++) {
                        tempShopListArabic.add(listOfShopsArabic.get(i));
                        Shop_items shop = tempShopListArabic.get(i);
                        Log.d("Data tempLst", "data in temp list after update called");
                        Log.d("data", shop.getTitle() + " " + shop.getDesc());
                    }

                    adapter.addAll(tempShopListArabic);
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

                for(int i = 0; i < listOfShops.size(); i++){
                    Shop_items shopsAr = listOfShops.get(i);
                    String shopTitleAr = Translate.execute(shopsAr.getTitle(), Language.ENGLISH, Language.ARABIC);
                    String shopDescAr = Translate.execute(shopsAr.getTitle(), Language.ENGLISH, Language.ARABIC);
                    String shopAddresAr = Translate.execute(shopsAr.getAddress(), Language.ENGLISH, Language.ARABIC);


                    Shop_items newShopArabic = new Shop_items(shopsAr.getId(), "", shopTitleAr, shopDescAr,shopAddresAr, shopsAr.getpNumber(), shopsAr.getOpeningtime(), shopsAr.getCloisngtime(),shopsAr.getLat(), shopsAr.getLongatt());
                    listOfShopsArabic.add(newShopArabic);
                }

            } catch (Exception e) {
                Log.d("Error translating",e.toString());
            }

            return true;
        }
    }

    /*
       * The method starts a Volley JsonArrayRequest, which is used for making network request by use of requestQueues
       * Volley also handle the request on separate worker threads and returns the parsed data back to the main thread
       * The data is recivied form the server as a jsonArray and then parsed, then used to create a shop-list item and added to a array list of shops,
       * which is then passed to the update function to update the recylerView with the recivied data
     */
    public void retrieveShopList() {


        mSwipeRefreshLayout.setRefreshing(true);
        String url = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/App-scripts/PullShopData.php";
        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //check the response from the server
                        Log.i("New Response", response.toString());

                        if (!(listOfShops == null)) {
                            listOfShops.clear();
                        }

                        if(!(tempShopList == null)){
                            tempShopList.clear();
                        }

                        if(!(listOfShopsArabic == null)){
                            listOfShopsArabic.clear();
                        }

                        if(!(tempShopListArabic == null)){
                            tempShopListArabic.clear();
                        }


                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject shopObj = (JSONObject) response.get(i);

                                int id = Integer.parseInt(shopObj.getString(tagId));
                                String shopname = shopObj.getString(tagName);
                                String shopdesc = shopObj.getString(tagDesc);
                                String shopAddress = shopObj.getString(tagAddress);
                                int shopNum = Integer.parseInt(shopObj.getString(tagpNum));
                                String shopOpenTime = shopObj.getString(tagOpeningTime);
                                String shopClosingTime = shopObj.getString(tagClosingTime);
                                Double lat = Double.parseDouble(shopObj.getString(tagLat));
                                Double longatt = Double.parseDouble(shopObj.getString(tagLong));


                                Log.i("shopid", "id" + id);//
                                Log.i("shopname", shopname);
                                Log.i("shopdesc", shopdesc);
                                Log.i("shopgeo","");

                                Shop_items shop = new Shop_items(id, "", shopname, shopdesc,shopAddress,shopNum,shopOpenTime,shopClosingTime,lat,longatt);
                                listOfShops.add(shop);

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
                mSwipeRefreshLayout.setRefreshing(false);
                Log.d("Pulling ShopData", "Error" + error.getMessage());
            }
        });

        //adding request to the request queue which is kept in a singleton as to make the request queue last for application lifecycle
        AppController.getInstance().addToRequestQueue(request);
    }


    @Override
    protected void onResume() {
        super.onResume();
        retrieveShopList();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /*
      Will have to keep track of which event it was called on i.e. of which click has been performed,
     */
    @Override
    public void onRefresh() {
        Toast.makeText(MainActivity.this, "Swipe refresh working", Toast.LENGTH_SHORT).show();
        //if arabic translation button is true,
        retrieveShopList();
    }
}//end of class



