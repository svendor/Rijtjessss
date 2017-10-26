package com.absontwikkeling.rijtjes;

import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class displayList extends AppCompatActivity {

    DBAdapter DLdbAdapter;
    LinearLayout linearLayoutList;
    TextView showList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        openDB();

        showList = (TextView) findViewById(R.id.showList);
        showList.setText(displayQuery(DLdbAdapter.getAllRowsMain()));

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
                Button button = new Button(this);
                button.setText(c.getString(DBAdapter.COL_TABLE_NAME_MAIN));
                linearLayoutList.addView(button);
            } while(c.moveToNext());
        }
    }

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
}
