package layout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.R;
import com.absontwikkeling.rijtjes.question;

/**
 * A simple {@link Fragment} subclass.\
 */

public class settings extends Fragment {

    View rootView;

    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;

    public static int [] settings = new int[3];
    private static int radioStateSettings = 0;
    RadioGroup radioGroupSettings;
    CheckBox whitespace;
    CheckBox capitals;

    public settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        whitespace = (CheckBox) rootView.findViewById(R.id.whitespaceCB);
        capitals = (CheckBox) rootView.findViewById(R.id.capitalsCB);

        openDB();

        dbAdapterMain.createTableSettings();
        if (!dbAdapterMain.getAllRowsSettings().moveToFirst()) {
            for (int i = 0; i < settings.length; i++) {
                dbAdapterMain.insertRowSettings(0, i+1);
            }
        }
        listSettings(dbAdapterMain.getAllRowsSettings());

        TextView debug = (TextView) rootView.findViewById(R.id.debug);
        debug.setText(displayQuery(dbAdapterMain.getAllRowsSettings()));

        Button b = (Button) rootView.findViewById(R.id.overhoor);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applySettings();
            }
        });

        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                switch(i) {
                    case R.id.defaultDirectionRB:
                        radioStateSettings = 0;
                        break;
                    case R.id.reverseDirectionRB:
                        radioStateSettings = 1;
                        break;
                }
            }
        });

        return rootView;
    }

    public void onDestroyView() {
        super.onDestroyView();
        closeDB();
    }

    private void openDB() {
        dbAdapter = new DBAdapter(getContext(), DBAdapter.DATABASE_NAME, DBAdapter.DATABASE_VERSION);
        dbAdapter.open();
        dbAdapterMain = new DBAdapter(getContext(), DBAdapter.DATABASE_MAIN_NAME, DBAdapter.DATABASE_MAIN_VERSION);
        dbAdapterMain.open();
    }

    private void closeDB() {
        dbAdapter.close();
        dbAdapterMain.close();
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
            radioGroupSettings = (RadioGroup) rootView.findViewById(R.id.radioGroup);
            radioGroupSettings.check(R.id.defaultDirectionRB);
        } else {
            radioGroupSettings = (RadioGroup) rootView.findViewById(R.id.radioGroup);
            radioGroupSettings.check(R.id.reverseDirectionRB);
        }
    }

    public void applySettings() {
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
