package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private void setupQuestionButtonListener(){
        Button questionButton = (Button) findViewById(R.id.questionLink);
        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intQuestion = new Intent(MainActivity.this, question.class);
                startActivity(intQuestion);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupQuestionButtonListener();
    }
}

// Sven:
// I want to die
// Again
// Time to test branches

// Arun:
// TEST plz goed nu

// Bassie.c:
// This is a 1,2,3,test