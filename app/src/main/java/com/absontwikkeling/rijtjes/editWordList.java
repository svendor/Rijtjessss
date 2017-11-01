package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class editWordList extends AppCompatActivity {

    TextView debugListTV;
    DBAdapter dbAdapter;
    String TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word_list);

        // Opens database
        openDB();

        // Gets information from intent
        Intent i = getIntent();
        final String TABLE_NAME = i.getStringExtra("tableName").replaceAll("\\s","_");

        debugListTV = (TextView) findViewById(R.id.debugListTV);
        try {
            Cursor c = dbAdapter.getAllRows(TABLE_NAME);
            debugListTV.setText(displayQuery(c));
        } catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                debugListTV.setText("Oeps, we hebben niks gevonden.");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
    }

    private void closeDB() {
        dbAdapter.close();
    }

    private String displayQuery(Cursor cursor) {
        String message = "";
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String question = cursor.getString(DBAdapter.COL_QUESTION);
                String answer = cursor.getString(DBAdapter.COL_ANSWER);

                // Append data to the message:
                message += "Vraag #" + id
                        +", Vraag = " + question
                        +", Antwoord = " + answer
                        +"\n";
            } while(cursor.moveToNext());
        }

        cursor.close();
        return message;
    }

}
