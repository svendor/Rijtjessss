package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class createList extends AppCompatActivity {

    TextView debugTV;
    EditText questionWord;
    EditText answerWord;
    EditText tableName;
    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        openDB();
        debugTV = (TextView) findViewById(R.id.textView4);
        tableName = (EditText) findViewById(R.id.dataBaseName);
        questionWord = (EditText) findViewById(R.id.questionWord);
        answerWord = (EditText) findViewById(R.id.answerWord);
    }

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
        Intent i = new Intent(this, displayList.class);
        dbAdapter.createTableMain();
        String table_name = tableName.getText().toString();

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
        Intent i = new Intent(this, displayList.class);
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

}