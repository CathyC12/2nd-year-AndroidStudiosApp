package ayry.com.ary_app;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;


/**
 * Created by Stephen J Kearns.
 */
public class TabPageAdapter extends FragmentPagerAdapter {
    /*
           Code referance: http://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout

      */
    final int PAGE_COUNT = 2;
    private static String TAG = "checkVariable";
    private String tabTitles[] = new String[]{ "Shop List", "Newsfeed"};
    private Context context;
                                            //,Context context
    public TabPageAdapter(FragmentManager fm ) {
        super(fm);
        //this.context = context;

    }
    @Override
    public int getCount(){
        return PAGE_COUNT;
    }


    @Override
    public Fragment getItem(int position){
        Log.i(TAG, "Position" + position);



        switch (position){

            //returns the fragment to display for that page w
           /* case 0:
                return Tab_fragment1.newInstance(0,"First Fragment Map"); */
            case 0:
                return ShopsList_Fragment.newInstance(1, "Second fragment shoplist");
            case 1:
                return Newsfeed_Fragment.newInstance(2, "Third Fragment ");
            default:
                Log.i(TAG, "Using default case which is NULL" + position); //example of how to log
                return null;

        }




    }

    @Override
    public CharSequence getPageTitle(int position){
        //generates a title based on the position of the fragment

        return tabTitles[position];
    }
}