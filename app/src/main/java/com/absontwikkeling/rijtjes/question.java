package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add current score indicator
// TODO: Change GOED/FOUT-feedback
// TODO: Add change strings to @String rescources

public class question extends AppCompatActivity {

    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;
    public static int[] settings = new int[3];
    public static int currentQuestion;
    public static int amountCorrect;
    public static String[][] wordList;
    public static String table_name;
    public static boolean capitals;
    public static boolean whitespace;
    public static int defaultDirection;

    public ConstraintLayout conLayout;

    public EditText inputString;
    public TextView question;
    public TextView feedback;
    public TextView grade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        currentQuestion = 0;
        amountCorrect = 0;
        openDB();

        question = (TextView) findViewById(R.id.questionTextView);
        inputString = (EditText) findViewById(R.id.inputString);
        feedback = (TextView) findViewById(R.id.GFFeedback);
        grade = (TextView) findViewById(R.id.grade);

        // Gets information from intent
        Intent i = getIntent();
        table_name = i.getStringExtra("tableName");
        settings = i.getIntArrayExtra("settings");

        //Translates settings from integer to relevant data-type
        capitals = settings[0] == 1;
        whitespace = settings[1] == 1;
        defaultDirection = settings[2];

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
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();

        amountCorrect = 0;
    }

    private void openDB() {
        dbAdapter = new DBAdapter(this, DBAdapter.DATABASE_NAME, DBAdapter.DATABASE_VERSION);
        dbAdapter.open();
        dbAdapterMain = new DBAdapter(this, DBAdapter.DATABASE_MAIN_NAME, DBAdapter.DATABASE_MAIN_VERSION);
        dbAdapterMain.open();
    }

    private void closeDB() {
        dbAdapter.close();
        dbAdapterMain.close();
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
        int integer = 0;
        if (defaultDirection == 0) {
            integer++;
        }

        if (wordList[0][currentQuestion-1] != null) {
            question.setText("Vraag #" + currentQuestion + ": " + wordList[integer][currentQuestion-1]);
            double d = setGrade();
            grade.setText("Jouw huidige cijfer is: " + d);
        } else {
            double d = setGrade();
            grade.setText("Jouw cijfer is: " + d);
            Button b = (Button) findViewById(R.id.checkAnswer);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(question.this, results.class);
                    int[] scorePoints = new int[2];
                    scorePoints[0] = currentQuestion-1;
                    scorePoints[1] = amountCorrect;
                    i.putExtra("scorePoints", scorePoints);
                    startActivity(i);
                }
            });
            b.setText("Naar resultaten");
            question.setText("Einde toets");
            inputString.setVisibility(View.GONE);

        }
    }

    public void wasCorrect(View v) {
        Button b = (Button) findViewById(R.id.wasCorrect);
        b.setVisibility(View.INVISIBLE);
        amountCorrect++;
        double d = setGrade();
        grade.setText("Jouw huidige cijfer is: " + d);
        feedback.setText("Jouw antwoord was goed!");
        feedback.setTextColor(Color.GREEN);
    }

    public double setGrade() {
        int i = amountCorrect;
        int j = currentQuestion-1;
        double temp = (double)i/j;
        double score = temp*9d+1d;
        return (double)Math.round(score * 10d)/10d;
    }

    // Sets up the listener for the button, which checks the answer and does nothing.
    private void setupCheckAnswerButton(){
        Button ansButton = (Button) findViewById(R.id.checkAnswer);
        ansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (checkAnswer(inputString.getText().toString(), wordList[defaultDirection][currentQuestion-1], capitals, whitespace)) {
                    amountCorrect++;
                    feedback.setText("Jouw antwoord was goed!");
                    feedback.setTextColor(Color.GREEN);
                    nextQuestion();
                } else {
                    feedback.setText("Het goede antwoord was: '" + wordList[defaultDirection][currentQuestion-1] + "'");
                    feedback.setTextColor(Color.RED);
                    nextQuestion();
                    Button b = (Button) findViewById(R.id.wasCorrect);
                    b.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
