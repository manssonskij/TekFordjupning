package manssonskij.tekfordjupning.Objects;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton on 2016-12-12.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String uid;
    //public Map<String, String> contacts = new HashMap<>();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }


    public String toString() {
        return username;
    }
}