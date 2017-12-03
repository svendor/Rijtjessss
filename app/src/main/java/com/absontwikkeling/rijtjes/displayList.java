package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

public class displayList extends AppCompatActivity {

    DBAdapter DLdbAdapter;
    LinearLayout linearLayoutList;
    // TextView showList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        // Opens database
        openDB();

        // Debugging for displayQuery()
        // showList = (TextView) findViewById(R.id.showList);
        // showList.setText(displayQuery(DLdbAdapter.getAllRowsMain()));

        // Creates list of buttons
        linearLayoutList = (LinearLayout) findViewById(R.id.displayListLinearLayout);
        createButtonListInLayout(DLdbAdapter.getAllRowsMain());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        DLdbAdapter = new DBAdapter(this);
        DLdbAdapter.open();
    }

    private void closeDB() {
        DLdbAdapter.close();
    }


    private void createButtonListInLayout(Cursor c) {
        if (c.moveToFirst()) {
            do {
                // Define button
                Button button = new Button(this);
                // Layout
                final String tableName = c.getString(DBAdapter.COL_TABLE_NAME_MAIN);
                button.setText(tableName);
                button.setTextColor(Color.parseColor("#454545"));
                button.setBackgroundResource(R.drawable.button);
                button.setHeight(100);
                button.setWidth(2000);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 10);
                button.setLayoutParams(params);
                button.setGravity(8);
                button.setPadding(30,40,0,0);
                button.setBackgroundResource(0); // Maakt background doorzichtig geloof ik, maar niet zeker, want zie niet of de buttons nog werken -Arun
                // Function
                View.OnClickListener buttonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(displayList.this, editWordList.class);
                        i.putExtra("tableName" ,tableName);
                        startActivity(i);
                    }
                };
                button.setOnClickListener(buttonListener);

                // Add button to activity
                linearLayoutList.addView(button);
            } while(c.moveToNext());
        }
    }

    /*
    ################### Function to check if main table works ###########################

    private String displayQuery(Cursor cursor) {
        String message = "";
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                String listName = cursor.getString(DBAdapter.COL_TABLE_NAME_MAIN);

                // Append data to the message:
                message += listName +"\n";
            } while(cursor.moveToNext());
        }

        cursor.close();
        return message;
    }
    */
}
