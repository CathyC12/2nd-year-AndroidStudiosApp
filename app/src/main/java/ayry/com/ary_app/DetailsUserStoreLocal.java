package ayry.com.ary_app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Stephen Kearns on 09/03/2016.
 */
public class DetailsUserStoreLocal {

    public static final String SP_NAME = "UserAccountDetails";
    SharedPreferences userLocalDB; //used so data can be stored locally
    static User user;

    public DetailsUserStoreLocal(Context context){
        userLocalDB = context.getSharedPreferences(SP_NAME,0);
    }

    //stores users details locally
    public void storeUserDetails(User user){
        this.user = user;
        SharedPreferences.Editor spEditor = userLocalDB.edit();

        spEditor.putString("name", user.name);
        spEditor.putString("userName", user.userName);
        spEditor.putString("email", user.email);
        spEditor.putString("password", user.password);
        spEditor.commit();
    }

    //checks if users is logged in i,e true of if not i,e false
    public User UserLoggedIn(){
        String name = userLocalDB.getString("name", "");
        String userName = userLocalDB.getString("userName", "");
        String email = userLocalDB.getString("email", "");
        String password = userLocalDB.getString("password", "");

        user = new User(name,userName, email,password);
        return user;
    }

    //set user who is logged in
    public boolean getLoggedIn(){
        if(userLocalDB.getBoolean("LoggedIn", false)== true){
            return true;
        }else{
            return false;
        }
    }

    public void setUserLoggedIn(boolean isLogged){
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putBoolean("LoggedIn",isLogged);
        spEditor.commit();
    }


    //clear cached user data when loggin out
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.clear();
        spEditor.commit();
    }


}

