package group3.tcss450.uw.edu.farmfresh.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import group3.tcss450.uw.edu.farmfresh.R;

/**
 * Created by Doseo on 12/2/2017.
 */

public class UserDB {

        public static final int DB_VERSION = 1;
        private final String DB_NAME;
        private final String COLORS_TABLE;
        private final String[] COLUMN_NAMES;

        private ColorDBHelper mColorDBHelper;
        private SQLiteDatabase mSQLiteDatabase;

        public UserDB(Context context) {

            COLUMN_NAMES = context.getResources().getStringArray(R.array.DB_COLUMN_NAMES);
            DB_NAME = context.getString(R.string.DB_NAME);
            COLORS_TABLE = context.getString(R.string.TABLE_NAME);

            mColorDBHelper = new ColorDBHelper(
                    context, DB_NAME, null, DB_VERSION);
            mSQLiteDatabase = mColorDBHelper.getWritableDatabase();
        }

        public boolean insertUser(String username, String password, boolean auto) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_NAMES[0], username);
            contentValues.put(COLUMN_NAMES[1], password);
            contentValues.put(COLUMN_NAMES[2], auto);

            long rowId = mSQLiteDatabase.insert(COLORS_TABLE, null, contentValues);
            return rowId != -1;
        }

        public void closeDB() {
            mSQLiteDatabase.close();
        }


        /**
         * Returns the list of UserEntry objects from the local Colors table.
         *
         * @return list
         */
        public UserEntry getUser() {

            Cursor c = mSQLiteDatabase.query(
                    COLORS_TABLE,  // The table to query
                    COLUMN_NAMES,                               // The COLUMN_NAMES to return
                    null,                                // The COLUMN_NAMES for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );
            //HACK just get last one which is most up to date.
            if (c == null || c.getCount() == 0) {
                return new UserEntry("", "", false);
            } else {
                c.moveToLast();
            /*List<UserEntry> list = new ArrayList<UserEntry>();
            for (int i = 0; i < c.getCount(); i++) {
                Long id = c.getLong(0);
                int red = c.getInt(1);
                int green = c.getInt(2);
                int blue = c.getInt(3);
                list.add(new UserEntry(id, Color.argb(255, red, green, blue)));
                c.moveToNext();
            }*/

                String user = c.getString(0);
                String pass = c.getString(1);
                boolean auto = false;
                if (c.getInt(2) == 1)
                    auto = true;
                Log.d("LOG INSIDE USERDB", user +", " + pass + ", " + auto);
                return new UserEntry(user, pass, auto);
            }
        }

        class ColorDBHelper extends SQLiteOpenHelper {

            private final String CREATE_COLOR_SQL;

            private final String DROP_COLOR_SQL;

            public ColorDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
                super(context, name, factory, version);
                CREATE_COLOR_SQL = context.getString(R.string.CREATE_FARMFRESH_SQL);
                DROP_COLOR_SQL = context.getString(R.string.DROP_FARMFRESH_SQL);

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