package group3.tcss450.uw.edu.farmfresh.sqlite;

/**
 * Created by Doseo on 12/2/2017.
 */

import java.io.Serializable;

/**
 * Encapsulates a tuple from the Color table.
 */
public class ListEntry implements Serializable {

    private final String name;
    private final Integer id;

    public ListEntry(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getMarketName() {
        return name;
    }

    public Integer getMarketId() {
        return id;
    }
}