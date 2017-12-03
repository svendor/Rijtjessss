package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class question extends AppCompatActivity {

    DBAdapter dbAdapter;
    public static int currentQuestion;
    public static int amountCorrect;
    public static String[][] wordList;
    public static String table_name;

    public ConstraintLayout conLayout;

    public TextView question;
    public EditText inputString;
    public CheckBox capitals;
    public CheckBox whitespace;
    public TextView feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        currentQuestion = 0;
        openDB();

        question = (TextView) findViewById(R.id.questionTextView);
        inputString = (EditText) findViewById(R.id.inputString);
        capitals = (CheckBox) findViewById(R.id.capitals);
        whitespace = (CheckBox) findViewById(R.id.whitespace);
        feedback = (TextView) findViewById(R.id.questionOutput);

        // Gets information from intent
        Intent i = getIntent();
        table_name = i.getStringExtra("tableName");

        // Get's query
        Cursor c = dbAdapter.getAllRows(table_name);

        // Translates query into array
        wordList = putInArray(c);

        //
        conLayout = (ConstraintLayout) findViewById(R.id.conLayout);

        // Puts first question into view
        nextQuestion();

        // Set's up the button
        setupCheckAnswerButton();
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

    public String[][] putInArray(Cursor c) {
        String[][] list = new String[2][100];

        if (c.moveToFirst()) {
            int i = 0;
            do {
                // Process the database
                String question = c.getString(DBAdapter.COL_QUESTION);
                String answer = c.getString(DBAdapter.COL_ANSWER);

                // Add data into array
                list[0][i] = question;
                list[1][i] = answer;

                i++;
            } while (c.moveToNext());
        }

        return list;
    }

    // Creates method for checking the answer. Currently both the answer and input are supplied by user.
    public static boolean checkAnswer(String inputString, String answer, boolean capitals, boolean whitespace) {

        if (!capitals) {
            inputString = inputString.toLowerCase();
            answer = answer.toLowerCase();
        }

        if (!whitespace) {
            inputString = inputString.replaceAll("\\s","");
            answer = answer.replaceAll("\\s","");
        }

        return inputString.equals(answer);
    }

    public void nextQuestion() {
        currentQuestion++;
        inputString.setText("");

        if (wordList[0][currentQuestion-1] != null) {
            question.setText("Vraag #" + currentQuestion + ": " + wordList[0][currentQuestion-1]);
        } else {
            Intent i = new Intent(this, results.class);
            int[] scorePoints = new int[2];
            scorePoints[0] = currentQuestion-1;
            scorePoints[1] = amountCorrect;
            i.putExtra("scorePoints", scorePoints);
            startActivity(i);

        }
    }

    // Sets up the listener for the button, which checks the answer and does nothing.
    private void setupCheckAnswerButton(){
        Button ansButton = (Button) findViewById(R.id.checkAnswer);
        ansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (checkAnswer(inputString.getText().toString(), wordList[1][currentQuestion-1], capitals.isChecked(), whitespace.isChecked())) {
                    amountCorrect++;
                    nextQuestion();
                    feedback.setText(amountCorrect + " goed!");
                } else {
                    nextQuestion();
                    feedback.setText("Fout!");
                }
            }
        });
    }
}
