package group3.tcss450.uw.edu.farmfresh.handler;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

import group3.tcss450.uw.edu.farmfresh.SearchActivity;
import group3.tcss450.uw.edu.farmfresh.R;

import static group3.tcss450.uw.edu.farmfresh.util.Links.API_DETAILS_LINK;
import static group3.tcss450.uw.edu.farmfresh.util.Links.API_LINK;

/**
 * Thread that searches for market around given zip code.
 * Created by Doseon on 11/3/2017.
 */

public class GetAPIAsync extends AsyncTask<String, Void, String> {
    /**
     * SearchActivity.
     */
    SearchActivity activity;

    /**
     * ArrayAdapter to populate list of markets.
     */
    private ArrayAdapter<String> adapter;

    /**
     * List of markets.
     */
    private ArrayList<String> itemList;

    private Map<String, String> myMap;

    private final String[] myFilters;

    /**
     * Constructs GetAPIAsync object.
     * Initializes:
     * @param activity SearchActivity
     * @param adapter Populates the list.
     * @param itemList List of Markets.
     */
    public GetAPIAsync(SearchActivity activity,
                       ArrayAdapter<String> adapter, ArrayList<String> itemList,
                       Map<String, String> map, String[] theFilters) {
        this.activity = activity;
        this.itemList = itemList;
        this.adapter = adapter;
        this.myMap = map;
        this.myFilters = theFilters;
    }

    /**
     * Runs in background.
     * Sends request to backend to retrieve all markets
     * around given zip code.
     * @param details zip code.
     * @return string response (JSON string) to OnPostExecute.
     */
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

    /**
     * Converts JSON string to object and populates
     * list of markets from it.
     * @param response JSON string.
     */
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

                JSONObject js_result = new JSONObject(response);
                JSONArray js_array = new JSONArray(js_result.getString("results"));

                for(int i = 0; i < js_array.length(); i++){
                    JSONObject obj = js_array.getJSONObject(i);
                    String id = obj.getString("id");
                    String market_name = obj.getString("marketname");
                    itemList.add(market_name);
                    myMap.put(market_name, id);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception ex) {
                //not JSON RETURNED
            }
        }
    }



    /**
     * Prepares thread.
     * Sets search button to disabled.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.findViewById(R.id.search_loading).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.search_button).setEnabled(false);
    }
}
