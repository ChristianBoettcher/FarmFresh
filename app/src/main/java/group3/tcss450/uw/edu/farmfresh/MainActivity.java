package group3.tcss450.uw.edu.farmfresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.HashMap;

import Handler.LoginRequirementsHandler;
import Handler.LoginPostHandler;
import Handler.RegistrationRequirementsHandler;
import Handler.PostHandlerNoReturn;
import Handler.SendEmailPostHandler;
import Structure.PostParams;

import static Structure.Links.SEND_EMAIL_URL;
import static Structure.Links.STORE_ACC_URL;
import static Structure.Links.VERIFY_ACC_URL;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener, PinFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    public void goRegister() {
        RegisterFragment rf = new RegisterFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragmentContainer, rf)
                .addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void loginManager() {
        //Handles the login.
        EditText email_text = (EditText) findViewById(R.id.login_email);
        EditText pass_text = (EditText) findViewById(R.id.login_pass);

        LoginRequirementsHandler logHandle= new LoginRequirementsHandler(email_text, pass_text);
        if (logHandle.checkLoginErrors()) {
            LoginPostHandler saveInfoTask = new LoginPostHandler(this);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user", email_text.getText().toString());
            params.put("pass", pass_text.getText().toString());
            PostParams pm = new PostParams(VERIFY_ACC_URL, params);
            saveInfoTask.execute(pm);

        }
    }

    @Override
    public void goPin() {
        EditText name_text = (EditText) findViewById(R.id.registration_name);
        EditText email_text = (EditText) findViewById(R.id.registration_email);
        EditText pass_text = (EditText) findViewById(R.id.registration_pass);
        EditText confirm_text = (EditText) findViewById(R.id.registration_confirm);

        RegistrationRequirementsHandler regHandle = new RegistrationRequirementsHandler(name_text, email_text, pass_text,
                confirm_text);
        if (regHandle.checkRegistrationErrors()) {

            //SEND EMAIL ASYNC POST
            SendEmailPostHandler sendEmail = new SendEmailPostHandler(this,
                    name_text.getText().toString());
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("name", name_text.getText().toString());
            params.put("email", email_text.getText().toString());
            PostParams pm = new PostParams(SEND_EMAIL_URL, params);
            sendEmail.execute(pm);
        }
    }

    @Override
    public void submitPin(String email, String pass, String name, String pin) {
        EditText pin_text = (EditText) findViewById(R.id.pin_edit_text);
        String curr_pin = pin_text.getText().toString();

        if (pin_text.getText().toString().length() < 6) {
            pin_text.setError("You must type in a 6 pin code.");
        } else if (!pin.equalsIgnoreCase(curr_pin)) {
            pin_text.setError("Incorrect Pin, please check again.");
        } else {
            Log.d("SUCCESS", "succesful pin entered");

            PostHandlerNoReturn saveInfoTask = new PostHandlerNoReturn();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user", email);
            params.put("pass", pass);
            params.put("name", name);
            PostParams pm = new PostParams(STORE_ACC_URL, params);
            saveInfoTask.execute(pm);

            //GO BACK TO LOGIN FRAGMENT AND OPEN TOASTER.

            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.email_key), email);
            LoginFragment lf = new LoginFragment();
            lf.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragmentContainer, lf)
                    .addToBackStack(null);
            transaction.commit();

        }
    }
}
