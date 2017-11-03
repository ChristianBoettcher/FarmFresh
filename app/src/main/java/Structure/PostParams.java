package Structure;

import java.util.HashMap;

/**
 * Created by Doseo on 11/3/2017.
 */

public class PostParams {
    public String url;
    public HashMap<String, String> postSet;

    public PostParams(String url, HashMap<String, String> map) {
        this.url = url;
        this.postSet = map;
    }
}
