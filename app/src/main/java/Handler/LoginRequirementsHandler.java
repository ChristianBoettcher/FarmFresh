package Handler;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Doseo on 11/3/2017.
 */

public class LoginRequirementsHandler {

    private static final String EMAIL_REGEX
            = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
            "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
            "|\"(?:[\\x01-x08\\x0b\\x0c\\x0e-\\x1f" +
            "\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-" +
            "\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9]" +
            "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*" +
            "[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9]" +
            "[0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]" +
            "?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-x08\\x0b\\x0c\\x0e-\\" +
            "x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private EditText email_text;
    private EditText pass_text;

    public LoginRequirementsHandler(EditText email_text, EditText pass_text) {
        this.email_text = email_text;
        this.pass_text = pass_text;
    }

    public boolean checkLoginErrors() {
        boolean canProceed = true;

        if (TextUtils.isEmpty(email_text.getText())) {
            email_text.setError("You must type in a email.");
            canProceed = false;
        }
        //Email must be valid.
        if (!Pattern.matches(EMAIL_REGEX, email_text.getText().toString().toLowerCase())) {
            email_text.setError("Email must be valid.");
            canProceed = false;
        }
        if (TextUtils.isEmpty(pass_text.getText())) {
            pass_text.setError("You must type in a password.");
            canProceed = false;
        }
        //Password must be at least 8 characters long.
        if (!Pattern.matches("\\S{8,}", pass_text.getText())) {
            pass_text.setError("Password must be at least 8 characters long.");
            canProceed = false;
        }

        return canProceed;
    }

}
