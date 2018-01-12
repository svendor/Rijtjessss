package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

public class editWordListACTIVITY extends AppCompatActivity {

    TextView debugListTV;
    EditText tableNameET;
    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;
    LinearLayout linearLayoutQuestion;
    LinearLayout linearLayoutAnswer;
    View view;
    public int entryAmount = 0;
    public int savedAmount = 0;
    public int[] listIndex = new int[2000];
    public String table_name;
    public String language1;
    public String language2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word_list);

        setupButtons();

        // Opens database
        openDB();

        // Gets information from intent
        Intent i = getIntent();
        table_name = i.getStringExtra("tableName");
        language1 = i.getStringExtra("lan1");
        language2 = i.getStringExtra("lan2");

        // Defines tableNameET view
        tableNameET = (EditText) findViewById(R.id.tableNameET);
        tableNameET.setText(table_name);
        TextView lan1tv = (TextView) findViewById(R.id.lan1TV);
        lan1tv.setText(language1);
        TextView lan2tv = (TextView) findViewById(R.id.lan2TV);
        lan2tv.setText(language2);

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
            savedAmount = entryAmount;
            setScrollView();
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

    public void onBackPressed() {
        Intent i = new Intent(this, NavMenu.class);
        startActivity(i);
    }

    private void openDB() {
        dbAdapter = new DBAdapter(this, DBAdapter.DATABASE_NAME, DBAdapter.DATABASE_VERSION);
        dbAdapter.open();
        dbAdapterMain = new DBAdapter(this, DBAdapter.DATABASE_MAIN_NAME, DBAdapter.DATABASE_MAIN_VERSION);
        dbAdapterMain.open();
    }

    private void closeDB() {
        dbAdapter.close();
        dbAdapterMain.close();
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

    public void setScrollView() {
        // Neemt de scrollView
        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView_edit_word_list);
        // Geeft de parameters die de mogelijkheid geven om de lay-out aan te passen
        ViewGroup.LayoutParams params = scrollView.getLayoutParams();
        // Verandert in dit geval de hoogte tot 60% van het scherm van het apparaat
        params.height = (int) (getScreenHeight()*0.55);
        scrollView.setLayoutParams(params);

    }

    // TODO: Delete deze todo, is namelijk een test

    private int showList(Cursor c, int[] listIndex) {
        // Find layouts
        linearLayoutAnswer = (LinearLayout) findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) findViewById(R.id.questionLinearLayout);

        int i = 0;
        if (c != null && c.moveToFirst()) {
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
                //queET.setMaxLines(1);
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
                //ansET.setMaxLines(1);
                linearLayoutAnswer.addView(ansET);

                // Index edittext id's
                listIndex[i] = sNextGeneratedId.get()-1;
                i++;

            } while (c.moveToNext());
        }
        return i;
    }

    private void setupButtons() {
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button quizButton = (Button) findViewById(R.id.quizButton);
        Button addRow = (Button) findViewById(R.id.addRow);
        Button removeRow = (Button) findViewById(R.id.removeRow);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateList(view);
            }
        });
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTheList(view);
            }
        });
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow(view);
            }
        });
        removeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRow(view);
            }
        });
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
        queET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });
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
        queET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });
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
        dbAdapterMain.deleteRowMain(table_name);

        savedAmount = entryAmount;

        if (entryAmount == 0) {
            Intent i = new Intent(this, NavMenu.class);
            startActivity(i);
            Toast.makeText(this, "Lijst verwijderd", Toast.LENGTH_SHORT).show();
        } else {
            table_name = tableNameET.getText().toString();
            dbAdapter.createTable(table_name);
            if (!dbAdapterMain.existsMain(table_name)) {
                dbAdapterMain.insertRowMain(table_name, language1, language2);
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
            Intent j = new Intent(this, NavMenu.class);
            startActivity(j);
            Toast.makeText(this, "Lijst opgeslagen", Toast.LENGTH_SHORT).show();
        }
    }

    public void questionTheList(View v) {
        if (savedAmount != 0) {
            Intent i = new Intent(this, question.class);
            i.putExtra("tableName", table_name);
            startActivity(i);
        } else {
            Toast.makeText(this, "Er moeten wel woorden in jouw lijst staan opgeslagen!", Toast.LENGTH_SHORT).show();
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
