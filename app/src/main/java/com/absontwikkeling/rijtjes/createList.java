package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class createList extends AppCompatActivity {

    DBHandler dbHandler;
    TextView debugTV;
    EditText dataBaseName;
    EditText questionWord;
    EditText answerWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        dbHandler = new DBHandler(this);
        debugTV = (TextView) findViewById(R.id.textView4);
        questionWord = (EditText) findViewById(R.id.questionWord);
        answerWord = (EditText) findViewById(R.id.answerWord);
        //printDatabase();

        //finishListButtonListener();
        //nextQuestionButtonListener();
    }

    public void finishList(View v) {
        Intent i = new Intent(this, question.class);
        startActivity(i);
    }

    public void nextQuestion(View v) {
        questionObj queOb = new questionObj(questionWord.getText().toString(), answerWord.getText().toString());

        final String QUESTION = queOb.get_question();
        final String ANSWER = queOb.get_answer();
        debugTV.setText(QUESTION + "\n" + ANSWER);

        dbHandler.newQuestion(queOb);
        // printDatabase();
    }

    public void printDatabase() {
        String dbString = dbHandler.databaseToString();
        debugTV.setText(dbString);

    }

}