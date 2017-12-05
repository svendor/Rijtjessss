package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

public class results extends AppCompatActivity {

    public static int correctAmount;
    public static int questionAmount;
    public static int[] scorePoints;
    public static double score;

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Gets information passed by previous activity
        Intent i = getIntent();

        // Defines array in which the information will be stored, and stores it
        scorePoints = new int[2];
        scorePoints = i.getIntArrayExtra("scorePoints");

        // Stores the information in separate variables
        questionAmount = scorePoints[0];
        correctAmount = scorePoints[1];

        // Performs simple arithmetic to determine a score and rounds it down to
        score = (9*correctAmount/questionAmount)+1d;
        score = (double)Math.round(score * 100d)/100d;

        // Prints score
        result = (TextView) findViewById(R.id.result);
        result.setText("Je hebt een " + score);


    }

    @Override
    public void onBackPressed() {

    }

    public void displayListButton(View v) {
        Intent i = new Intent(this, displayList.class);
        startActivity(i);
    }

    public void mainButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
