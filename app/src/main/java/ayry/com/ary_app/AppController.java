package ayry.com.ary_app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Stephen Kearns 13/04/2016.
 *
 * Code reference:  http://www.androidhive.info/2014/09/android-json-parsing-using-volley/
 */
public class AppController extends Application {
        public static final String TAG = AppController.class.getSimpleName();
        private static AppController mInstance;
        private RequestQueue mRequestQueue;

        private static Context mContext;


        @Override
        public void onCreate() {
            super.onCreate();
            mInstance = this;
        }





        public static synchronized AppController getInstance() {
            return mInstance;
        }

        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            return mRequestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req, String tag) {
            req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
            getRequestQueue().add(req);
        }

        public <T> void addToRequestQueue(Request<T> req) {
            req.setTag(TAG);
            getRequestQueue().add(req);
        }



        public void cancelPendingRequests(Object tag) {
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(tag);
            }
        }
}
