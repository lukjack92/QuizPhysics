package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        RequestBody body;
        Button buttonBack = findViewById(R.id.buttonBack);
        TextView usernameResultActivity = findViewById(R.id.usernameResultActivity);
        TextView categoryView = findViewById(R.id.categoryView);
        TextView resultView = findViewById(R.id.resultView);
        String email = LoginActivity.username.getString("email");

        usernameResultActivity.setText(email);

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


        RequestParams params = new RequestParams();
        params.add("type","updateResultQuizForUser");
        params.add("email", email);
        params.add("result", resultQuiz + " of " + numberOfQuiz);
        params.add("category", categoryQuiz);

        body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), params.toString());
        postRequest(LoginActivity.postURIStage, body);
    }

    public void postRequest(String postUrl, RequestBody postBody) {
        final List<String> categories1 = new ArrayList<String>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("FAIL", e.getMessage());
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseTextLogin = findViewById(R.id.responseTextLogin);
                        responseTextLogin.setText("Server is unreachable. Please try soon.");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseTextLogin = findViewById(R.id.textView);
                        try {
                            String categoryResponseString = response.body().string();
                            Log.d("TEST:", categoryResponseString);

                            JSONObject jsonResponse = new JSONObject(categoryResponseString);
                            Log.d("Message", "Message form the server : " + jsonResponse.getString("message"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
