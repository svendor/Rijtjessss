package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class createList extends AppCompatActivity {

    private void finishListButtonListener(){
        Button questionButton = (Button) findViewById(R.id.finishList);
        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intQuestion = new Intent(createList.this, question.class);
                startActivity(intQuestion);
            }
        });
    }

    private void nextQuestionButtonListener(){
        Button nextQuestionButton = (Button) findViewById(R.id.nextQuestion);
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        finishListButtonListener();
        nextQuestionButtonListener();
    }
}