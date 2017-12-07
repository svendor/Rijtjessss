package layout;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.R;
import com.absontwikkeling.rijtjes.displayListACTIVITY;
import com.absontwikkeling.rijtjes.questionObj;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_list, container, false);
    }

    TextView debugTV;
    EditText questionWord;
    EditText answerWord;
    EditText tableName;
    DBAdapter dbAdapter;

    // TODO Java fixen voor het createList fragment
    /** DOET HET NIET
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        openDB();
        tableName = (EditText) findViewById(R.id.dataBaseName);
        questionWord = (EditText) findViewById(R.id.questionWord);
        answerWord = (EditText) findViewById(R.id.answerWord);
    }
     */

    /*
    // TODO FIX die Java
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
    }

    private void closeDB() {
        dbAdapter.close();
    }

    private void addQuestion(questionObj object) {
        final String QUESTION = object.get_question();
        final String ANSWER = object.get_answer();
        final String TABLE_NAME = object.get_table_name();
        dbAdapter.createTable(TABLE_NAME);
        long questionID = dbAdapter.insertRow(QUESTION, ANSWER, TABLE_NAME);

        Cursor c = dbAdapter.getRow(questionID);
        String text = displayQuery(dbAdapter.getAllRows(TABLE_NAME)) + TABLE_NAME;
        debugTV.setText(text);
    }

    public void finishList(View v) {
        Intent i = new Intent(this, displayListACTIVITY.class);
        dbAdapter.createTableMain();
        String table_name = tableName.getText().toString();
        dbAdapter.createTable(table_name);

        if (!dbAdapter.existsMain(table_name)) {
            dbAdapter.insertRowMain(table_name);
        }

        startActivity(i);
    }

    public void nextQuestion(View v) {
        questionObj queOb = new questionObj(questionWord.getText().toString(), answerWord.getText().toString(), tableName.getText().toString());
        questionWord.setText("");
        answerWord.setText("");
        addQuestion(queOb);
    }

    public void onClick_deleteCurrentList(View v) {
        dbAdapter.deleteAll(tableName.getText().toString());
        dbAdapter.deleteRowMain(tableName.getText().toString());
        Intent i = new Intent(this, displayListACTIVITY.class);
        startActivity(i);
    }

    private String displayQuery(Cursor cursor) {
        String message = "";
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String question = cursor.getString(DBAdapter.COL_QUESTION);
                String answer = cursor.getString(DBAdapter.COL_ANSWER);

                // Append data to the message:
                message += "Vraag #" + id
                        +", Vraag = " + question
                        +", Antwoord = " + answer
                        +"\n";
            } while(cursor.moveToNext());
        }

        cursor.close();
        return message;
    }
    */
}
