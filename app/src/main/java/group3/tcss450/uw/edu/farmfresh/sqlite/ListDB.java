package group3.tcss450.uw.edu.farmfresh.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group3.tcss450.uw.edu.farmfresh.R;

/**
 * Created by Doseo on 12/2/2017.
 */

public class ListDB {

    public static final int DB_VERSION = 1;
    private final String DB_NAME;
    private final String COLORS_TABLE;
    private final String[] COLUMN_NAMES;

    private ColorDBHelper mColorDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public ListDB(Context context) {

        COLUMN_NAMES = context.getResources().getStringArray(R.array.DB_COLUMN_NAMES2);
        DB_NAME = context.getString(R.string.DB_NAME2);
        COLORS_TABLE = context.getString(R.string.TABLE_NAME2);

        mColorDBHelper = new ColorDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mColorDBHelper.getWritableDatabase();
    }

    public boolean insertMarket(String marketname, Integer marketid) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAMES[0], marketname);
        contentValues.put(COLUMN_NAMES[1], marketid);

        long rowId = mSQLiteDatabase.insert(COLORS_TABLE, null, contentValues);
        return rowId != -1;
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }



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

    class ColorDBHelper extends SQLiteOpenHelper {

        private final String CREATE_COLOR_SQL;

        private final String DROP_COLOR_SQL;

        public ColorDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_COLOR_SQL = context.getString(R.string.CREATE_FARMFRESH_SQL2);
            DROP_COLOR_SQL = context.getString(R.string.DROP_FARMFRESH_SQL2);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_COLOR_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_COLOR_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}