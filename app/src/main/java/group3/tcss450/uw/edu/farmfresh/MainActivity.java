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


public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener, PinFragment.OnFragmentInteractionListener {

    /*private static final String STORE_PIN_URL
            = "http://farmfresh.getenjoyment.net/add_pin.php";
    */

    private static final String STORE_ACC_URL
            = "http://farmfresh.getenjoyment.net/register.php";

    private static final String CHECK_USER_URL
            = "http://farmfresh.getenjoyment.net/check_user.php?user=";

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

    private static class RegistrationDetails {
        String response;
        String pincode;
        String user;
        EditText name_text;
        EditText email_text;
        EditText pass_text;
        ProgressBar load;


        RegistrationDetails(String response, String pincode, EditText name_text,
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

    private static class PostParams {
        String url;
        HashMap<String, String> postSet;

        PostParams(String url, HashMap<String, String> map) {
            this.url = url;
            this.postSet = map;
        }
    }

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
    public void goPin() {
        EditText name_text = (EditText) findViewById(R.id.registration_name);
        EditText email_text = (EditText) findViewById(R.id.registration_email);
        EditText pass_text = (EditText) findViewById(R.id.registration_pass);
        EditText confirm_text = (EditText) findViewById(R.id.registration_confirm);
        ProgressBar loading = (ProgressBar) findViewById(R.id.register_loading);

        if (checkRegistrationErrors(name_text, email_text, pass_text, confirm_text)) {
            Integer pincode = generatePin();

            RegistrationDetails details = new RegistrationDetails("", pincode.toString(),
                    name_text, email_text, pass_text, loading);
            GetTask checkUserDupe = new GetTask();
            checkUserDupe.execute(details);

            loading.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkRegistrationErrors(EditText name_text, EditText email_text,
                                          EditText pass_text, EditText confirm_text) {
        boolean canProceed = true;


        // Check if username already exists WITH GET.
        // Check password length username length etc.
        // Check if it is an actual email with @
        if (TextUtils.isEmpty(name_text.getText())) {
            name_text.setError("You must type in a name.");
            canProceed = false;
        }
        if (TextUtils.isEmpty(email_text.getText())) {
            email_text.setError("You must type in a email.");
            canProceed = false;
        }
        //Email must be valid.
        if (!Pattern.matches(EMAIL_REGEX, email_text.getText())) {
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
        if (TextUtils.isEmpty(confirm_text.getText())) {
            confirm_text.setError("You must type in a password.");
            canProceed = false;
        }

        if (canProceed && !confirm_text.getText().toString().equals(
                pass_text.getText().toString())) {
            canProceed = false;
            pass_text.setError("Your password does not match.");
            confirm_text.setError("Your password does not match.");
        }
        return canProceed;
    }

    private void sendEmail(final String pincode, final String email) {
        Log.d("EMAIL", "SENT EMAIL");
        final GMailSender sender = new GMailSender("farmfresh5000@gmail.com", "TCSS450GROUP3");
        new AsyncTask<String, Void, Void>() {
            @Override
            public Void doInBackground(String... strings) {
                try {
                    sender.sendMail("Farm Fresh Registration!",
                            "This is your pincode to register: " + pincode,
                            "FarmFresh.no-reply@gmail.com",
                            email);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
                return null;
            }
        }.execute();
    }

    private int generatePin() {
        Random rand = new Random();
        return 100000 + (int) (rand.nextFloat() * 900000);
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

            PostTask saveInfoTask = new PostTask();
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


    private class GetTask extends AsyncTask<RegistrationDetails, Void, RegistrationDetails> {
        @Override
        protected RegistrationDetails doInBackground(RegistrationDetails... details) {
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(CHECK_USER_URL + details[0].user);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            details[0].response = response;
            Log.d("POST_RESPONse", response);
            return details[0];
        }

        @Override
        protected void onPostExecute(RegistrationDetails details) {
            details.load.setVisibility(View.GONE);
            // Something wrong with the network or the URL.
            if (details.response.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), details.response, Toast.LENGTH_LONG)
                        .show();
                return;
            } else {
                try {
                    JSONObject mainObject = new JSONObject(details.response);
                    Integer code = mainObject.getInt("code");
                    if (code == 200) {
                        Toast.makeText(getApplicationContext(), "Username already exists.", Toast.LENGTH_LONG)
                                .show();
                        details.email_text.setError("Email already exists.");
                        return;
                    } else {
                        sendEmail(details.pincode, details.user);

                        Bundle args = new Bundle();
                        args.putSerializable(getString(R.string.email_key), details.email_text.getText().toString());
                        args.putSerializable(getString(R.string.password_key), details.pass_text.getText().toString());
                        args.putSerializable(getString(R.string.pincode_key), details.pincode.toString());

                        PinFragment pf = new PinFragment();
                        pf.setArguments(args);
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                                .beginTransaction().replace(R.id.fragmentContainer, pf)
                                .addToBackStack(null);
                        transaction.commit();
                    }
                } catch (Exception ex) {
                    //not JSON RETURNED
                }
            }
        }
    }


    private class PostTask extends AsyncTask<PostParams, Void, String> {
        @Override
        public String doInBackground(PostParams... params) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = params[0].url;
            HashMap<String, String> map = params[0].postSet;
            try {
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(map));

                writer.flush();
                writer.close();
                os.close();

                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            Log.d("POST_RESPONse", response);
            return response;
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


}
