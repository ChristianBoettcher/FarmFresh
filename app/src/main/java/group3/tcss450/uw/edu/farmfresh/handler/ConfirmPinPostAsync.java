package group3.tcss450.uw.edu.farmfresh.handler;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import group3.tcss450.uw.edu.farmfresh.LoginActivity;
import group3.tcss450.uw.edu.farmfresh.util.PostParams;
import group3.tcss450.uw.edu.farmfresh.ChangePassFragment;
import group3.tcss450.uw.edu.farmfresh.LoginFragment;
import group3.tcss450.uw.edu.farmfresh.R;

import static group3.tcss450.uw.edu.farmfresh.util.Links.CONFIRM_PIN_URL;
import static group3.tcss450.uw.edu.farmfresh.util.Links.STORE_ACC_URL;
import static group3.tcss450.uw.edu.farmfresh.util.PostParams.getPostDataString;

/**
 * Thread that runs in background when user submits 6-digit pin.
 * Created by Doseon on 11/9/2017.
 */

public class ConfirmPinPostAsync extends AsyncTask<Void, Void, String> {

    /**
     * Activity passed to this class.
     */
    private LoginActivity activity;

    /**
     * Parameters passed:
     * user email, pin;
     * corresponding URL.
     */
    private PostParams params;

    /**
     * User email.
     */
    private String email;

    /**
     * User password.
     */
    private String pass;

    /**
     * User name.
     */
    private String name;
    /**
     * Check if register or change pass pin screen.
     */
    private Boolean forgot;


    //Shared Preferences for username, autologin feature.
    private SharedPreferences mpref;

    /**
     * Construct ConfirmPostHandler object.
     * Initializes:
     * @param activity LoginActivity
     * @param params parameters.
     * @param email user email.
     * @param pass user password.
     * @param name user name.
     */
    public ConfirmPinPostAsync(LoginActivity activity, PostParams params, String email,
                               String pass, String name, boolean forgot, SharedPreferences mpref) {
        this.activity = activity;
        this.params = params;
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.forgot = forgot;
        this.mpref = mpref;
    }

    /**
     * Runs in background.
     * Sends POST request to backend.
     * @param voids no params.
     * @return string response to onPostExecute.
     */
    @Override
    public String doInBackground(Void... voids) {
        String response = "";
        HttpURLConnection urlConnection = null;
        HashMap<String, String> map = params.postSet;
        try {
            URL urlObject = new URL(CONFIRM_PIN_URL);
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
        //Log.d("POST_RESPONse", response);
        return response;
    }

    /**
     * Decides what to do next depending on response.
     * Depending on response can:
     * Successfully register user;
     * Prompt with incorrect pin;
     * Prompt with problem in backend;
     * Prompt with no pin for given email.
     * @param response determines what to do next.
     */
    @Override
    protected void onPostExecute(String response) {
        // Something wrong with the network or the URL.
        activity.findViewById(R.id.pin_loading).setVisibility(View.GONE);
        activity.findViewById(R.id.pin_submit_button).setEnabled(true);
        if (response.startsWith("Unable to")) {
            Toast.makeText(activity.getApplicationContext(), response, Toast.LENGTH_LONG)
                    .show();
            return;
        } else {
            try {
                JSONObject mainObject = new JSONObject(response);
                Integer code = mainObject.getInt("code");
                EditText pin_text = (EditText) activity.findViewById(R.id.pin_edit_text);
                if (code == 300) {
                    //CORRECT PIN
                    correctPinEntered();
                } else if (code == 200) {
                    Toast.makeText(activity.getApplicationContext(),
                            "No pin was found for that username.", Toast.LENGTH_LONG).show();
                    pin_text.setError("No pin for email.");
                    return;
                } else if (code == 201) {
                    Toast.makeText(activity.getApplicationContext(),
                            "Incorrect pin, please try again.", Toast.LENGTH_LONG).show();
                    pin_text.setError("Incorrect pin.");
                    return;
                } else {
                    Toast.makeText(activity.getApplicationContext(),
                            "Back end error, please try again later.", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception ex) {
                //not JSON RETURNED
            }
        }
    }

    /**
     * Pre thread initializations of SearchFragment.
     * Sets button disabled.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.findViewById(R.id.pin_loading).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.pin_submit_button).setEnabled(false);
    }

    /**
     * Called when pin entered is correct.
     * User successfully registered and now is redirected
     * to login page. Saves user credentials.
     */
    private void correctPinEntered() {
        if (!forgot) {
            PostHandlerNoReturnAsync saveInfoTask = new PostHandlerNoReturnAsync();
            HashMap<String, String> params = new HashMap<>();
            params.put("user", email);
            params.put("pass", pass);
            params.put("name", name);
            PostParams pm = new PostParams(STORE_ACC_URL, params);
            saveInfoTask.execute(pm);

            //GO BACK TO LOGIN FRAGMENT AND OPEN TOASTER.

            Bundle args = new Bundle();
            //args.putSerializable(activity.getString(R.string.email_key), email);

            activity.saveToSharedPrefs(email, pass, 0);

            args.putString(activity.getString(R.string.SAVEDNAME), mpref.getString(activity.getString(R.string.SAVEDNAME), ""));
            args.putString(activity.getString(R.string.SAVEDPASS), mpref.getString(activity.getString(R.string.SAVEDPASS), ""));
            args.putInt(activity.getString(R.string.SAVEDAUTO), mpref.getInt(activity.getString(R.string.SAVEDAUTO), 0));
            args.putSerializable("LOGIN_MESSAGE", "You have successfully registered.");
            LoginFragment lf = new LoginFragment();
            lf.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction = activity.
                    getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragmentContainer, lf)
                    .addToBackStack(null);
            transaction.commit();
        } else {
            Bundle args = new Bundle();
            args.putSerializable(activity.getString(R.string.email_key), email);
            ChangePassFragment cpf = new ChangePassFragment();
            cpf.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction = activity.
                    getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragmentContainer, cpf)
                    .addToBackStack(null);
            transaction.commit();
        }
    }

}