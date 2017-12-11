package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class settings extends AppCompatActivity {

    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;

    public static int [] settings = new int[3];
    public static int radioState = 1;
    public static String tableName;
    CheckBox whitespace;
    CheckBox capitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        openDB();

        whitespace = (CheckBox) findViewById(R.id.whitespaceCB);
        capitals = (CheckBox) findViewById(R.id.capitalsCB);

        Intent i = getIntent();
        tableName = i.getStringExtra("tableName");
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
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.reverseDirectionRB:
                if (checked) {
                    radioState = 0;
                }
                break;
            case R.id.defaultDirectionRB:
                if (checked) {
                    radioState = 1;
                }
                break;
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

        settings[2] = radioState;

        Intent i = new Intent(this, question.class);
        i.putExtra("settings", settings);
        i.putExtra("tableName", tableName);
        startActivity(i);
    }

}
