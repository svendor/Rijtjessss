package com.absontwikkeling.rijtjes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    // For logging:
    private static final String TAG = "DBAdapter";

    // DB mainTable Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    public static final String KEY_QUESTION = "question";
    public static final String KEY_ANSWER = "answer";
    public static final int COL_QUESTION = 1;
    public static final int COL_ANSWER = 2;

    public static final String KEY_ROWID_MAIN = "_id";
    public static final int COL_ROWID_MAIN = 0;
    public static final String KEY_TABLE_NAME_MAIN = "table_name";
    public static final int COL_TABLE_NAME_MAIN = 1;
    public static final String MAIN_TABLE_NAME = "list_table";

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_QUESTION, KEY_ANSWER};
    public static final String[] ALL_KEYS_MAIN = new String[] {KEY_ROWID_MAIN, KEY_TABLE_NAME_MAIN};
    public static final String DATABASE_NAME = "wordLists";
    public static final String DATABASE_TABLE = "mainTable";
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_QUESTION + " string not null, "
                    + KEY_ANSWER + " string not null"
                    + ");";

    // Context of application who uses us.
    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    //	Public methods:

    public void createTableMain() {
        db.execSQL("create table if not exists " + MAIN_TABLE_NAME + " ("
                + KEY_ROWID_MAIN + " integer primary key autoincrement, "
                + KEY_TABLE_NAME_MAIN + " string not null);");
    }

    public void createTable(String tableName) {
        tableName = tableName.replaceAll("\\s","_");
        db.execSQL("create table if not exists " + tableName + " ("
                + KEY_ROWID + " integer primary key autoincrement, "
                + KEY_QUESTION + " string not null, "
                + KEY_ANSWER + " string not null);");
    }

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    public long insertRowMain(String tableName) {

        ContentValues values = new ContentValues();
        values.put(KEY_TABLE_NAME_MAIN, tableName);

        return db.insert(MAIN_TABLE_NAME, null, values);
    }

    // Add a new set of values to the database.
    public long insertRow(String question, String answer, String tableName) {

        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_QUESTION, question);
        initialValues.put(KEY_ANSWER, answer);

        // Insert it into the database.
        return db.insert(tableName, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId, String tableName) {
        String row_ID = KEY_ROWID + "=" + rowId;
        return db.delete(tableName, row_ID, null) != 0;
    }

    public void deleteAll(String tableName) {
        Cursor c = getAllRows(tableName);
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId), tableName);
            } while (c.moveToNext());
        }
        c.close();
    }

    public Cursor getAllRowsMain() {
        String where = null;
        Cursor c = db.query(true, MAIN_TABLE_NAME, ALL_KEYS_MAIN,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Return all data in the database.
    public Cursor getAllRows(String tableName) {
        String where = null;
        Cursor c = 	db.query(true, tableName, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String question, String answer) {
        String where = KEY_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_QUESTION, question);
        newValues.put(KEY_ANSWER, answer);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }



    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}