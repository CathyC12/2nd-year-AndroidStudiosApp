package ayry.com.ary_app;

/**
 * Created by ste on 09/03/2016.
 */
public class User {
    String userName, name, email, password;

    public User(String name,String userName, String email, String password) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    //other way of creating user
    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
