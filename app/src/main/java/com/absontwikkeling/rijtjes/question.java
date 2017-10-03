package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class question extends AppCompatActivity {

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

    // Sets up the listener for the button, which checks the answer and does nothing.
    private void setupCheckAnswerButton(){
        Button ansButton = (Button) findViewById(R.id.checkAnswer);
        ansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // Get User's Answer
                EditText inputString = (EditText) findViewById(R.id.inputString);

                // Get Answer
                EditText answer = (EditText) findViewById(R.id.answer);

                // Get Settings for question
                CheckBox capitals = (CheckBox) findViewById(R.id.capitals);
                CheckBox whitespace = (CheckBox) findViewById(R.id.whitespace);

                if (checkAnswer(inputString.getText().toString(),answer.getText().toString(),capitals.isChecked() ,whitespace.isChecked())) {
                    // Redirect to homepage for debugging purposes
                    Intent home = new Intent(question.this, MainActivity.class);
                    startActivity(home);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        setupCheckAnswerButton();
    }
}
