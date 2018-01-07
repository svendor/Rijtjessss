package layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.R;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

/**
 * A simple {@link Fragment} subclass.
 */
public class createList extends Fragment {

    public createList() {
        // Required empty public constructor
    }

    View rootView;
    EditText tableName;
    Spinner language1Spinner;
    Spinner language2Spinner;
    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create_list, container, false);

        openDB();
        tableName = (EditText) rootView.findViewById(R.id.dataBaseName);
        language1Spinner = (Spinner) rootView.findViewById(R.id.language1Spinner);
        language2Spinner = (Spinner) rootView.findViewById(R.id.language2Spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.languagues, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language1Spinner.setAdapter(adapter);
        language2Spinner.setAdapter(adapter);

        Button finishList = (Button) rootView.findViewById(R.id.finishList);
        finishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishList();
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

    public void finishList() {
        dbAdapterMain.createTableMain();
        String table_name = tableName.getText().toString();
        String language1 = language1Spinner.getSelectedItem().toString();
        String language2 = language2Spinner.getSelectedItem().toString();
        dbAdapter.createTable(table_name);

        if (!dbAdapterMain.existsMain(table_name)) {
            dbAdapterMain.insertRowMain(table_name, language1, language2);
        }

        displayList fragment = new displayList();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativelayout_fragment, fragment).commit();
    }
}
