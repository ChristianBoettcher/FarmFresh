package Handler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import Structure.PostParams;
import group3.tcss450.uw.edu.farmfresh.LoginFragment;
import group3.tcss450.uw.edu.farmfresh.MainActivity;
import group3.tcss450.uw.edu.farmfresh.R;

import static Structure.Links.CONFIRM_PIN_URL;
import static Structure.Links.STORE_ACC_URL;
import static Structure.PostParams.getPostDataString;

/**
 * Created by Doseon on 11/9/2017.
 */

public class ConfirmPinPostHandler extends AsyncTask<Void, Void, String> {

    MainActivity activity;
    PostParams params;
    String email;
    String pass;
    String name;

    public ConfirmPinPostHandler(MainActivity activity, PostParams params, String email,
                                 String pass, String name) {
        this.activity = activity;
        this.params = params;
        this.email = email;
        this.pass = pass;
        this.name = name;
    }

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
        Log.d("POST_RESPONse", response);
        return response;
    }

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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.findViewById(R.id.pin_loading).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.pin_submit_button).setEnabled(false);
    }

    private void correctPinEntered() {
        PostHandlerNoReturn saveInfoTask = new PostHandlerNoReturn();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user", email);
        params.put("pass", pass);
        params.put("name", name);
        PostParams pm = new PostParams(STORE_ACC_URL, params);
        saveInfoTask.execute(pm);

        //GO BACK TO LOGIN FRAGMENT AND OPEN TOASTER.

        Bundle args = new Bundle();
        args.putSerializable(activity.getString(R.string.email_key), email);
        LoginFragment lf = new LoginFragment();
        lf.setArguments(args);
        android.support.v4.app.FragmentTransaction transaction = activity.
                getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragmentContainer, lf)
                .addToBackStack(null);
        transaction.commit();
    }

}