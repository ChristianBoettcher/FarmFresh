package group3.tcss450.uw.edu.farmfresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

import group3.tcss450.uw.edu.farmfresh.handler.ConfirmPinPostAsync;
import group3.tcss450.uw.edu.farmfresh.handler.ForgotPassAsync;
import group3.tcss450.uw.edu.farmfresh.handler.LoginRequirementsHandler;
import group3.tcss450.uw.edu.farmfresh.handler.LoginPostAsync;
import group3.tcss450.uw.edu.farmfresh.handler.PostHandlerNoReturnAsync;
import group3.tcss450.uw.edu.farmfresh.handler.RegistrationRequirementsHandler;
import group3.tcss450.uw.edu.farmfresh.handler.SendEmailPostAsync;
import group3.tcss450.uw.edu.farmfresh.util.PostParams;

import static group3.tcss450.uw.edu.farmfresh.util.Links.CHANGE_PASS_URL;
import static group3.tcss450.uw.edu.farmfresh.util.Links.SEND_EMAIL_URL;
import static group3.tcss450.uw.edu.farmfresh.util.Links.STORE_ACC_URL;
import static group3.tcss450.uw.edu.farmfresh.util.Links.VERIFY_ACC_URL;

/**
 * Main Activity for Login, Registration and Forgot password fragments.
 */
public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener, PinFragment.OnFragmentInteractionListener,
    ChangePassFragment.OnFragmentInteractionListener {

    private SharedPreferences mPrefs;

    /**
     *Initializes LoginActivity with LoginFragment.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                mPrefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
                String username = mPrefs.getString(getString(R.string.SAVEDNAME), "");
                String password = mPrefs.getString(getString(R.string.SAVEDPASS), "");
                Integer auto = mPrefs.getInt(getString(R.string.SAVEDAUTO), 0);

                Bundle args = new Bundle();
                args.putString(getString(R.string.SAVEDNAME), username);
                args.putString(getString(R.string.SAVEDPASS), password);
                args.putInt(getString(R.string.SAVEDAUTO), auto);
                args.putSerializable("LOGIN_MESSAGE", "");

                //Integer loggedout = getIntent().getIntExtra("SQLITE", 0);

                /*if (loggedout == 1) {
                    mPrefs.edit().putInt(getString(R.string.SAVEDAUTO), 0).apply();
                }*/

                LoginFragment lf = new LoginFragment();
                lf.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, lf)
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
        CheckBox auto = (CheckBox) findViewById(R.id.remember_CheckBox);

        LoginRequirementsHandler logHandle= new LoginRequirementsHandler(email_text, pass_text);
        if (logHandle.checkLoginErrors()) {

            Integer autoInt = 0;
            if (auto.isChecked())
                autoInt = 1;
            LoginPostAsync saveInfoTask = new LoginPostAsync(this, email_text.getText().toString(),
                    pass_text.getText().toString(), autoInt, mPrefs);
            HashMap<String, String> params = new HashMap<>();
            params.put("user", email_text.getText().toString());
            params.put("pass", pass_text.getText().toString());
            PostParams pm = new PostParams(VERIFY_ACC_URL, params);
            saveInfoTask.execute(pm);

        }
    }

    /**
     * Redirects to ChangePassFragment.
     */
    @Override
    public void goForgotPassword() {
        EditText login_email = (EditText) findViewById(R.id.login_email);
        if (TextUtils.isEmpty(login_email.getText())) {
            login_email.setError("You must type in a email.");
            return;
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("name", login_email.getText().toString());
            params.put("email", login_email.getText().toString());
            PostParams pm = new PostParams(SEND_EMAIL_URL, params);

            ForgotPassAsync changePass = new ForgotPassAsync(this, login_email.getText()
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

            HashMap<String, String> params = new HashMap<>();
            params.put("name", name_text.getText().toString());
            params.put("email", email_text.getText().toString());
            PostParams pm = new PostParams(SEND_EMAIL_URL, params);

            //SEND EMAIL ASYNC POST
            SendEmailPostAsync sendEmail = new SendEmailPostAsync(this,
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
            HashMap<String, String> params = new HashMap<>();
            params.put("user", email);
            params.put("pin", pin_text.getText().toString());
            PostParams pm = new PostParams(STORE_ACC_URL, params);

            ConfirmPinPostAsync confirmPin = new ConfirmPinPostAsync(this, pm, email, pass,
                    name, forgot, mPrefs);
            confirmPin.execute();
            
        }
    }

    /**
     * Check if password values are valid.
     * Sends POST request to change password
     * to backend.
     * @param email User email.
     */
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
            PostHandlerNoReturnAsync saveInfoTask = new PostHandlerNoReturnAsync();
            HashMap<String, String> params = new HashMap<>();
            params.put("user", email);
            params.put("pass", new_pass.getText().toString());
            PostParams pm = new PostParams(CHANGE_PASS_URL, params);
            saveInfoTask.execute(pm);
            //GO BACK TO LOGIN FRAGMENT AND OPEN TOASTER.

            saveToSharedPrefs(email, new_pass.toString(), 0);

            Bundle args = new Bundle();

            args.putString(getString(R.string.SAVEDNAME), mPrefs.getString(getString(R.string.SAVEDNAME), ""));
            args.putString(getString(R.string.SAVEDPASS), mPrefs.getString(getString(R.string.SAVEDPASS), ""));
            args.putInt(getString(R.string.SAVEDAUTO), mPrefs.getInt(getString(R.string.SAVEDAUTO), 0));
            args.putSerializable("LOGIN_MESSAGE", "You have successfully changed your password.");
            LoginFragment lf = new LoginFragment();
            lf.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragmentContainer, lf)
                    .addToBackStack(null);
            transaction.commit();
        }
    }

    public void saveToSharedPrefs(String name, String pass, Integer auto) {
        mPrefs.edit().putString(getString(R.string.SAVEDNAME), name).apply();
        mPrefs.edit().putString(getString(R.string.SAVEDPASS), pass).apply();
        mPrefs.edit().putInt(getString(R.string.SAVEDAUTO), auto).apply();
    }

}
