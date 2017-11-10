package group3.tcss450.uw.edu.farmfresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.HashMap;

import Handler.ConfirmPinPostHandler;
import Handler.LoginRequirementsHandler;
import Handler.LoginPostHandler;
import Handler.RegistrationRequirementsHandler;
import Handler.PostHandlerNoReturn;
import Handler.SendEmailPostHandler;
import Structure.PostParams;

import static Structure.Links.SEND_EMAIL_URL;
import static Structure.Links.STORE_ACC_URL;
import static Structure.Links.VERIFY_ACC_URL;

/**
 * Main Activity for Login, Registration and Forgot password fragments.
 */
public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener, PinFragment.OnFragmentInteractionListener {

    /**
     *Initializes MainActivity with LoginFragment.
     */
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

    /**
     * Redirects to Registration page.
     */
    @Override
    public void goRegister() {
        RegisterFragment rf = new RegisterFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragmentContainer, rf)
                .addToBackStack(null);
        transaction.commit();

    }

    /**
     * Checks user credentials and logs in.
     * If user is registered and logs in with valid account
     * then user will be redirected to main page of the app.
     */
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

    /**
     * Checks user entered information during registration for validity.
     * If valid -> Redirects to pinFragment.
     */
    @Override
    public void goPin() {
        EditText name_text = (EditText) findViewById(R.id.registration_name);
        EditText email_text = (EditText) findViewById(R.id.registration_email);
        EditText pass_text = (EditText) findViewById(R.id.registration_pass);
        EditText confirm_text = (EditText) findViewById(R.id.registration_confirm);

        RegistrationRequirementsHandler regHandle = new RegistrationRequirementsHandler(name_text, email_text, pass_text,
                confirm_text);
        if (regHandle.checkRegistrationErrors()) {

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("name", name_text.getText().toString());
            params.put("email", email_text.getText().toString());
            PostParams pm = new PostParams(SEND_EMAIL_URL, params);

            //SEND EMAIL ASYNC POST
            SendEmailPostHandler sendEmail = new SendEmailPostHandler(this,
                    email_text.getText().toString(), pm);
            sendEmail.execute();
        }
    }


    /*
        HANDLE THIS TO PHP BACKEND, STORE INTO
        DATABASE REGISTRATION : username, pin.

     */

    /**
     * Submit pin button handler.
     * Checks pin for validity and if valid then
     * redirects to LoginFragment.
     * @param email user email.
     * @param pass user password.
     * @param name user name.
     * @param pin user pin code.
     */
    @Override
    public void submitPin(String email, String pass, String name) {
        EditText pin_text = (EditText) findViewById(R.id.pin_edit_text);

        if (pin_text.getText().toString().length() < 6) {
            pin_text.setError("You must type in a 6 pin code.");
        } else {
            //CHECK IF PIN IS CORRECT THROUGH ASYNC.

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user", email);
            params.put("pin", pin_text.getText().toString());
            PostParams pm = new PostParams(STORE_ACC_URL, params);

            ConfirmPinPostHandler confirmPin = new ConfirmPinPostHandler(this, pm, email, pass,
                    name);
            confirmPin.execute();
            
        }
    }
}
