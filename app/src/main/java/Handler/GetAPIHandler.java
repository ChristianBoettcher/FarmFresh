package Handler;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Structure.Links;
import group3.tcss450.uw.edu.farmfresh.Main2Activity;
import group3.tcss450.uw.edu.farmfresh.R;

import static Structure.Links.API_LINK;

/**
 * Created by Doseon on 11/3/2017.
 */

public class GetAPIHandler extends AsyncTask<String, Void, String> {
    Main2Activity activity;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> itemList;


    public GetAPIHandler(Main2Activity activity,
                         ArrayAdapter<String> adapter, ArrayList<String> itemList) {
        this.activity = activity;
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
        activity.findViewById(R.id.search_loading).setVisibility(View.GONE);
        activity.findViewById(R.id.search_button).setEnabled(true);
        // Something wrong with the network or the URL.
        if (response.startsWith("Unable to")) {
            Toast.makeText(activity.getApplicationContext(), response, Toast.LENGTH_LONG)
                   .show();
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
            } catch (Exception ex) {
                //not JSON RETURNED
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.findViewById(R.id.search_loading).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.search_button).setEnabled(false);
    }
}
