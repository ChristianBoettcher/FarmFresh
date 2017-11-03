package group3.tcss450.uw.edu.farmfresh;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import GMailSender.GMailSender;
import Handler.GetAsyncTaskHandler;
import Handler.LoginHandler;
import Handler.LoginPostHandler;
import Handler.RegistrationHandler;
import Handler.RegistrationPostTaskHandler;
import Structure.PostParams;
import Structure.RegistrationDetails;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener, PinFragment.OnFragmentInteractionListener {

    /*private static final String STORE_PIN_URL
            = "http://farmfresh.getenjoyment.net/add_pin.php";
    */

    private static final String STORE_ACC_URL
            = "http://farmfresh.getenjoyment.net/register.php";
    private static final String VERIFY_ACC_URL
            = "http://farmfresh.getenjoyment.net/confirm_info.php";

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
        LoginHandler logHandle= new LoginHandler(email_text, pass_text);
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
        ProgressBar loading = (ProgressBar) findViewById(R.id.register_loading);

        RegistrationHandler regHandle = new RegistrationHandler(name_text, email_text, pass_text,
                confirm_text);
        if (regHandle.checkRegistrationErrors()) {
            Integer pincode = regHandle.generatePin();

            RegistrationDetails details = new RegistrationDetails("", pincode.toString(),
                    name_text, email_text, pass_text, loading);

            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.email_key), details.email_text.getText().toString());
            args.putSerializable(getString(R.string.password_key), details.pass_text.getText().toString());
            args.putSerializable(getString(R.string.pincode_key), details.pincode.toString());

            PinFragment pf = new PinFragment();
            pf.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragmentContainer, pf)
                    .addToBackStack(null);

            GetAsyncTaskHandler checkUsername = new GetAsyncTaskHandler(this, transaction);
            checkUsername.execute(details);

            loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void submitPin(String email, String pass, String pin) {
        EditText pin_text = (EditText) findViewById(R.id.pin_edit_text);
        String curr_pin = pin_text.getText().toString();

        if (pin_text.getText().toString().length() < 6) {
            pin_text.setError("You must type in a 6 pin code.");
        } else if (!pin.equalsIgnoreCase(curr_pin)) {
            pin_text.setError("Incorrect Pin, please check again.");
        } else {
            Log.d("SUCCESS", "succesful pin entered");

            RegistrationPostTaskHandler saveInfoTask = new RegistrationPostTaskHandler();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user", email);
            params.put("pass", pass);
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
