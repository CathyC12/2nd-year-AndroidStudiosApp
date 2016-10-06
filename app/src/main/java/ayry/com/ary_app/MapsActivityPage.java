package ayry.com.ary_app;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityPage extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity_page);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       LatLng gpo = new LatLng(53.349395, -6.260391);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gpo));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gpo, 10));

        LatLng site  = new LatLng(53.350894, -6.262975);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(site, 10));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(site));
        mMap.addMarker(new MarkerOptions()
                .title("Halal Supermarket")
                .snippet("Most popular halal shop in dublin city centre located near Historic Moore street.")
                .position(site));

        LatLng site1 = new LatLng(53.341087, -6.262124);


        mMap.addMarker(new MarkerOptions()
                .title("Halal Resturant MOMO")
                .snippet("The most popular hipster Halal restuant")
                .snippet(" ever to grace dublin specialising in halal meat")
                .position(site1));
        LatLng site2 = new LatLng(53.383635, -6.398015);


        mMap.addMarker(new MarkerOptions()
                .title("Al-Mustafa Islamic Centre")
                .snippet("By the Grace of Almighty Allah,is one of the mainstream centres in ireland")
                .position(site2));


        LatLng site3 = new LatLng(53.330525, -6.264188);


        mMap.addMarker(new MarkerOptions()
                .title("Rotana Cafe Lebanese Restaurant ")
                .snippet("Rotana offers a wonderful Lebanese eating experience at an excellent price point")
                .position(site3));

        LatLng site4 = new LatLng(53.452167, -6.154823);


        mMap.addMarker(new MarkerOptions()
                .title("Kajjal")
                .snippet("Kajjal offers a diverse pakistani and indian eating experience")
                .position(site4));


        LatLng site5 = new LatLng(53.333732, -6.245748);


        mMap.addMarker(new MarkerOptions()
                .title("Keshkl")
                .snippet("Keshkl offers a diverse  Egyptian, Greek and Turkish eating experience")
                .position(site5));
        LatLng site6 = new LatLng(53.304499, -6.233727);
        mMap.addMarker(new MarkerOptions()
                .title("Islamic Cultural Centre of Ireland")
                .snippet("The Islamic Cultural Centre of Ireland is an Islamic complex, including a mosque, in Clonskeagh, Dublin, Ireland. It is funded by the al-Maktoum Foundation of Dubai and has a Sunni orientation.")
                .position(site6));

        LatLng site7 = new LatLng(53.267348, -6.135700);
        mMap.addMarker(new MarkerOptions()
                .title("Masala")
                .snippet("Masala is a place for simplicity. Good food and good service. Masala is the Hindi name of the Indian spices, and we’re good at using them in right amounts at right places. We’re Dubliners with Indian at heart and make Indian food for the lovely people of Dublin.\n")
                .position(site7));


    }
}
