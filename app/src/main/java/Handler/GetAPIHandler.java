package Handler;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import group3.tcss450.uw.edu.farmfresh.Main2Activity;

/**
 * Created by Doseon on 11/3/2017.
 */

public class GetAPIHandler extends AsyncTask<String, Void, String> {
    private static final String API_LINK
            = "https://search.ams.usda.gov/farmersmarkets/v1/data.svc/zipSearch?zip=";

    private ArrayAdapter<String> adapter;
    private ArrayList<String> itemList;


    public GetAPIHandler(ArrayAdapter<String> adapter, ArrayList<String> itemList) {
        this.itemList = itemList;
        this.adapter = adapter;
    }


    @Override
    protected String doInBackground(String... details) {
        String response = "";
        HttpURLConnection urlConnection = null;
        try {
            URL urlObject = new URL(API_LINK + details[0]);
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
        //Log.d("API_RESPONSE", response);
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        // Something wrong with the network or the URL.
        if (response.startsWith("Unable to")) {
            Log.d("MARKET_NAME", "ERROR");
            //Toast.makeText(weakActivity.get().getApplicationContext(), details.response, Toast.LENGTH_LONG)
            //        .show();
            return;
        } else {
            try {
                Log.d("MARKET_NAME", "SEARCH");
                JSONObject js_result = new JSONObject(response);
                JSONArray js_array = new JSONArray(js_result.getString("results"));

                for(int i = 0; i < js_array.length(); i++){
                    JSONObject obj = js_array.getJSONObject(i);

                    Integer id = obj.getInt("id");
                    String market_name=obj.getString("marketname");
                    Log.d("MARKET_NAME", market_name);
                    itemList.add(market_name);
                }
                adapter.notifyDataSetChanged();
                /*JSONObject mainObject = new JSONObject(response);
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

                }*/
            } catch (Exception ex) {
                //not JSON RETURNED
            }
        }
    }
}
