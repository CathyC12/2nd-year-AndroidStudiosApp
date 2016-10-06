package ayry.com.ary_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Stephen Kearns on 09/03/2016.
 */
public class UserLogin extends AppCompatActivity implements View.OnClickListener {
//how is it going
    Button LoginBtn;
    EditText userNameET, userPasswordET;
    DetailsUserStoreLocal DetailsUserStoreLocal;
    TextView tvRegister;

    public void onCreate(Bundle  savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        userNameET = (EditText) findViewById(R.id.userName);
        userPasswordET = (EditText) findViewById(R.id.userPassword);
        LoginBtn = (Button) findViewById(R.id.loginBtn);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(this);

        LoginBtn.setOnClickListener(this);
        DetailsUserStoreLocal = new DetailsUserStoreLocal(this);

    }

    public void onClick(View v){
         switch(v.getId()){
             case R.id.loginBtn:
                 //set up error checks
                 if(userNameET.getText().toString() == ""){
                    // display message above user name field
                 }else if(userPasswordET.getText().toString() ==""){
                     //display message above password field
                 }else {
                     String userName = userNameET.getText().toString();
                     String password = userPasswordET.getText().toString();

                     User user = new User(userName, password);
                     ObjectRequestHolder requestObj = new ObjectRequestHolder();

                     requestObj.setUserApp(user);


                     //checker, for variable being sent to server request
                     Log.i("Data being sent autent", user.getUserName() + " " + user.getPassword());

                     //checks to see if the user creds correspond to a user store in the database
                     authenticate(requestObj);

                     //do on click listner here

                 }

                 break;
             case R.id.tvRegister:
                 //starts the register activity
                 Log.i("make to reg case", "working");
                 Intent intent = new Intent(this,UserRegister.class);
                 startActivity(intent);
                 break;
         }
    }

    /*
        starts a request to the server to check if the enter user details match a user stored on the server
        if not a alert is shown incorrect details, if the cred match then the user is logged in
    */

   /* public void  authenticate(User user){
        DB_Sever_Request dbRequest = new DB_Sever_Request(this);
        dbRequest.RequestUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void finished(User returnedUser) {
                if (returnedUser == null){
                    showErrorMsg();
                }else{
                    LogUserIn(returnedUser);
                }
            }
        });
    }*/

    public void  authenticate(ObjectRequestHolder requestObj){
        DB_Sever_Request dbRequest = new DB_Sever_Request(this);
        dbRequest.RequestUserDataInBackground(requestObj.getUserApp(), new GetUserCallBack() {
            @Override
            public void finished(ObjectRequestHolder obj) {
                User returnedUser = obj.getUserApp();
                if (returnedUser == null){
                    showErrorMsg();
                }else{
                    LogUserIn(returnedUser);
                }
            }
        });
    }

    public void showErrorMsg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserLogin.this);
        builder.setMessage("Incorrect Details");
        builder.setPositiveButton("OK",null);
        builder.show();
    }

    public  void LogUserIn(User userReturned){
        if(userReturned == null){
            Log.i("Object returned", "Is null");
        }else {
            DetailsUserStoreLocal.storeUserDetails(userReturned);
            DetailsUserStoreLocal.setUserLoggedIn(true);

            Log.i("object returned name:", userReturned.userName);

            //start the main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
