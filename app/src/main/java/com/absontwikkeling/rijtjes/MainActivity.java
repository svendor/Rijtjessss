package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent addQuestion = new Intent(MainActivity.this, NavMenu.class);
                startActivity(addQuestion);
            }
        });
    }

    public void displayListButton(View v) {
        Intent i = new Intent(this, NavMenu.class);
        startActivity(i);
    }

    public void menutest3Button(View v) {
        Intent i = new Intent(this, NavMenu.class);
        startActivity(i);
    }
}

// added to push merge 21-10-2017 16:16