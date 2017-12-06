package group3.tcss450.uw.edu.farmfresh.handler;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static group3.tcss450.uw.edu.farmfresh.util.Links.API_DETAILS_LINK;

/**
 * Created by baimenov on 12/5/2017.
 */

public class GetAPIHelperAsync extends AsyncTask<String, Void, String>{

    String[] myFilters;

    String myID;

    JSONArray myjs_array;

    String returnValue = "1";

    public GetAPIHelperAsync(String[] theFilters, String theID, JSONArray js_array) {
        myFilters = theFilters;
        myID = theID;
        myjs_array = js_array;
    }

    @Override
    protected String doInBackground(String... details) {

        try {

            for (int i = 0; i < myjs_array.length(); i++) {
                JSONObject js_object = myjs_array.getJSONObject(i);

                String id = js_object.getString("id");
            }
            //boolean toReturn = false;
            String newResponse = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(API_DETAILS_LINK + myID);

                urlConnection = (HttpURLConnection) urlObject.openConnection();
                Log.d("trying before ", "inputStream creation");
                InputStream content = urlConnection.getInputStream();
                Log.d("trying after ", "inputStream creation");
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";

                while ((s = buffer.readLine()) != null) {
                    newResponse += s;
                }

                if (newResponse.startsWith("Unable to")) {
                    Log.d("error", "error DetailsAPI");
                } else {
                    if (filteredHelper(newResponse)) {
                        return "1";
                    } else {
                        return "0";
                    }
                }
            } catch (Exception ex) {
                Log.d("Something went wrong", "w/ inputStream");
                Log.e("MYAPP", "exception", ex);

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return "0";
        } catch (Exception ex) {

        }
        return "0";
    }

    private boolean filteredHelper(String newResponse) {
        boolean toReturn = false;
        try {
            JSONObject js_result = new JSONObject(newResponse);
            JSONObject details = new JSONObject(js_result.getString("marketdetails"));
            String products = (String) details.get("Products");
            Log.d("Products are", products);
            Log.d("Filter is ", myFilters[0]);
            if (products.contains(myFilters[0])) {
                toReturn = true;
            }
        } catch (Exception ex){
            Log.d("Something wrong", "Something Wrong");
        }
        return toReturn;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response.equals("1")) {
            returnValue = "1";
        } else {
            returnValue = "0";
        }
    }

    public String getReturnValue() {
        Log.d("return value is", returnValue);
        return returnValue;
    }
}
