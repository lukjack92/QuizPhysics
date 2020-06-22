package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizActivity extends AppCompatActivity {

    private Button buttonQuit, buttonNext, buttonPrev, buttonSubmit;
    private TextView username, question, number;
    private JSONArray objQuestions;
    private int counter, numberQuestions = 0;
    private RadioGroup groupAnswer;
    private RadioButton rBA, rBB, rBC, rBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        buttonQuit = findViewById(R.id.buttonQuit);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrev = findViewById(R.id.buttonPrev);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        question = findViewById(R.id.question);
        number = findViewById(R.id.number);
        username = findViewById(R.id.username);
        groupAnswer = findViewById(R.id.answerGroup);
        rBA = findViewById(R.id.radioButtonA);
        rBB = findViewById(R.id.radioButtonB);
        rBC = findViewById(R.id.radioButtonC);
        rBD = findViewById(R.id.radioButtonD);

        username.setText(LoginActivity.username.getString("email"));
        question.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String questions = intent.getStringExtra("QUESTIONS");

        Log.d("QuestionFromQuizActivity1: ", questions.toString());

        // Convert String from URL to JSONObject
        try {
            JSONObject jsonObject = new JSONObject(questions);
            objQuestions = jsonObject.getJSONArray("message");
            System.out.println("ArrayLength: " + objQuestions.length());
            numberQuestions = objQuestions.length();
            System.out.println("Array0: " + objQuestions.getJSONObject(counter));

            // Set the first question in the Activity QuizActivity
            number.setText(Html.fromHtml("<b>"  +counter+ " of " + numberQuestions+ "</b>"));
            question.setText(objQuestions.getJSONObject(counter).getString("question"));
            rBA.setText(objQuestions.getJSONObject(counter).getString("ansa"));
            rBB.setText(objQuestions.getJSONObject(counter).getString("ansb"));
            rBC.setText(objQuestions.getJSONObject(counter).getString("ansc"));
            rBD.setText(objQuestions.getJSONObject(counter).getString("ansd"));
            buttonPrev.setVisibility(View.INVISIBLE);
            System.out.println("Counter0: " + counter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Handling to button Next
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    groupAnswer.clearCheck();
                    counter++;
                    System.out.println("Counter2: " + counter);

                    if (counter == numberQuestions-1) {
                        number.setText(Html.fromHtml("<b>"  +counter+ " of " + numberQuestions+ "</b>"));
                        question.setText(objQuestions.getJSONObject(counter).getString("question"));
                        rBA.setText(objQuestions.getJSONObject(counter).getString("ansa"));
                        rBB.setText(objQuestions.getJSONObject(counter).getString("ansb"));
                        rBC.setText(objQuestions.getJSONObject(counter).getString("ansc"));
                        rBD.setText(objQuestions.getJSONObject(counter).getString("ansd"));

                        buttonSubmit.setVisibility(View.VISIBLE);
                        buttonNext.setVisibility(View.INVISIBLE);

                    } else if(counter < numberQuestions) {
                        number.setText(Html.fromHtml("<b>"  +counter+ " of " + numberQuestions+ "</b>"));
                        question.setText(objQuestions.getJSONObject(counter).getString("question"));
                        rBA.setText(objQuestions.getJSONObject(counter).getString("ansa"));
                        rBB.setText(objQuestions.getJSONObject(counter).getString("ansb"));
                        rBC.setText(objQuestions.getJSONObject(counter).getString("ansc"));
                        rBD.setText(objQuestions.getJSONObject(counter).getString("ansd"));

                        buttonNext.setVisibility(View.VISIBLE);
                        buttonPrev.setVisibility(View.VISIBLE);
                    }

                    //Zapis do tablicy
                    //RadioButton answerUser = findViewById(groupAnswer.getCheckedRadioButtonId());
                    //String ansText = answerUser.getText().toString();

                    if(groupAnswer.getCheckedRadioButtonId()==-1) {
                        Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Handling to button Prev
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    groupAnswer.clearCheck();
                    counter--;
                    System.out.println("Counter3: " + counter);

                    if(counter <= 0) {
                        System.out.println("CounterZero: " + counter);
                        number.setText(Html.fromHtml("<b>"  +counter+ " of " + numberQuestions+ "</b>"));
                        question.setText(objQuestions.getJSONObject(counter).getString("question"));
                        rBA.setText(objQuestions.getJSONObject(counter).getString("ansa"));
                        rBB.setText(objQuestions.getJSONObject(counter).getString("ansb"));
                        rBC.setText(objQuestions.getJSONObject(counter).getString("ansc"));
                        rBD.setText(objQuestions.getJSONObject(counter).getString("ansd"));

                        buttonPrev.setVisibility(View.INVISIBLE);
                        buttonNext.setVisibility(View.VISIBLE);

                    } else if(counter < numberQuestions)
                    {
                        number.setText(Html.fromHtml("<b>"  +counter+ " of " + numberQuestions+ "</b>"));
                        question.setText(objQuestions.getJSONObject(counter).getString("question"));
                        rBA.setText(objQuestions.getJSONObject(counter).getString("ansa"));
                        rBB.setText(objQuestions.getJSONObject(counter).getString("ansb"));
                        rBC.setText(objQuestions.getJSONObject(counter).getString("ansc"));
                        rBD.setText(objQuestions.getJSONObject(counter).getString("ansd"));

                        buttonSubmit.setVisibility(View.INVISIBLE);
                        buttonNext.setVisibility(View.VISIBLE);
                        buttonPrev.setVisibility(View.VISIBLE);
                    }

                    //Zapis do tablicy
                    //RadioButton answerUser = findViewById(groupAnswer.getCheckedRadioButtonId());
                    //String ansText = answerUser.getText().toString();

                    if(groupAnswer.getCheckedRadioButtonId()==-1) {
                        Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        // Handling to button Back
        buttonQuit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),DisplayMessageActivity.class);
               startActivity(intent);
           }
        });

        // Handling to button Submit
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                startActivity(intent);
            }
        });
    }
}
