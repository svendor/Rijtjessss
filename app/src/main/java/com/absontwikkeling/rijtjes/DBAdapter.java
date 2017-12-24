package com.absontwikkeling.rijtjes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

public class DBAdapter {

    // For logging:
    private static final String TAG = "DBAdapter";

    // DB 'mainTable' Fields
    public static final String KEY_ROWID_MAIN = "_id";
    public static final String KEY_TABLE_NAME_MAIN = "table_name";
    public static final String KEY_MAIN_LANGUAGE_1 = "language_1";
    public static final String KEY_MAIN_LANGUAGE_2 = "language_2";
    public static final int COL_ROWID_MAIN = 0;
    public static final int COL_TABLE_NAME_MAIN = 1;
    public static final int COL_MAIN_LANGUAGE_1 = 2;
    public static final int COL_MAIN_LANGUAGE_2 = 3;
    public static final String MAIN_TABLE_NAME = "list_table";
    public static final String[] ALL_KEYS_MAIN = new String[] {KEY_ROWID_MAIN, KEY_TABLE_NAME_MAIN, KEY_MAIN_LANGUAGE_1, KEY_MAIN_LANGUAGE_2};

    // DB 'settings' Fields
    public static final String KEY_SETTINGS_ROWID = "_id";
    public static final String KEY_SETTINGS_VALUE = "value";
    public static final int COL_SETTINGS_ROWID = 0;
    public static final int COL_SETTINGS_VALUE = 1;
    public static final String SETTINGS_TABLE_NAME = "settings";
    public static final String[] ALL_KEYS_SETTINGS = new String[] {KEY_SETTINGS_ROWID, KEY_SETTINGS_VALUE};


    // DB 'mainTable' General info
    public static final String DATABASE_MAIN_NAME = "main_database";
    public static final int DATABASE_MAIN_VERSION = 5;

    // DB 'wordList' Fields (The table which indexes the wordLists)
    public static final String KEY_ROWID = "_id";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_ANSWER = "answer";
    public static final int COL_ROWID = 0;
    public static final int COL_QUESTION = 1;
    public static final int COL_ANSWER = 2;
    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_QUESTION, KEY_ANSWER};

    // DB 'wordLists' General info
    public static final String DATABASE_NAME = "wordLists";
    public static final int DATABASE_VERSION = 4;

    // Context of application who uses us.
    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    ///////////////////////////////////////////////////////
    // ############### Public methods: ################# //
    ///////////////////////////////////////////////////////

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public DBAdapter(Context ctx, String name, int i) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context, name, i);
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    /////////////////////////////////////////////////////
    // ################ mainDB tables ################ //
    /////////////////////////////////////////////////////

    // Create mainTable if it doesn't exist yet
    public void createTableMain() {
        db.execSQL("create table if not exists " + MAIN_TABLE_NAME + " ("
                + KEY_ROWID_MAIN + " integer primary key autoincrement, "
                + KEY_TABLE_NAME_MAIN + " string not null, "
                + KEY_MAIN_LANGUAGE_1 + " integer not null, "
                + KEY_MAIN_LANGUAGE_2 + " integer not null);");
    }

    // Add a new set of values to the mainTable
    public long insertRowMain(String tableName, String language1, String language2) {
        ContentValues values = new ContentValues();
        values.put(KEY_TABLE_NAME_MAIN, tableName);
        values.put(KEY_MAIN_LANGUAGE_1, language1);
        values.put(KEY_MAIN_LANGUAGE_2, language2);

        return db.insert(MAIN_TABLE_NAME, null, values);
    }

    // Delete a row from mainTable, by tableName
    public void deleteRowMain(String tableName) {
        db.execSQL("DELETE FROM " + MAIN_TABLE_NAME + " WHERE " + KEY_TABLE_NAME_MAIN + "='" + tableName + "';");
    }

    public void deleteRowMainByID(long id) {
        db.execSQL("DELETE FROM " + MAIN_TABLE_NAME + " WHERE " + KEY_ROWID_MAIN + " = " + id + ";");
    }

    // Check if a row in mainTable exists
    public boolean existsMain(String table_name) {
        Cursor c = db.rawQuery("SELECT * FROM " + MAIN_TABLE_NAME + " WHERE " + KEY_TABLE_NAME_MAIN + " = '" + table_name + "'", null);
        boolean exist = (c.getCount() > 0);
        c.close();
        return exist;
    }

    public Cursor getRowMain(String tableName) {
        String where = KEY_TABLE_NAME_MAIN + "=" + tableName;
        Cursor c = db.query(true, MAIN_TABLE_NAME, ALL_KEYS_MAIN,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowMainByID(long id) {
        String where = KEY_ROWID_MAIN + "=" + id;
        Cursor c = db.query(true, MAIN_TABLE_NAME, ALL_KEYS_MAIN, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get's all rows from mainTable
    public Cursor getAllRowsMain() {
        String where = null;
        Cursor c = db.query(true, MAIN_TABLE_NAME, ALL_KEYS_MAIN,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    ///////////////////////////////////////////////////////
    // ################ settings tables ################ //
    ///////////////////////////////////////////////////////

    public void createTableSettings() {
        db.execSQL("create table if not exists " + SETTINGS_TABLE_NAME + " ("
                + KEY_SETTINGS_ROWID + " integer primary key, "
                + KEY_SETTINGS_VALUE + " integer not null);");
    }

    // Add a new set of values to the 'settings'
    public long insertRowSettings(int value, int id) {
        ContentValues values = new ContentValues();
        values.put(KEY_SETTINGS_VALUE, value);
        String where = KEY_SETTINGS_ROWID + "=" + id;

        return db.insert(SETTINGS_TABLE_NAME, where, values);
    }

    /*
    // Delete a row from 'settings', by tableName
    public void deleteRowSettings(String settingsName) {
        db.execSQL("DELETE FROM " + SETTINGS_TABLE_NAME + " WHERE " + KEY_SETTINGS_NAME + "='" + settingsName + "';");
    }

    // Check if a row in 'settings' exists
    public boolean existsSettings() {
        Cursor c = db.rawQuery("SELECT * FROM " + MAIN_TABLE_NAME + " WHERE " + KEY_TABLE_NAME_MAIN + " = '" + table_name + "'", null);
        boolean exist = (c.getCount() > 0);
        c.close();
        return exist;
    }

    public Cursor getRowSettings() {
        String where = KEY_TABLE_NAME_MAIN + "=" + tableName;
        Cursor c = db.query(true, tableName, ALL_KEYS_MAIN,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    */

    // Get's all rows from 'settings'
    public Cursor getAllRowsSettings() {
        String where = null;
        Cursor c = db.query(true, SETTINGS_TABLE_NAME, ALL_KEYS_SETTINGS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Updates row in 'settings' table
    public boolean updateRowSettings(long rowId, int i) {
        String where = KEY_SETTINGS_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_SETTINGS_VALUE, i);

        // Insert it into the database.
        return db.update(SETTINGS_TABLE_NAME, newValues, where, null) != 0;
    }

    ///////////////////////////////////////////////////////
    // ################ wordList tables ################ //
    ///////////////////////////////////////////////////////

    // Create table for wordList
    public void createTable(String tableName) {
        db.execSQL("create table if not exists '" + tableName.replaceAll("\\s", "_") + "' ("
                + KEY_ROWID + " integer primary key autoincrement, "
                + KEY_QUESTION + " string not null, "
                + KEY_ANSWER + " string not null);");
    }

    // Add a new set of values to the database.
    public long insertRow(String question, String answer, String tableName) {

        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_QUESTION, question);
        initialValues.put(KEY_ANSWER, answer);

        // Insert it into the database.
        return db.insert(tableName.replaceAll("\\s", "_"), null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId, String tableName) {
        String row_ID = KEY_ROWID + "=" + rowId;
        return db.delete(tableName.replaceAll("\\s", "_"), row_ID, null) != 0;
    }

    // Removes all data from a table
    public void deleteAll(String tableName) {
        Cursor c = getAllRows(tableName.replaceAll("\\s", "_"));
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId), tableName.replaceAll("\\s","_"));
            } while (c.moveToNext());
        }
        c.close();
        //db.execSQL("TRUNCATE TABLE '" + tableName.replaceAll("\\s","_") + "';");
    }

    // Deletes a table
    public void deleteTable(String tableName) {
        db.execSQL("DROP TABLE IF EXISTS '" + tableName.replaceAll("\\s","_") + "';");
    }

    // Return all data in the table.
    public Cursor getAllRows(String tableName) {
        Cursor c = db.rawQuery("SELECT * FROM ["+tableName.replaceAll("\\s", "_")+"]", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public int getRowCount(String table_name) {
        int count = 0;
        Cursor c = getAllRows(table_name);
        if (c.moveToFirst()) {
            do {
                count++;
            } while (c.moveToNext());
        }
        return count;
    }

    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context, String name, int i) {
            super(context, name, null, i);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            //_db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            _db.execSQL("DROP TABLE " + MAIN_TABLE_NAME + ";");
            _db.execSQL("DROP TABLE " + SETTINGS_TABLE_NAME + ";");

            // Recreate new database:
            onCreate(_db);
        }
    }
}