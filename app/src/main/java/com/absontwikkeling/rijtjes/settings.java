package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class settings extends AppCompatActivity {

    public static int [] settings = new int[2];
    public static String tableName;
    CheckBox whitespace;
    CheckBox capitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        whitespace = (CheckBox) findViewById(R.id.whitespaceCB);
        capitals = (CheckBox) findViewById(R.id.capitalsCB);

        Intent i = getIntent();
        tableName = i.getStringExtra("tableName");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        Intent i = new Intent(this, question.class);
        i.putExtra("settings", settings);
        i.putExtra("tableName", tableName);
        startActivity(i);
    }

}
