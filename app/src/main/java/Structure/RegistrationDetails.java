package Structure;

import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created by Doseo on 11/3/2017.
 */

public class RegistrationDetails {
        public String response;
        public String pincode;
        public String user;
        public EditText name_text;
        public EditText email_text;
        public EditText pass_text;
        public ProgressBar load;


        public RegistrationDetails(String response, String pincode, EditText name_text,
                                   EditText email_text, EditText pass_text, ProgressBar pg) {
            this.response = response;
            this.pincode = pincode;
            this.name_text = name_text;
            this.email_text = email_text;
            this.pass_text = pass_text;
            this.user = email_text.getText().toString();
            this.load = pg;
        }
}
