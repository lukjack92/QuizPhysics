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

import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {

    public static Bundle resultQuiz = new Bundle();
    public static Bundle numberOfQuiz = new Bundle();
    private Button buttonQuit, buttonNext, buttonPrev, buttonSubmit;
    private TextView username, question, number;
    private JSONArray objQuestions;
    private int numberQuestions = 0;
    private int counter = 1;
    private RadioGroup groupAnswer;
    private RadioButton rBA, rBB, rBC, rBD;
    private String answersStringArray[];
    private int answersRadioButtonArray[];


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),DisplayMessageActivity.class);
        startActivity(intent);
    }

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

        // Convert String from URL to JSONObject
        try {
            JSONObject jsonObject = new JSONObject(questions);
            objQuestions = jsonObject.getJSONArray("message");
            System.out.println("ArrayLength: " + objQuestions.length());
            numberQuestions = objQuestions.length();
            answersStringArray = new String[numberQuestions];
            answersRadioButtonArray = new int[numberQuestions];

            System.out.println("Array0: " + objQuestions.getJSONObject(counter-1));

            // Set the first question in the Activity QuizActivity
            number.setText(Html.fromHtml("<b>"  + counter +" of " + numberQuestions+ "</b>"));
            question.setText(objQuestions.getJSONObject(counter-1).getString("question"));
            rBA.setText(objQuestions.getJSONObject(counter-1).getString("ansa"));
            rBB.setText(objQuestions.getJSONObject(counter-1).getString("ansb"));
            rBC.setText(objQuestions.getJSONObject(counter-1).getString("ansc"));
            rBD.setText(objQuestions.getJSONObject(counter-1).getString("ansd"));
            buttonPrev.setVisibility(View.INVISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Handling to button Next
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //Log.d("Checked Radio Button", String.valueOf(groupAnswer.getCheckedRadioButtonId()));
                    if(groupAnswer.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(groupAnswer.getCheckedRadioButtonId() != -1) {
                        RadioButton answer = findViewById(groupAnswer.getCheckedRadioButtonId());
                        String ansText = answer.getText().toString();
                        answersStringArray[counter-1] = ansText;
                        answersRadioButtonArray[counter-1] = groupAnswer.getCheckedRadioButtonId();
                        System.out.println("answersStringArray" + Arrays.toString(answersStringArray));
                        System.out.println("answersRadioButtonArray" + Arrays.toString(answersRadioButtonArray));
                    }
                    groupAnswer.clearCheck();
                    counter++;
                    findRadioButton(answersRadioButtonArray, counter-1);
                    if (counter == numberQuestions) {
                        number.setText(Html.fromHtml("<b>"  + counter + " of " + numberQuestions+ "</b>"));
                        question.setText(objQuestions.getJSONObject(counter-1).getString("question"));
                        rBA.setText(objQuestions.getJSONObject(counter-1).getString("ansa"));
                        rBB.setText(objQuestions.getJSONObject(counter-1).getString("ansb"));
                        rBC.setText(objQuestions.getJSONObject(counter-1).getString("ansc"));
                        rBD.setText(objQuestions.getJSONObject(counter-1).getString("ansd"));

                        //Log.d("Counter: ", String.valueOf(counter));

                        buttonSubmit.setVisibility(View.VISIBLE);
                        buttonNext.setVisibility(View.INVISIBLE);

                    } else if(counter <= numberQuestions) {
                        number.setText(Html.fromHtml("<b>"  +counter+ " of " + numberQuestions+ "</b>"));
                        question.setText(objQuestions.getJSONObject(counter-1).getString("question"));
                        rBA.setText(objQuestions.getJSONObject(counter-1).getString("ansa"));
                        rBB.setText(objQuestions.getJSONObject(counter-1).getString("ansb"));
                        rBC.setText(objQuestions.getJSONObject(counter-1).getString("ansc"));
                        rBD.setText(objQuestions.getJSONObject(counter-1).getString("ansd"));

                        buttonNext.setVisibility(View.VISIBLE);
                        buttonPrev.setVisibility(View.VISIBLE);
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
                    if(groupAnswer.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    answersRadioButtonArray[counter-1] = groupAnswer.getCheckedRadioButtonId();
                    groupAnswer.clearCheck();
                    counter--;
                    findRadioButton(answersRadioButtonArray, counter-1);
                    if(counter == 1) {
                        number.setText(Html.fromHtml("<b>"  +counter+ " of " + numberQuestions+ "</b>"));
                        question.setText(objQuestions.getJSONObject(counter-1).getString("question"));
                        rBA.setText(objQuestions.getJSONObject(counter-1).getString("ansa"));
                        rBB.setText(objQuestions.getJSONObject(counter-1).getString("ansb"));
                        rBC.setText(objQuestions.getJSONObject(counter-1).getString("ansc"));
                        rBD.setText(objQuestions.getJSONObject(counter-1).getString("ansd"));

                        buttonPrev.setVisibility(View.INVISIBLE);
                        buttonNext.setVisibility(View.VISIBLE);

                    } else if(counter <= numberQuestions) {
                        number.setText(Html.fromHtml("<b>"  +counter+ " of " + numberQuestions+ "</b>"));
                        question.setText(objQuestions.getJSONObject(counter-1).getString("question"));
                        rBA.setText(objQuestions.getJSONObject(counter-1).getString("ansa"));
                        rBB.setText(objQuestions.getJSONObject(counter-1).getString("ansb"));
                        rBC.setText(objQuestions.getJSONObject(counter-1).getString("ansc"));
                        rBD.setText(objQuestions.getJSONObject(counter-1).getString("ansd"));

                        buttonSubmit.setVisibility(View.INVISIBLE);
                        buttonNext.setVisibility(View.VISIBLE);
                        buttonPrev.setVisibility(View.VISIBLE);
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
            int numberCorrectQuestion = 0;
            @Override
            public void onClick(View v) {
                if(groupAnswer.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(groupAnswer.getCheckedRadioButtonId() != -1) {
                    RadioButton answer = findViewById(groupAnswer.getCheckedRadioButtonId());
                    String ansText = answer.getText().toString();
                    answersStringArray[counter-1] = ansText;
                }

                // Check the result of the quiz
                for(int i = 0; i < numberQuestions; i++) {
                    try {
                        String answerFromObject = objQuestions.getJSONObject(i).getString("odp");
                        Log.d("FetchAnswerFromObject: ", answerFromObject);
                        String correctAnswer = objQuestions.getJSONObject(i).getString(answerFromObject);
                        Log.d("CorrectAnswer: ", correctAnswer);
                        if(correctAnswer.equals(answersStringArray[i])) {
                            numberCorrectQuestion++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String result = String.valueOf(numberCorrectQuestion);
                //Log.d("ResultQuiz: ", String.valueOf(numberCorrectQuestion));

                resultQuiz.putString("resultQuiz", result);
                numberOfQuiz.putString("numberOfQuiz", String.valueOf(numberQuestions));
                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findRadioButton(int arrayRadioButton[], int counter) {
        //System.out.println("To select: " + arrayRadioButton[counter]);
        switch(arrayRadioButton[counter]) {
            case R.id.radioButtonA:
                RadioButton rbA = findViewById(R.id.radioButtonA);
                rbA.setChecked(true);
                break;
            case R.id.radioButtonB:
                RadioButton rbB = findViewById(R.id.radioButtonB);
                rbB.setChecked(true);
                break;
            case R.id.radioButtonC:
                RadioButton rbC = findViewById(R.id.radioButtonC);
                rbC.setChecked(true);
                break;
            case R.id.radioButtonD:
                RadioButton rbD = findViewById(R.id.radioButtonD);
                rbD.setChecked(true);
                break;
        }
    }
}
