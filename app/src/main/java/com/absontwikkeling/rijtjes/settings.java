package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;


public class settings extends AppCompatActivity {

    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;

    public static int [] settings = new int[3];
    private static int radioStateSettings = 0;
    public static String tableName;
    RadioGroup radioGroupSettings;
    CheckBox whitespace;
    CheckBox capitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        whitespace = (CheckBox) findViewById(R.id.whitespaceCB);
        capitals = (CheckBox) findViewById(R.id.capitalsCB);

        openDB();

        dbAdapterMain.createTableSettings();
        if (!dbAdapterMain.getAllRowsSettings().moveToFirst()) {
            for (int i = 0; i < settings.length; i++) {
                dbAdapterMain.insertRowSettings(0, i+1);
            }
        }
        listSettings(dbAdapterMain.getAllRowsSettings());

        Intent i = getIntent();
        tableName = i.getStringExtra("tableName");

        TextView debug = (TextView) findViewById(R.id.debug);
        debug.setText(displayQuery(dbAdapterMain.getAllRowsSettings()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
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

    public void onRadioButtonClicked(View v) {
        switch(v.getId()) {
            case R.id.defaultDirectionRB:
                radioStateSettings = 0;
                break;
            case R.id.reverseDirectionRB:
                radioStateSettings = 1;
                break;
        }
    }

    public void listSettings(Cursor c) {
        if (c.moveToFirst()) {
            for (int i = 0; i < settings.length; i++) {
                settings[i] = c.getInt(DBAdapter.COL_SETTINGS_VALUE);
                c.moveToNext();
            }
        } else {
            for (int i = 0; i < settings.length; i++) {
                settings[i] = 0;
            }
        }
        capitals.setChecked(settings[0] == 1);
        whitespace.setChecked(settings[1] == 1);
        if (settings[2] == 0) {
            radioGroupSettings = (RadioGroup) findViewById(R.id.radioGroup);
            radioGroupSettings.check(R.id.defaultDirectionRB);
        } else {
            radioGroupSettings = (RadioGroup) findViewById(R.id.radioGroup);
            radioGroupSettings.check(R.id.reverseDirectionRB);
        }
    }

    public void applySettings(View v) {
        for (int i = 0; i < settings.length; i++) {
            settings[i] = 0;
        }

        if (capitals.isChecked()) {
            settings[0] = 1;
        }

        if (whitespace.isChecked()) {
            settings[1] = 1;
        }

        settings[2] = radioStateSettings;

        for (int i = 0; i < settings.length; i++) {
            dbAdapterMain.updateRowSettings(i+1, settings[i]);
        }

        Intent i = new Intent(this, question.class);
        i.putExtra("settings", settings);
        i.putExtra("tableName", tableName);
        startActivity(i);
    }

    private String displayQuery(Cursor cursor) {
        String message = "";
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int value = cursor.getInt(DBAdapter.COL_SETTINGS_VALUE);
                int id = cursor.getInt(DBAdapter.COL_SETTINGS_ROWID);

                // Append data to the message:
                message += id +"\n" + value + "\n";
            } while(cursor.moveToNext());
        }

        cursor.close();
        return message;
    }

}
