package group3.tcss450.uw.edu.farmfresh.handler;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import group3.tcss450.uw.edu.farmfresh.SearchActivity;
import group3.tcss450.uw.edu.farmfresh.util.PostParams;
import group3.tcss450.uw.edu.farmfresh.R;

/**
 * Thread that logs in user to main page of the app.
 * Created by Doseo on 11/3/2017.
 */

public class LoginPostAsync extends AsyncTask<PostParams, Integer, String>{

    /**
     * LoginActivity
     */
    LoginActivity activity;

    String username;
    String pass;
    boolean auto;

    /**
     * Constructs LoginPostAsync Object.
     * Initializes:
     * @param activity LoginActivity.
     */
    public LoginPostAsync(LoginActivity activity, String username, String pass, boolean auto) {
        this.activity = activity;
        this.username = username;
        this.pass = pass;
        this.auto = auto;
    }

    /**
     * Runs in background.
     * Sends request to successfully log in user to the app.
     * @param params Parameters: URL, email, password.
     * @return response JSON String.
     */
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
            writer.write(PostParams.getPostDataString(map));

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

    /**
     * Depending on response will:
     * Log in user;
     * Prompt with an error;
     * @param response JSON string.
     */
    @Override
    protected void onPostExecute(String response) {
        activity.findViewById(R.id.login_progress).setVisibility(ProgressBar.GONE);
        activity.findViewById(R.id.login_button).setEnabled(true);
        activity.findViewById(R.id.register_button).setEnabled(true);
        activity.findViewById(R.id.forgot_button).setEnabled(true);
        // Something wrong with the network or the URL.
        if (response.startsWith("Unable to")) {
            Toast.makeText(activity.getApplicationContext(), response, Toast.LENGTH_LONG)
                    .show();
            return;
        } else {
            try {
                JSONObject mainObject = new JSONObject(response);
                String message = mainObject.getString("message");
                Integer code = mainObject.getInt("code");
                if (code == 300) {
                    //success

                    activity.saveToSqlite(username, pass, auto);

                    ((EditText)activity.findViewById(R.id.login_pass)).setText("");
                    activity.startActivity(new Intent(activity, SearchActivity.class));
                } else if (code == 200) {
                    //wrong login
                } else if (code == 201){
                    //wrong pass
                }
                Toast.makeText(activity.getApplicationContext(),
                        message, Toast.LENGTH_LONG).show();
                return;
            } catch (Exception ex) {
                //not JSON RETURNED
            }
        }
    }

    /**
     * Prepares thread to run.
     * Sets buttons to disabled.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.findViewById(R.id.login_progress).setVisibility(ProgressBar.VISIBLE);
        activity.findViewById(R.id.login_button).setEnabled(false);
        activity.findViewById(R.id.register_button).setEnabled(false);
        activity.findViewById(R.id.forgot_button).setEnabled(false);
    }

}
