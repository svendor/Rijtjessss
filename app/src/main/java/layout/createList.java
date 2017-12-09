package layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    DBAdapter dbAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create_list, container, false);

        openDB();
        tableName = (EditText) rootView.findViewById(R.id.dataBaseName);

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
        dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();
    }

    private void closeDB() {
        dbAdapter.close();
    }

    public void finishList() {
        dbAdapter.createTableMain();
        String table_name = tableName.getText().toString();
        dbAdapter.createTable(table_name);

        if (!dbAdapter.existsMain(table_name)) {
            dbAdapter.insertRowMain(table_name);
        }

        displayList fragment = new displayList();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativelayout_fragment, fragment).commit();
    }
}
