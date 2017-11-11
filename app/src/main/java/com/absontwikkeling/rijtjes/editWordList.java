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

        // Gets information from intent
        Intent i = getIntent();
        final String table_name = i.getStringExtra("tableName");

        // Gives debug textView text
        debugListTV = (TextView) findViewById(R.id.debugListTV);
        try {
            Cursor c = dbAdapter.getAllRows(table_name);
            String text = table_name + "\n" + displayQuery(c);
            // debugListTV.setText(text);
            showList(c);
            c.close();
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
        linearLayoutAnswer = (LinearLayout) findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) findViewById(R.id.questionLinearLayout);

         if (c.moveToFirst()) {
            do {
                EditText queET = new EditText(this);
                queET.setText(c.getString(1));
                queET.setHeight(100);
                queET.setWidth(2000);
                LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsQ.setMargins(0, 0, 0, 10);
                queET.setLayoutParams(paramsQ);
                queET.setGravity(8);
                queET.setPadding(30,40,0,0);
                linearLayoutQuestion.addView(queET);

                EditText ansET = new EditText(this);
                ansET.setText(c.getString(2));
                ansET.setHeight(100);
                ansET.setWidth(2000);
                LinearLayout.LayoutParams paramsA = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsA.setMargins(0, 0, 0, 10);
                ansET.setLayoutParams(paramsA);
                ansET.setGravity(8);
                ansET.setPadding(30,40,0,0);
                linearLayoutAnswer.addView(ansET);

            } while (c.moveToNext());
        }
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
        return message;
    }

}
