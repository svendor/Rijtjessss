package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class createList extends AppCompatActivity {

    TextView debugTV;
    EditText questionWord;
    EditText answerWord;
    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        openDB();
        debugTV = (TextView) findViewById(R.id.textView4);
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
        long questionID = dbAdapter.insertRow(QUESTION, ANSWER);

        Cursor c = dbAdapter.getRow(questionID);
        debugTV.setText(displayQuery(dbAdapter.getAllRows()));
    }

    public void finishList(View v) {
        Intent i = new Intent(this, question.class);
        startActivity(i);
    }

    public void nextQuestion(View v) {
        questionObj queOb = new questionObj(questionWord.getText().toString(), answerWord.getText().toString());
        addQuestion(queOb);
    }

    public void onClick_deleteCurrentList(View v) {
        dbAdapter.deleteAll();
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