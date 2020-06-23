package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    public static Bundle username = new Bundle();
    public static final String postURILive = "https://mgr.ljack.com.pl/api/api.php";
    public static final String postURIStage = "https://stage.mgr.ljack.com.pl/api/api.php";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);
    }

    public void changeActivityToSelectCategory(String message) {
        //Intent intentQuiz = new Intent(this, QuizActivity.class);
        //intentQuiz.putExtra("USER", username);

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra("MESSAGE_LOGIN", message);
        startActivity(intent);
    }

    public void submitLogin(View view) throws Exception {
        EditText usernameView = findViewById(R.id.loginUsername);
        EditText passwordView = findViewById(R.id.loginPassword);

        String username = usernameView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();

        if(username.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(),"Type Email and Password", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        JSONObject loginForm = new JSONObject();
        try {
            loginForm.put("type", "login");
            loginForm.put("email", username);
            loginForm.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        RequestParams params = new RequestParams();
        params.add("type","login");
        params.add("email",username);
        params.add("password",password);

        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), params.toString());
        postRequest(LoginActivity.postURIStage, body);
    }

    public void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                //.header("Accept", "application/x-www-form-urlencoded")
                //.header("Content-Type", "application/x-www-form-urlencoded")
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
                        TextView responseTextLogin = findViewById(R.id.responseTextLogin);
                        try {
                            String loginResponseString = response.body().string();
                            //JSONObject user = response.getJSONObject("user");
                            JSONObject jsonResponse = new JSONObject(loginResponseString);
                            Log.d("Message", "Message form the server : " + jsonResponse.getString("message").trim());
                            Log.d("LOGIN", "Response from the server : " + loginResponseString);
                            if (jsonResponse.getString("message").trim().equals("Successfully logged in.")) {
                                Log.d("LOGIN", "Successful Login");
                                //TextView responseTextLoginToScreen = findViewById(R.id.textView);
                                //responseTextLoginToScreen.setText(jsonResponse.getString("message").trim());
                                JSONObject user = jsonResponse.getJSONObject("user");
                                String email = user.getString("email");
                                Log.d("USER", email);
                                LoginActivity.username.putString("email",email);
                                changeActivityToSelectCategory("You have been success logged in.");
                                //finish();//finishing activity and return to the calling activity.
                            } else if (jsonResponse.getString("message").trim().equals("User not found or Invalid login details.")) {
                                responseTextLogin.setText("Login Failed. Invalid username or password.");
                                Log.d("LOGIN", "Unsuccessful Login");
                            } else {
                                responseTextLogin.setText(loginResponseString);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseTextLogin.setText("The page is unreachable. Please try soon.");
                        }
                    }
                });
            }
        });
    }
}