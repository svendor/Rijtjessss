package com.absontwikkeling.rijtjes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class createList extends AppCompatActivity {

    private void inputToWordList(final wordList wl) {
        EditText questionWord = (EditText) findViewById(R.id.questionWord);
        EditText answerWord = (EditText) findViewById(R.id.answerWord);
        wl.newWord(wl.wordAmount, questionWord.getText().toString(), answerWord.getText().toString());
    }

    private void setupQuestionButtonListener(final wordList wl){
        Button questionButton = (Button) findViewById(R.id.questionLink);
        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intQuestion = new Intent(createList.this, question.class);
                //inputToWordList(wl);
                //intQuestion.putExtra("list", wl);
                startActivity(intQuestion);
            }
        });
    }

    private void setupNextQuestionListener(final wordList wl){
        Button nextQuestionButton = (Button) findViewById(R.id.nextQuestion);
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent stayHere = new Intent(createList.this, createList.class);
                //inputToWordList(wl);
                //wl.wordAmount++;
                //stayHere.putExtra("list", wl);
                startActivity(stayHere);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        wordList list;
        Intent i = getIntent();
        list = (wordList)i.getExtras().getParcelable("list");

        setupQuestionButtonListener(list);
        setupNextQuestionListener(list);
    }
}