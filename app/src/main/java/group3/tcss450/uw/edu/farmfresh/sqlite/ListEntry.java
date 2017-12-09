package group3.tcss450.uw.edu.farmfresh.sqlite;

/**
 * Market object that is used as an entry for the SQLite database.
 * Created by Doseo on 12/2/2017.
 */

import java.io.Serializable;

/**
 * Encapsulates a tuple from the market object .
 */
public class ListEntry implements Serializable {

    //Name of the market.
    private final String name;

    //ID of the market.
    private final Integer id;

    /**
     * Generates market with given name and id.
     * @param name the name of market.
     * @param id the id of the market.
     */
    public ListEntry(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Returns the market name.
     * @return the market name.
     */
    public String getMarketName() {
        return name;
    }

    /**
     * Returns the market ID.
     * @return the market ID.
     */
    public Integer getMarketId() {
        return id;
    }
}