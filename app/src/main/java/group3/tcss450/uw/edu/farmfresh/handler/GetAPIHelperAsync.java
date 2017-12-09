package group3.tcss450.uw.edu.farmfresh.handler;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import group3.tcss450.uw.edu.farmfresh.R;
import group3.tcss450.uw.edu.farmfresh.SearchActivity;

import static group3.tcss450.uw.edu.farmfresh.util.Links.API_DETAILS_LINK;

/**
 * Created by baimenov on 12/5/2017.
 */

public class GetAPIHelperAsync extends AsyncTask<String, Void, Void>{

    String[] myFilters;

    Map<String, Integer> myMap;

    ArrayAdapter<String> myAdapter;

    ArrayList<String> myItemList;

    SearchActivity myActivity;

    public GetAPIHelperAsync(String[] theFilters, Map<String, Integer> theMap,
                             ArrayAdapter<String> theAdapter, ArrayList<String> theItemList,
                             SearchActivity theActivity) {
        myItemList = theItemList;
        myFilters = theFilters;
        myMap = theMap;
        myAdapter = theAdapter;
        myActivity = theActivity;
    }

    @Override
    protected Void doInBackground(String... details) {
        String response = "";
        HttpURLConnection urlConnection = null;
        Iterator<Map.Entry<String, Integer>> it = myMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            int id = entry.getValue();
            try {
                URL urlObject = new URL(API_DETAILS_LINK + id);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
                //Log.d("Your filter is: ", myFilters[0]);
                if (!response.toLowerCase().contains(myFilters[0].toLowerCase())) {
                    it.remove();
                    myItemList.remove(entry.getKey());
                }
                response = "";
            } catch (Exception ex) {

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
        return null;
    }



    @Override
    protected void onPostExecute(Void voids) {
        final ListView lv = (ListView) myActivity.findViewById(R.id.search_list);
        lv.setAdapter(myAdapter);
        myActivity.findViewById(R.id.search_loading).setVisibility(View.INVISIBLE);
        myActivity.findViewById(R.id.search_button).setEnabled(true);
        myAdapter.notifyDataSetChanged();
    }

    /**
     * Prepares thread.
     * Sets search button to disabled.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myActivity.findViewById(R.id.search_loading).setVisibility(View.VISIBLE);
        myActivity.findViewById(R.id.search_button).setEnabled(false);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
