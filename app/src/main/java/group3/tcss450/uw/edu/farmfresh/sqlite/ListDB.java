package group3.tcss450.uw.edu.farmfresh.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import group3.tcss450.uw.edu.farmfresh.R;

/**
 * The SQLite Database of markets available in the list.
 * Created by Doseo on 12/2/2017.
 */

public class ListDB {
    // Version of DB.
    public static final int DB_VERSION = 1;

    // Name of the database.
    private final String DB_NAME;

    //The name of table is read.
    private final String COLORS_TABLE;

    //The name of the columns that are read in the table.
    private final String[] COLUMN_NAMES;

    //Inner class that helps operate SQLite.
    private ColorDBHelper mColorDBHelper;

    //The SQLite database object.
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * Constructs a database with information inside of the string values.
     * @param context the context of the application calling the database.
     */
    public ListDB(Context context) {

        COLUMN_NAMES = context.getResources().getStringArray(R.array.DB_COLUMN_NAMES2);
        DB_NAME = context.getString(R.string.DB_NAME2);
        COLORS_TABLE = context.getString(R.string.TABLE_NAME2);

        mColorDBHelper = new ColorDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mColorDBHelper.getWritableDatabase();
    }

    /**
     * Saves market into the database with it's id.
     * @param marketname Name of the market.
     * @param marketid ID of the market.
     * @return the success of inserting the market into database.
     */
    public boolean insertMarket(String marketname, Integer marketid) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAMES[0], marketname);
        contentValues.put(COLUMN_NAMES[1], marketid);

        long rowId = mSQLiteDatabase.insert(COLORS_TABLE, null, contentValues);
        return rowId != -1;
    }

    //Closes the database.
    public void closeDB() {
        mSQLiteDatabase.close();
    }


    /**
     * Clears the table to save a new list.
     */
    public void clearList() {
        mSQLiteDatabase.execSQL("delete from "+ COLORS_TABLE);
    }
    /**
     * Returns the list of UserEntry objects from the local Colors table.
     *
     * @return list
     */
    public List<ListEntry> getList() {

        Cursor c = mSQLiteDatabase.query(
                COLORS_TABLE,  // The table to query
                COLUMN_NAMES,                               // The COLUMN_NAMES to return
                null,                                // The COLUMN_NAMES for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<ListEntry> list = new ArrayList<ListEntry>();
        for (int i=0; i<c.getCount(); i++) {
            String name = c.getString(0);
            int id = c.getInt(1);
            list.add(new ListEntry(name, id));
            c.moveToNext();
        }
        return list;
    }

    /**
     * Inner class to help operate the SQLite database object.
     */
    class ColorDBHelper extends SQLiteOpenHelper {

        // Query to generate the SQL database.
        private final String CREATE_COLOR_SQL;

        //Query to destroy the SQL database.
        private final String DROP_COLOR_SQL;

        /**
         * Constructs the helper for SQLite database.
         * @param context context of the current machine.
         * @param name the name database.
         * @param factory the cursor of the database.
         * @param version the version of Database.
         */
        public ColorDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_COLOR_SQL = context.getString(R.string.CREATE_FARMFRESH_SQL2);
            DROP_COLOR_SQL = context.getString(R.string.DROP_FARMFRESH_SQL2);

        }

        /**
         * Creates the database.
         * @param sqLiteDatabase the database in which to create the DB.
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_COLOR_SQL);
        }

        /**
         * Destroys the database.
         * @param sqLiteDatabase the database in which to destroy the DB.
         * @param i unnecessary paramter.
         * @param i1 unnecessary paramter.
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_COLOR_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}