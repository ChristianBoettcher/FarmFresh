package group3.tcss450.uw.edu.farmfresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.HashMap;
import java.util.regex.Pattern;

import Handler.ConfirmPinPostHandler;
import Handler.ForgotPassHandler;
import Handler.LoginRequirementsHandler;
import Handler.LoginPostHandler;
import Handler.PostHandlerNoReturn;
import Handler.RegistrationRequirementsHandler;
import Handler.SendEmailPostHandler;
import Structure.PostParams;

import static Structure.Links.CHANGE_PASS_URL;
import static Structure.Links.SEND_EMAIL_URL;
import static Structure.Links.STORE_ACC_URL;
import static Structure.Links.VERIFY_ACC_URL;

/**
 * Main Activity for Login, Registration and Forgot password fragments.
 */
public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener, PinFragment.OnFragmentInteractionListener,
    ChangePassFragment.OnFragmentInteractionListener {

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

    @Override
    public void goForgotPassword() {
        EditText login_email = (EditText) findViewById(R.id.login_email);
        if (TextUtils.isEmpty(login_email.getText())) {
            login_email.setError("You must type in a email.");
            return;
        } else {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("name", login_email.getText().toString());
            params.put("email", login_email.getText().toString());
            PostParams pm = new PostParams(SEND_EMAIL_URL, params);

            ForgotPassHandler changePass = new ForgotPassHandler(this, login_email.getText()
                    .toString(), pm);
            changePass.execute();
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

        RegistrationRequirementsHandler regHandle = new
                RegistrationRequirementsHandler(name_text, email_text, pass_text, confirm_text);
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
     */
    @Override
    public void submitPin(String email, String pass, String name, Boolean forgot) {
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
                    name, forgot);
            confirmPin.execute();
            
        }
    }

    @Override
    public void onChangePass(String email) {
        EditText new_pass = (EditText) findViewById(R.id.change_pass);
        EditText new_pass_confirm = (EditText) findViewById(R.id.change_pass_confirm);
        boolean canProceed = true;
        if (TextUtils.isEmpty(new_pass.getText())) {
            new_pass.setError("You must type in a password.");
            canProceed = false;
        }
        //Password must be at least 8 characters long.
        if (!Pattern.matches("\\S{8,}", new_pass.getText())) {
            new_pass.setError("Password must be at least 8 characters long.");
            canProceed = false;
        }
        if (TextUtils.isEmpty(new_pass_confirm.getText())) {
            new_pass_confirm.setError("You must type in a password.");
            canProceed = false;
        }

        if (canProceed && !new_pass_confirm.getText().toString().equals(
                new_pass.getText().toString())) {
            canProceed = false;
            new_pass.setError("Your password does not match.");
            new_pass_confirm.setError("Your password does not match.");
        }

        if (canProceed) {
            PostHandlerNoReturn saveInfoTask = new PostHandlerNoReturn();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user", email);
            params.put("pass", new_pass.getText().toString());
            PostParams pm = new PostParams(CHANGE_PASS_URL, params);
            saveInfoTask.execute(pm);

            //GO BACK TO LOGIN FRAGMENT AND OPEN TOASTER.

            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.email_key), email);
            args.putSerializable("LOGIN_MESSAGE", "You have successfully changed your password.");
            LoginFragment lf = new LoginFragment();
            lf.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragmentContainer, lf)
                    .addToBackStack(null);
            transaction.commit();
        }

    }
}
