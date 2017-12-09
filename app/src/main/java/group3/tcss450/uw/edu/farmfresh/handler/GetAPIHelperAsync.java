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
 * Async task helper that runs along details Async for filter purposes.
 * Created by baimenov on 12/5/2017.
 */

class GetAPIHelperAsync extends AsyncTask<Void, Void, Void>{

    //Filters to look for when updating the results.
    private String[] myFilters;

    //Map of market name and corresponding market id.
    private Map<String, Integer> myMap;

    //The adapter that is connected to the text view list.
    private ArrayAdapter<String> myAdapter;

    // The list that is used by the adapter to update text view list.
    private ArrayList<String> myItemList;

    //Search Activity that calls this task.
    private SearchActivity myActivity;

    /**
     * Constructor of the task to get details for items to filter through.
     * @param theFilters list of filters to check for in the list of markets and products.
     * @param theMap the map of market name and corresponding market id.
     * @param theAdapter the adapter to update the current list view on display.
     * @param theItemList the list that updates the name of markets.
     * @param theActivity the Search activity that calls this task.
     */
    GetAPIHelperAsync(String[] theFilters, Map<String, Integer> theMap,
                      ArrayAdapter<String> theAdapter, ArrayList<String> theItemList,
                      SearchActivity theActivity) {
        myItemList = theItemList;
        myFilters = theFilters;
        myMap = theMap;
        myAdapter = theAdapter;
        myActivity = theActivity;
    }

    /**
     * Background work that iterates through all the market names in order to filter through them.
     * Calls for details of all markets that are listed in the zipcode and filters through the list
     * of filters.
     * @return null, nothing to return.
     */
    @Override
    protected Void doInBackground(Void... details) {
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
                //Catch error.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
        return null;
    }


    /**
     * Post execution of the background task. Updates the current display of available markets.
     * @param voids empty void parameter.
     */
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

    /**
     * Updates the progress bar.
     * @param values the current value of progress.
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
