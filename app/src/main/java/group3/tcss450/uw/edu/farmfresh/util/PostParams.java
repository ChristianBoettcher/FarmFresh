package group3.tcss450.uw.edu.farmfresh.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for storing Parameters needed during background threads.
 * Created by Doseo on 11/3/2017.
 */

public class PostParams {

    /**
     * Request URL.
     */
    public String url;

    /**
     * User parameters.
     */
    public HashMap<String, String> postSet;

    /**
     * Initializes PostParams Object with:
     * @param url Request URL.
     * @param map Map of user parameters.
     */
    public PostParams(String url, HashMap<String, String> map) {
        this.url = url;
        this.postSet = map;
    }

    /**
     * Returns Post data String.
     * @param params map of parameters.
     * @return String Post data.
     */
    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
