package layout;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.R;
import com.absontwikkeling.rijtjes.displayListACTIVITY;
import com.absontwikkeling.rijtjes.question;

import java.util.concurrent.atomic.AtomicInteger;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

/**
 * A simple {@link Fragment} subclass.
 */
public class editWordList extends Fragment {

    View rootView;
    TextView debugListTV;
    EditText tableNameET;
    DBAdapter dbAdapter;
    LinearLayout linearLayoutQuestion;
    LinearLayout linearLayoutAnswer;
    public static int entryAmount = 0;
    public static int[] listIndex = new int[2000];
    public static String table_name;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);


    public editWordList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_edit_word_list, container, false);
        // Opens Database
        openDB();

        Bundle bundle = this.getArguments();
        table_name = bundle.getString("tableName");

        // Defines tableNameET view
        tableNameET = (EditText) rootView.findViewById(R.id.tableNameET);
        tableNameET.setText(table_name);

        Button addRow = (Button) rootView.findViewById(R.id.addRowButton);
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow();
            }
        });

        try {
            // Finds query
            Cursor c = dbAdapter.getAllRows(table_name);

            // Tests if the cursor returns correct table
            /* String text = table_name + "\n" + displayQuery(c);
               debugListTV.setText(text); */


            // Creates edittext fields + integer that contains the amount of fields
            entryAmount = showList(c, listIndex);
            setLinearLayout();
            c.close();
        } catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                debugListTV.setText("Oeps, we hebben niks gevonden.");
                // TODO: REMOVE ALL BUTTONS IN THIS CASE
            }
        }

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

    // https://stackoverflow.com/questions/1714297/android-view-setidint-id-programmatically-how-to-avoid-id-conflicts
    private static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void setLinearLayout() {

        // Gets linearlayout
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.editwordlistLinearLayout);
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = (int) (getScreenHeight()*0.60);
        params.width = (int) (getScreenWidth()*0.60);
        layout.setLayoutParams(params);

    }

    private int showList(Cursor c, int[] listIndex) {
        // Find layouts
        linearLayoutAnswer = (LinearLayout) rootView.findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) rootView.findViewById(R.id.questionLinearLayout);

        int i = 0;
        if (c.moveToFirst()) {
            do {
                //Define question edittext
                EditText queET = new EditText(getContext());
                queET.setId(generateViewId());
                queET.setText(c.getString(1));
                queET.setHeight(145);
                LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsQ.setMargins(0, 0, 0, 10);
                queET.setLayoutParams(paramsQ);
                queET.setGravity(8);
                queET.setPadding(30,40,0,0);
                linearLayoutQuestion.addView(queET);

                // Index edittext id's
                listIndex[i] = sNextGeneratedId.get()-1;
                i++;

                // Define answer edittext
                EditText ansET = new EditText(getContext());
                ansET.setId(generateViewId());
                ansET.setText(c.getString(2));
                ansET.setHeight(145);
                LinearLayout.LayoutParams paramsA = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsA.setMargins(0, 0, 0, 10);
                ansET.setLayoutParams(paramsA);
                ansET.setGravity(8);
                ansET.setPadding(30,40,0,0);
                linearLayoutAnswer.addView(ansET);

                // Index edittext id's
                listIndex[i] = sNextGeneratedId.get()-1;
                i++;

            } while (c.moveToNext());
        }
        return i;
    }

    public void addRow() {
        // Find layouts
        linearLayoutAnswer = (LinearLayout) rootView.findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) rootView.findViewById(R.id.questionLinearLayout);

        //Define question edittext
        EditText queET = new EditText(getContext());
        queET.setId(generateViewId());
        queET.setText("");
        queET.setHeight(145);
        LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsQ.setMargins(0, 0, 0, 10);
        queET.setLayoutParams(paramsQ);
        queET.setGravity(8);
        queET.setPadding(30,40,0,0);
        linearLayoutQuestion.addView(queET);

        // Index edittext id's
        listIndex[entryAmount] = sNextGeneratedId.get()-1;
        entryAmount++;

        // Define answer edittext
        EditText ansET = new EditText(getContext());
        ansET.setId(generateViewId());
        ansET.setText("");
        ansET.setHeight(145);
        LinearLayout.LayoutParams paramsA = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsA.setMargins(0, 0, 0, 10);
        ansET.setLayoutParams(paramsA);
        ansET.setGravity(8);
        ansET.setPadding(30,40,0,0);
        linearLayoutAnswer.addView(ansET);

        // Index edittext id's
        listIndex[entryAmount] = sNextGeneratedId.get()-1;
        entryAmount++;
    }

    public void removeRow() {
        // Find layouts
        linearLayoutAnswer = (LinearLayout) rootView.findViewById(R.id.answerLinearLayout);
        linearLayoutQuestion = (LinearLayout) rootView.findViewById(R.id.questionLinearLayout);

        //Define question edittext
        EditText queET = (EditText) rootView.findViewById(listIndex[entryAmount-1]);
        queET.setVisibility(View.GONE);

        // Remove EditText ID's
        listIndex[entryAmount] = 0;
        entryAmount--;

        // Define answer edittext
        EditText ansET = (EditText) rootView.findViewById(listIndex[entryAmount-1]);
        ansET.setVisibility(View.GONE);

        // Remove EditText ID's
        listIndex[entryAmount] = 0;
        entryAmount--;
    }

    public void updateList() {
        dbAdapter.deleteAll(table_name);
        dbAdapter.deleteRowMain(table_name);

        table_name = tableNameET.getText().toString();
        dbAdapter.createTable(table_name);
        if (!dbAdapter.existsMain(table_name)) {
            dbAdapter.insertRowMain(table_name);
        } else {
            dbAdapter.deleteAll(table_name);
        }

        int i = 0;
        do {
            EditText queET = (EditText) rootView.findViewById(listIndex[i]);
            String question = queET.getText().toString();
            i++;
            EditText ansET = (EditText) rootView.findViewById((listIndex[i]));
            String answer = ansET.getText().toString();
            i++;

            dbAdapter.insertRow(question, answer, table_name);
        } while (i < entryAmount);
    }

    public void questionTheList() {
        Intent i = new Intent(getActivity(), question.class);
        i.putExtra("tableName", table_name);
        startActivity(i);
    }

    public void displayListButton() {
        Intent i = new Intent(getActivity(), displayListACTIVITY.class);
        startActivity(i);
    }
}
