package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupQuestionButtonListener();
    }

    // Creates button
    private void setupQuestionButtonListener(){
        Button b = (Button) findViewById(R.id.createListButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent addQuestion = new Intent(MainActivity.this, createList.class);
                startActivity(addQuestion);
            }
        });
    }

}

// added to push merge 21-10-2017 16:16