package ayry.com.ary_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stephen Kearns on 10/03/2016.
 */
public class DB_Sever_Request {

    //creates a loading dialog for server request
    ProgressDialog progressDialog;
    public static final int Con_Timer = 1000*50;
    public static final String server_LoginUrl = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Login/login.php";
    public  static final String server_Registration = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Login/Register.php";
    public static  final String server_PullShopData = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Pull-NewsFeed/SearchEvents.php";
    private String requestToMake;
    protected String urlToUse;
    public int i = 0;
    ObjectRequestHolder shopHolder = new ObjectRequestHolder();

    //Tags for parsing json

    private static final String tagName = "shopname";
    private static final String tagDesc = "shopDesc";
    private static final String tagGeo = "geo";

    //Tags for parsing json
    private static final String tagId = "id";
    private static final String tagCatagory = "catagory";
    private static final String tagtitle = "title";
    private static final String tagLocation = "location";
    private static final String tagpTime = "eventTime";
    private static final String tagDate = "eventDate";
    private static final String tagLat = "latitude";
    private static final String tagLong = "longitude";

    // instaniates the progressDialog by getting the context of the activity which uses it,
    // as Progress dialogs need a class which extends the activity class
    public DB_Sever_Request(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing details");
        progressDialog.setMessage("Please wait....");
    }


    public void SaveUserDataInBackground(User user,GetUserCallBack userCallBack){
      progressDialog.show();

      //initiate the async task
      //  new storeUserDataAsync(user, userCallBack).execute();
        requestToMake = "register";
        new ServerRequestDataAsync(user,userCallBack).execute();


    }

    public void RequestUserDataInBackground(User user,GetUserCallBack userCallBack){
     progressDialog.show();
    // new retriveUserDataAsync(user,userCallBack).execute();
        requestToMake = "login";
        new ServerRequestDataAsync(user,userCallBack).execute();

    }

    public void SearchEvents(EventsModel event, GetUserCallBack userCallBack){
        progressDialog.show();
        //new instances of server request to pulling

        requestToMake = "search";
        new ServerRequestDataAsync(event,userCallBack).execute();
    }

    public class ServerRequestDataAsync extends AsyncTask<Void,Void,ObjectRequestHolder>{
        ObjectRequestHolder requesrObj;
        GetUserCallBack callBackUser;
        User user;
        Shop_items shop;
        EventsModel event;
        public ServerRequestDataAsync(User user, GetUserCallBack callBackUser){
            this.callBackUser = callBackUser;
            this.user = user;
        }

        public ServerRequestDataAsync(EventsModel event, GetUserCallBack callBackUser){
            this.callBackUser = callBackUser;
            this.event = event;


        }



        @Override
        protected ObjectRequestHolder doInBackground(Void... params) {
            Map<String, String> dbLoginCred = new HashMap<String, String>();

            //populates the map with data dependent on which request is to be made
            switch(requestToMake){
                case "login":
                    dbLoginCred.put("userName", user.userName);
                    dbLoginCred.put("password", user.password);
                    urlToUse = server_LoginUrl;
                    break;
                case "register":
                    dbLoginCred.put("name", user.name);
                    dbLoginCred.put("userName", user.userName);
                    dbLoginCred.put("email", user.email);
                    dbLoginCred.put("password", user.password);
                    urlToUse = server_Registration;
                    break;
                case "search":
                    dbLoginCred.put("catagory", event.getCatagory());
                    dbLoginCred.put("date", event.getDate());
                    dbLoginCred.put("location", event.getLocatiion());
                   // dbLoginCred.put("countern",String.valueOf(i));
                    urlToUse = server_PullShopData;

                    break;


            }



            User userReturned = null;

            try {
                URL url = new URL(urlToUse);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(Con_Timer);
                connection.setReadTimeout(Con_Timer);
                connection.setConnectTimeout(Con_Timer);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);


                OutputStream oStream = connection.getOutputStream();
                BufferedWriter bWriter = new BufferedWriter(
                        new OutputStreamWriter(oStream, "UTF-8")
                );

                bWriter.write(getPostDataString(dbLoginCred));
                bWriter.flush();
                bWriter.close();

                oStream.close();

                int code = connection.getResponseCode();
                Log.d("code", Integer.toString(code));

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseReader.close();


                String response = stringBuilder.toString();
               // Log.d("response", response);

                if (requestToMake.equals("login")) {
                    //returns json as a object
                    JSONObject jsonResponse = new JSONObject(response);
                    //response from sever is returning null so crash at this point
                    Log.d("length", Integer.toString(jsonResponse.length()));
                    if (jsonResponse.length() == 0) {
                        Log.i("User is null", null);
                        userReturned = null;
                    } else {


                        // String name = jsonResponse.getString("name");
                        String name = jsonResponse.getString("name");
                        String username = jsonResponse.getString("userName");
                        String email = jsonResponse.getString("email");
                        String password = jsonResponse.getString("password");

                        //if it makes it to this stage, which means the login credintails are correct
                        // the username, and password sent are assigned to a new user and the user will be logged in

                        Log.i("usrnme,passwrd returned", user.userName + user.password);
                        userReturned = new User(name,username, email,password);
                        //Log.d("UserReturned",userReturned.name);

                    }
                }else if(requestToMake .equals("shopData")){

                    JSONArray jsonObj = new JSONArray(response);


                //    JSONObject shoplst = jsonObj.getJSONObject();

                    for(int i = 0; i < jsonObj.length(); i++){
                        JSONObject eventObj = jsonObj.getJSONObject(i);;

                        int id = Integer.parseInt(eventObj .getString(tagId));
                        String eventCat = eventObj .getString(tagCatagory);
                        String eventTitle = eventObj .getString(tagtitle);
                        String eventLocation = eventObj.getString(tagLocation);
                        String eventTime = eventObj.getString(tagpTime);
                        String eventDate  = eventObj.getString(tagDate);
                        Double eventLat = Double.parseDouble(eventObj.getString(tagLat));
                        Double eventLong = Double.parseDouble(eventObj.getString(tagLong));
                        Log.i("shopid", "id"+id);
                        Log.i("shopname", eventCat);
                        Log.i("shopdesc", eventTitle);
                        Log.i("shopgeo", eventLocation);

                        EventsModel newEvent = new EventsModel(id,eventCat,eventTitle,eventLocation,eventTime,eventDate, eventLat,eventLong);
                        requesrObj.addShop(shop);


                    }




                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(requestToMake.equals("login")){
                requesrObj = new ObjectRequestHolder();
                requesrObj.setUserApp(userReturned);
                return requesrObj;
            }else if(requestToMake.equals("search")){
                return requesrObj;
            }else{
                return null;
            }


        }





        private String getPostDataString(Map<String, String> dbLoginCred)throws UnsupportedEncodingException{
            StringBuilder resultSB = new StringBuilder();
            boolean first = true;

            for(Map.Entry<String,String> entry : dbLoginCred.entrySet()){
                if (first){
                    first = false;
                }else {
                    resultSB.append("&");
                }

                resultSB.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                resultSB.append("=");
                resultSB.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
            }

         //   Log.i("Value", resultSB.toString());
            return resultSB.toString();


        }


        @Override
        protected void onPostExecute(ObjectRequestHolder requesrObj) {
            progressDialog.dismiss();
            switch(requestToMake) {
                case "login":
                    callBackUser.finished(requesrObj);
                    super.onPostExecute(requesrObj);
                    break;
                case "register":
                    callBackUser.finished(null);
                    super.onPostExecute(null);
                    break;
                case "search":
                    callBackUser.finished(requesrObj);
                    super.onPostExecute(requesrObj);
                    break;


            }
        }
    }

}

