package group3.tcss450.uw.edu.farmfresh.sqlite;

/**
 * Created by Doseo on 12/2/2017.
 */

import java.io.Serializable;

/**
 * Encapsulates a tuple from the Color table.
 */
public class UserEntry implements Serializable {

    private final String username;
    private final String password;
    private final boolean autologin;

    public UserEntry(String username, String password, boolean autologin) {
        this.username = username;
        this.password = password;
        this.autologin = autologin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getAutoLogin() {
        return autologin;
    }

}