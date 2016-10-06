package ayry.com.ary_app;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Stephen J Kearns on 15/02/2016.
 */

public class ShopsList_Fragment extends Fragment implements  View.OnClickListener {

     /*
          Code referance: http://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout

          For infromation on RecyclerView and implmentation
          Code referance: https://guides.codepath.com/android/using-the-recyclerview#using-the-recyclerview
     */

    protected RecyclerView.LayoutManager mLayoutManger;
    private ObjectRequestHolder objWrapper;
    private Button mTranlateBtn;
    //for test purposes
    private static String TAG = "checkUser data";

    Context context = getActivity();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //if arguments need to be returned
        int someInt = getArguments().getInt("someInt",1);
        String someTitle = getArguments().getString("someTitle","");

        //what ever you want to do with arguments when returned....


         objWrapper = new ObjectRequestHolder();
        //retrive the data from the database





        //Trying to get a referance to the button in the main activity as to be able to attach an event listner to it


    }

    // new Instance constructor for creating fragment with arguments
    public static ShopsList_Fragment newInstance(int page, String title){
        //create new istance of the Tab_fragment1 class
        ShopsList_Fragment shopsList_fragment = new ShopsList_Fragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 1);
        args.putString("someTitle", title);
        shopsList_fragment.setArguments(args);



        return shopsList_fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootview = inflater.inflate(R.layout.shoplist_layout, container, false);

        RecyclerView rvShops = (RecyclerView) rootview.findViewById(R.id.rvshoplist);

        //Create the data source and inflate the populated list view
        ArrayList<Shop_items> listOfSops = objWrapper.getShopList();

        for(int i = 0; i < listOfSops.size(); i ++){
            //Error check - data is being retrived correctly
            Shop_items shop = listOfSops.get(i);
            Log.i(TAG, shop.getTitle() + ", " + shop.getDesc());
        }



        View rootview2 = inflater.inflate(R.layout.activity_main, container, false);

        //Set and click event on translator button to translate the text
        mTranlateBtn = (Button) rootview2.findViewById(R.id.translationBtn);

        mTranlateBtn.setOnClickListener(this);


        //Create the adapter to convert array to view
        //ShoplistAdapter adapter = new ShoplistAdapter();



        /* Error bring caused here  java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first. */

        //LinearLayoutManager is used here, this will lays out elements in a similar fashion
       // mLayoutManger = new LinearLayoutManager(getActivity());
        rvShops.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        //rvShops.setAdapter(adapter);

        //Sets the layout manager to position the items
        //rvShops.setLayoutManager(mLayoutManger);



        return rootview;
    }





    public void dataSource(ObjectRequestHolder requestObj){
        //sets the reference to the object to the object in this class for acceptability between
        this.objWrapper = requestObj;

    }

    @Override
    public void onClick(View v) {
        Toast toast = Toast.makeText(context,"Click working" ,Toast.LENGTH_SHORT);
        toast.show();

        Log.i("Click event","Working");


    }


/* ArrayAdapter with list view method
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.shoplist_layout, container, false);

        //Create the data source and inflate the populated list view
        ArrayList<Shop_items> listOfSops = Shop_items.getShopItems();

        //Create the adapter to convert array to view
        ShoplistAdapter adapter = new ShoplistAdapter(getActivity(),listOfSops);

        ListView listView = (ListView) view.findViewById(R.id.shoplist);
        listView.setAdapter(adapter);
        return view;
    }


   /* public void populateList(){
        //Create the data source and inflate the populated list view
        ArrayList<Shop_items> listOfSops = new ArrayList<Shop_items>();

        //Create the adapter to convert array to view
        ShoplistAdapter adapter = new ShoplistAdapter(getActivity(),listOfSops);

        ListView listView = (ListView) view.findViewById(R.id.shoplist);
        listView.setAdapter(adapter);
    } */

}