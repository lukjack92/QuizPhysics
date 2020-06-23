package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),DisplayMessageActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Button buttonBack = findViewById(R.id.buttonBack);
        TextView usernameResultActivity = findViewById(R.id.usernameResultActivity);
        TextView categoryView = findViewById(R.id.categoryView);
        TextView resultView = findViewById(R.id.resultView);

        usernameResultActivity.setText(LoginActivity.username.getString("email"));

        String categoryQuiz = DisplayMessageActivity.selectedCategory.getString("SELECTED_CATEGORY").toString();
        String resultQuiz = QuizActivity.resultQuiz.getString("resultQuiz").toString();
        String numberOfQuiz = QuizActivity.numberOfQuiz.get("numberOfQuiz").toString();

        // Set View with result Quiz
        categoryView.setText(categoryQuiz);
        resultView.setText(resultQuiz + " of " + numberOfQuiz);

        // Handling to button Back
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DisplayMessageActivity.class);
                startActivity(intent);
            }
        });
    }
}
