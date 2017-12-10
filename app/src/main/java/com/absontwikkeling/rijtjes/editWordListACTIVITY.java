package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

public class editWordListACTIVITY extends AppCompatActivity {

    TextView debugListTV;
    EditText tableNameET;
    DBAdapter dbAdapter;
    LinearLayout linearLayoutQuestion;
    LinearLayout linearLayoutAnswer;
    public static int entryAmount = 0;
    public static int[] listIndex = new int[2000];
    public static String table_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word_list);

        // Opens database
        openDB();

        // Gets information from intent
        Intent i = getIntent();
        table_name = i.getStringExtra("tableName");

        // Defines tableNameET view
        tableNameET = (EditText) findViewById(R.id.tableNameET);
        tableNameET.setText(table_name);

        // Defines Debug Textview
        // debugListTV = (TextView) findViewById(R.id.debugListTV);

        try {
            // Finds query
            Cursor c = dbAdapter.getAllRows(table_name);

            // Tests if the cursor returns correct table
            /* String text = table_name + "\n" + displayQuery(c);
               debugListTV.setText(text); */

            // Creates edittext fields + integer that contains the amount of fields
            entryAmount = showList(c, listIndex);
            setLinearLayout();
            c.close();
        } catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                debugListTV.setText("Oeps, we hebben niks gevonden.");
                // TODO: REMOVE ALL BUTTONS IN THIS CASE
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

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    // https://stackoverflow.com/questions/1714297/android-view-setidint-id-programmatically-how-to-avoid-id-conflicts
    private static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void setLinearLayout() {

        // Gets linearlayout
        LinearLayout layout = (LinearLayout) findViewById(R.id.editwordlistLinearLayout);
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = (int) (getScreenHeight()*0.60);
        params.width = (int) (getScreenWidth()*0.60);
        layout.setLayoutParams(params);

    }

    private int showList(Cursor c, int[] listIndex) {
        // Find layouts
        linearLayoutAnswer = (LinearLayout) findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) findViewById(R.id.questionLinearLayout);

        int i = 0;
        if (c.moveToFirst()) {
            do {
                //Define question edittext
                EditText queET = new EditText(this);
                queET.setId(generateViewId());
                queET.setText(c.getString(1));
                queET.setHeight(145);
                LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                      LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsQ.setMargins(0, 0, 0, 10);
                queET.setLayoutParams(paramsQ);
                queET.setGravity(8);
                queET.setPadding(30,40,0,0);
                linearLayoutQuestion.addView(queET);

                // Index edittext id's
                listIndex[i] = sNextGeneratedId.get()-1;
                i++;

                // Define answer edittext
                EditText ansET = new EditText(this);
                ansET.setId(generateViewId());
                ansET.setText(c.getString(2));
                ansET.setHeight(145);
                LinearLayout.LayoutParams paramsA = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsA.setMargins(0, 0, 0, 10);
                ansET.setLayoutParams(paramsA);
                ansET.setGravity(8);
                ansET.setPadding(30,40,0,0);
                linearLayoutAnswer.addView(ansET);

                // Index edittext id's
                listIndex[i] = sNextGeneratedId.get()-1;
                i++;

            } while (c.moveToNext());
        }
        return i;
    }

    public void addRow(View v) {
        // Find layouts
        linearLayoutAnswer = (LinearLayout) findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) findViewById(R.id.questionLinearLayout);

        //Define question edittext
        EditText queET = new EditText(this);
        queET.setId(generateViewId());
        queET.setText("");
        queET.setHeight(145);
        LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsQ.setMargins(0, 0, 0, 10);
        queET.setLayoutParams(paramsQ);
        queET.setGravity(8);
        queET.setPadding(30,40,0,0);
        linearLayoutQuestion.addView(queET);

        // Index edittext id's
        listIndex[entryAmount] = sNextGeneratedId.get()-1;
        entryAmount++;

        // Define answer edittext
        EditText ansET = new EditText(this);
        ansET.setId(generateViewId());
        ansET.setText("");
        ansET.setHeight(145);
        LinearLayout.LayoutParams paramsA = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsA.setMargins(0, 0, 0, 10);
        ansET.setLayoutParams(paramsA);
        ansET.setGravity(8);
        ansET.setPadding(30,40,0,0);
        linearLayoutAnswer.addView(ansET);

        // Index edittext id's
        listIndex[entryAmount] = sNextGeneratedId.get()-1;
        entryAmount++;
    }

    public void removeRow(View v) {
        // Find layouts
        linearLayoutAnswer = (LinearLayout) findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) findViewById(R.id.questionLinearLayout);

        if (entryAmount > 1) {
            //Define question edittext
            EditText queET = (EditText) findViewById(listIndex[entryAmount-1]);
            queET.setVisibility(View.GONE);

            // Remove EditText ID's
            listIndex[entryAmount] = 0;
            entryAmount--;

            // Define answer edittext
            EditText ansET = (EditText) findViewById(listIndex[entryAmount-1]);
            ansET.setVisibility(View.GONE);

            // Remove EditText ID's
            listIndex[entryAmount] = 0;
            entryAmount--;
        }
    }

    public void updateList(View v) {
        dbAdapter.deleteAll(table_name);
        dbAdapter.deleteRowMain(table_name);

        table_name = tableNameET.getText().toString();
        dbAdapter.createTable(table_name);
        if (!dbAdapter.existsMain(table_name)) {
            dbAdapter.insertRowMain(table_name);
        } else {
            dbAdapter.deleteAll(table_name);
        }

        int i = 0;
        do {
            EditText queET = (EditText) findViewById(listIndex[i]);
            String question = queET.getText().toString();
            i++;
            EditText ansET = (EditText) findViewById((listIndex[i]));
            String answer = ansET.getText().toString();
            i++;

            dbAdapter.insertRow(question, answer, table_name);
        } while (i < entryAmount);
    }

    public void questionTheList(View v) {
        Intent i = new Intent(this, question.class);
        i.putExtra("tableName", table_name);
        startActivity(i);
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
