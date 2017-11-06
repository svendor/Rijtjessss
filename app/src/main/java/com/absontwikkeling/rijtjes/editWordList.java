package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class editWordList extends AppCompatActivity {

    TextView debugListTV;
    DBAdapter dbAdapter;
    LinearLayout linearLayoutQuestion;
    LinearLayout linearLayoutAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word_list);

        // Opens database
        openDB();

        linearLayoutAnswer = (LinearLayout) findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) findViewById(R.id.questionLinearLayout);

        // Gets information from intent
        Intent i = getIntent();
        final String table_name = i.getStringExtra("tableName");
        final String TABLE_NAME = table_name.replaceAll("\\s","");

        // Gives debug textView text
        debugListTV = (TextView) findViewById(R.id.debugListTV);
        try {
            Cursor c = dbAdapter.getAllRows(TABLE_NAME);
            String text = table_name + "\n" + displayQuery(c);
            debugListTV.setText(text);
            // showList(c);
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

    private void showList(Cursor c) {
        if (c.moveToFirst()) {
            do {
                EditText queET = new EditText(this);
                queET.setText(c.getString(dbAdapter.COL_QUESTION));
                linearLayoutQuestion.addView(queET);

                EditText ansET = new EditText(this);
                ansET.setText(c.getString(dbAdapter.COL_ANSWER));
                linearLayoutAnswer.addView(ansET);

            } while (c.moveToNext());
        }
        c.close();
    }

    private String displayQuery(Cursor cursor) {
        String message = "";
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                String question = cursor.getString(DBAdapter.COL_QUESTION);
                String answer = cursor.getString(DBAdapter.COL_ANSWER);

                // Append data to the message:
                message += "Vraag = " + question
                        +", Antwoord = " + answer
                        +"\n";
            } while(cursor.moveToNext());
        }

        cursor.close();
        return message;
    }

}
