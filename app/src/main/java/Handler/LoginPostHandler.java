package Handler;

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

import Structure.PostParams;
import group3.tcss450.uw.edu.farmfresh.Main2Activity;
import group3.tcss450.uw.edu.farmfresh.MainActivity;
import group3.tcss450.uw.edu.farmfresh.R;

/**
 * Created by Doseo on 11/3/2017.
 */

public class LoginPostHandler extends AsyncTask<PostParams, Integer, String>{


    MainActivity activity;
    public LoginPostHandler(MainActivity activity) {
        this.activity = activity;
    }

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
                    ((EditText)activity.findViewById(R.id.login_pass)).setText("");
                    activity.startActivity(new Intent(activity, Main2Activity.class));
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.findViewById(R.id.login_progress).setVisibility(ProgressBar.VISIBLE);
        activity.findViewById(R.id.login_button).setEnabled(false);
        activity.findViewById(R.id.register_button).setEnabled(false);
        activity.findViewById(R.id.forgot_button).setEnabled(false);
    }

}
