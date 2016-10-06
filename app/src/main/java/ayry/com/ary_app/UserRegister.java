package ayry.com.ary_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ste on 09/03/2016.
 */
public class UserRegister  extends AppCompatActivity implements View.OnClickListener {

    Button regBtn;
    EditText userNameET, userPasswordET, userEmailET, etName;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_layout);

        etName = (EditText) findViewById(R.id.etNameReg);
        userNameET = (EditText) findViewById(R.id.etUserNameReg);
        userEmailET = (EditText) findViewById(R.id.etUserEmailReg);
        userPasswordET = (EditText) findViewById(R.id.etUserPassswordReg);
        regBtn = (Button) findViewById(R.id.RegBtn);

        regBtn.setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.RegBtn:

                //retrive data entred by user
                String name = etName.getText().toString();
                String username = userNameET.getText().toString();
                String email = userEmailET.getText().toString();
                String password = userPasswordET.getText().toString();

                User RegUser = new User(name, username, email, password);
                ObjectRequestHolder requestObj = new ObjectRequestHolder();

                requestObj.setUserApp(RegUser);

                regUser(requestObj);

                break;

            case R.id.tvLogin:
                Intent intent = new Intent(this, UserLogin.class);
                startActivity(intent);
                //Return to login page
                break;
        }
    }

    //private void regUser(User user){
    private void regUser(ObjectRequestHolder requestObj){

        DB_Sever_Request  DB_Sever_Request = new  DB_Sever_Request(this);
        DB_Sever_Request.SaveUserDataInBackground(requestObj.getUserApp(), new GetUserCallBack() {
            @Override
            public void finished(ObjectRequestHolder returnedUser) {


                    Intent intent = new Intent(UserRegister.this, UserLogin.class);
                    startActivity(intent);
                }

        });
    }
}
