package group3.tcss450.uw.edu.farmfresh.handler;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import group3.tcss450.uw.edu.farmfresh.util.PostParams;

import static group3.tcss450.uw.edu.farmfresh.util.PostParams.getPostDataString;

/**
 * Dynamic POST request handler.
 * Created by Doseon on 11/3/2017.
 */

public class PostHandlerNoReturnAsync extends AsyncTask<PostParams, Void, String> {

    /**
     * Performs POST request.
     * @param params URL, user info.
     * @return JSON String.
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
}
