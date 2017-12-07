package layout;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.MainActivity;
import com.absontwikkeling.rijtjes.R;
import com.absontwikkeling.rijtjes.displayListACTIVITY;
import com.absontwikkeling.rijtjes.editWordListACTIVITY;
import com.absontwikkeling.rijtjes.settings;

/**
 * A simple {@link Fragment} subclass.
 */
public class displayList extends Fragment {

    DBAdapter DLdbAdapter;
    LinearLayout linearLayoutList;
    public static int radioState;
    // TextView showList;

    public displayList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_list, container, false);
    }

    // TODO Java fixen voor het displayList fragment
    /**
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void openDB() {
        DLdbAdapter = new DBAdapter(this);
        DLdbAdapter.open();
    }

    private void closeDB() {
        DLdbAdapter.close();
    }

    */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.questionTheList:
                if (checked) {
                    radioState = 0;
                }
                break;
            case R.id.editList:
                if (checked) {
                    radioState = 1;
                }
                break;
            case R.id.deleteList:
                if (checked) {
                    radioState = 2;
                }
                break;
        }
    }
    // TODO Java fixen voor het displayList fragment
/*
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0, 0, 0, 10);
                button.setLayoutParams(params);
                button.setGravity(8);
                button.setPadding(30,40,0,0);
                button.setBackgroundResource(0); // Maakt background doorzichtig geloof ik, maar niet zeker, want zie niet of de buttons nog werken -Arun
                // Function
                View.OnClickListener buttonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (radioState == 0) {
                            Intent i = new Intent(displayListACTIVITY.this, settings.class);
                            i.putExtra("tableName", tableName);
                            startActivity(i);

                        } else if (radioState == 1) {
                            Intent i = new Intent(displayListACTIVITY.this, editWordListACTIVITY.class);
                            i.putExtra("tableName", tableName);
                            startActivity(i);


                        }

                        } else if (radioState == 2) {
                            DLdbAdapter.deleteAll(tableName);
                            DLdbAdapter.deleteRowMain(tableName);
                            finish();
                            startActivity(getIntent());
                        }

                    }
                };

                button.setOnClickListener(buttonListener);

                // Add button to activity
                linearLayoutList.addView(button);

            } while(c.moveToNext());
        }

    }
    */

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
