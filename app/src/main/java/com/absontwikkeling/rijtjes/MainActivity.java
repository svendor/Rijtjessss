package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private void setupQuestionButtonListener(){
        Button questionButton = (Button) findViewById(R.id.createListButton);
        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent addQuestion = new Intent(MainActivity.this, createList.class);
                startActivity(addQuestion);
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
