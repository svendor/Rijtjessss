package layout;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.MainActivity;
import com.absontwikkeling.rijtjes.NavMenu;
import com.absontwikkeling.rijtjes.R;
import com.absontwikkeling.rijtjes.displayListACTIVITY;
import com.absontwikkeling.rijtjes.editWordListACTIVITY;
import com.absontwikkeling.rijtjes.settings;

/**
 * A simple {@link Fragment} subclass.
 */
public class displayList extends Fragment {

    DBAdapter DLdbAdapter;
    View view;
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
        view = inflater.inflate(R.layout.fragment_display_list, container, false);

        // Debugging for displayQuery()
        // showList = (TextView) findViewById(R.id.showList);
        // showList.setText(displayQuery(DLdbAdapter.getAllRowsMain()));

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                // checkedId is the RadioButton selected

                switch(i) {
                    case R.id.questionTheList:
                        radioState = 0;
                        break;
                    case R.id.editList:
                        radioState = 1;
                        break;
                    case R.id.deleteList:
                        radioState = 2;
                        break;
                }
            }
        });

        // Opens database
        openDB();

        // Creates list of buttons
        linearLayoutList = view.findViewById(R.id.displayListLinearLayout);
        createButtonListInLayout(DLdbAdapter.getAllRowsMain());

        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        closeDB();
    }

    private void openDB() {
        DLdbAdapter = new DBAdapter(getContext());
        DLdbAdapter.open();
    }

    private void closeDB() {
        DLdbAdapter.close();
    }

    private void createButtonListInLayout(Cursor c) {
        if (c.moveToFirst()) {
            do {
                // Define button
                Button button = new Button(getContext());
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
                            Intent i = new Intent(getActivity(), settings.class);
                            i.putExtra("tableName", tableName);
                            startActivity(i);

                        } else if (radioState == 1) {
                            Intent i = new Intent(getActivity(), editWordListACTIVITY.class);
                            i.putExtra("tableName", tableName);
                            startActivity(i);

                        } else if (radioState == 2) {
                            DLdbAdapter.deleteTable(tableName);
                            DLdbAdapter.deleteRowMain(tableName);
                            displayList fragment = (displayList) getFragmentManager().findFragmentById(R.id.relativelayout_fragment);
                            getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                        }

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
