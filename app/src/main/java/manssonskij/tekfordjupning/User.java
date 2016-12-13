package manssonskij.tekfordjupning;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Anton on 2016-12-12.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}