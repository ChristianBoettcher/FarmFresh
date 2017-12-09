package group3.tcss450.uw.edu.farmfresh.handler;

import android.os.AsyncTask;
import android.os.Bundle;
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
import group3.tcss450.uw.edu.farmfresh.PinFragment;
import group3.tcss450.uw.edu.farmfresh.R;
import group3.tcss450.uw.edu.farmfresh.util.PostParams;

import static group3.tcss450.uw.edu.farmfresh.util.Links.CHECK_USER_URL;
import static group3.tcss450.uw.edu.farmfresh.util.PostParams.getPostDataString;

/**
 * Thread that checks if email is valid
 * and emails pin code to user's email to reset password.
 * Created by Doseo on 11/10/2017.
 */

public class ForgotPassAsync extends AsyncTask<Void, Void, String> {

    /**
     * LoginActivity.
     */
    LoginActivity activity;

    /**
     * User email.
     */
    String user_email;

    /**
     * PostParams Object.
     */
    PostParams params;

    /**
     * Constructs ForgotPassAsync Object.
     * @param activity LoginActivity
     * @param user_email User email.
     * @param params PostParams Object.
     */
        public ForgotPassAsync(LoginActivity activity, String user_email, PostParams params) {
            this.activity = activity;
            this.user_email = user_email;
            this.params = params;
        }

    /**
     * Runs in background.
     * Requests and returns JSON Object string.
     * @param string void
     * @return JSON String response.
     */
    @Override
        protected String doInBackground(Void... string) {
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(CHECK_USER_URL + user_email);
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
            response = response;
            //Log.d("POST_RESPONse", response);
            return response;
        }

    /**
     * Depending on response:
     * Prompt that username doesn't exist;
     * or resets password.
     * @param response
     */
    @Override
        protected void onPostExecute(String response) {
            // Something wrong with the network or the URL.
            if (response.startsWith("Unable to")) {
                Toast.makeText(activity.getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();
                return;
            } else {
                try {
                    JSONObject mainObject = new JSONObject(response);
                    Integer code = mainObject.getInt("code");
                    EditText email_text = (EditText) activity.findViewById(R.id.login_email);
                    if (code == 300) {
                        //activity.findViewById(R.id.register_loading).setVisibility(View.GONE);
                        //activity.findViewById(R.id.register_submit).setEnabled(true);
                        Toast.makeText(activity.getApplicationContext(),
                                "Username does not exist.", Toast.LENGTH_LONG).show();
                        email_text.setError("Email does not exist.");
                        return;
                    } else if (code == 200){
                        //Log.d("forgot pass handler: ", "user exists");
                         MoveToPinHandler goToPinTask =
                                new MoveToPinHandler();
                        goToPinTask.execute(params);
                    }
                } catch (Exception ex) {
                    //not JSON RETURNED
                }
            }
        }

    /**
     * Thread that resets the password.
     */
    public class MoveToPinHandler extends AsyncTask<PostParams, Void, String> {

        /**
         * Requests and returns JSON object string.
         * @param params PostParams object.
         * @return JSON string response.
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
         * Resets Password.
         * @param response JSON String.
         */
        @Override
            protected void onPostExecute(String response) {
                //activity.findViewById(R.id.register_loading).setVisibility(View.GONE);
                //activity.findViewById(R.id.register_submit).setEnabled(true);
                // Something wrong with the network or the URL.
                if (response.startsWith("Unable to")) {
                    Toast.makeText(activity.getApplicationContext(), response, Toast.LENGTH_LONG)
                            .show();
                    return;
                } else {
                    try {
                        JSONObject mainObject = new JSONObject(response);
                        Integer code = mainObject.getInt("code");
                        String pin = mainObject.getString("message");
                        EditText email_text = (EditText) activity.findViewById(R.id.login_email);

                        if (code == 300) {
                            //Log.d("TEST PIN", pin);


                            Bundle args = new Bundle();
                            args.putSerializable(activity.getString(R.string.email_key), email_text.getText().toString());
                            args.putSerializable("CHANGE_PASS_BOOLEAN", true);


                            PinFragment pf = new PinFragment();
                            pf.setArguments(args);
                            android.support.v4.app.FragmentTransaction transaction =
                                    activity.getSupportFragmentManager()
                                            .beginTransaction().replace(R.id.fragmentContainer, pf)
                                            .addToBackStack(null);
                            transaction.commit();

                        } else {
                            Toast.makeText(activity.getApplicationContext(),
                                    "Error occurred in the back end. Please Try Again.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (Exception ex) {
                        //not JSON RETURNED
                    }
                }
            }
        }
}
