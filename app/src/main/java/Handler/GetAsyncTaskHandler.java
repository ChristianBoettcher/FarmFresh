package Handler;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import GMailSender.GMailSender;
import Structure.RegistrationDetails;
import group3.tcss450.uw.edu.farmfresh.LoginFragment;
import group3.tcss450.uw.edu.farmfresh.MainActivity;
import group3.tcss450.uw.edu.farmfresh.PinFragment;
import group3.tcss450.uw.edu.farmfresh.R;

/**
 * Created by Doseo on 11/3/2017.
 */

public class GetAsyncTaskHandler extends AsyncTask<RegistrationDetails, Void, RegistrationDetails> {

    private static final String CHECK_USER_URL
            = "http://farmfresh.getenjoyment.net/check_user.php?user=";

    WeakReference<Activity> weakActivity;
    android.support.v4.app.FragmentTransaction transaction;
    public GetAsyncTaskHandler(MainActivity activity, android.support.v4.app.FragmentTransaction ts) {
        weakActivity = new WeakReference<Activity>(activity);
        transaction = ts;
    }

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
            Toast.makeText(weakActivity.get().getApplicationContext(), details.response, Toast.LENGTH_LONG)
                    .show();
            return;
        } else {
            try {
                JSONObject mainObject = new JSONObject(details.response);
                Integer code = mainObject.getInt("code");
                if (code == 200) {
                    Toast.makeText(weakActivity.get().getApplicationContext(),
                            "Username already exists.", Toast.LENGTH_LONG).show();
                    details.email_text.setError("Email already exists.");
                    return;
                } else {
                    sendEmail(details.pincode, details.user);

                    Bundle args = new Bundle();
                    args.putSerializable(weakActivity.get().getString(R.string.email_key),
                            details.email_text.getText().toString());
                    args.putSerializable(weakActivity.get().getString(R.string.password_key),
                            details.pass_text.getText().toString());
                    args.putSerializable(weakActivity.get().getString(R.string.pincode_key),
                            details.pincode.toString());

                    transaction.commit();

                }
            } catch (Exception ex) {
                //not JSON RETURNED
            }
        }
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
}
