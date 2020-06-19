package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
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

public class DisplayMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<String> categories;
    private Spinner spinner;
    RequestBody body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra("MESSAGE_LOGIN");

        TextView textView = findViewById(R.id.textView);
        textView.setText(message);

        categories = new ArrayList<String>();

        Log.d("LOGIN", "TEST!!!");
        spinner = findViewById(R.id.spinner);

        RequestParams params = new RequestParams();
        params.add("type","category");

        body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), params.toString());
        postRequest(MainActivity.postURIStage, body);


        // Spinner Drop down elements
        //List<String> categories = new ArrayList<String>();
        //categories.add("Automobile");
        //categories.add("Business Services");
        //categories.add("");

        // Creating adapter for spinner
        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attaching data adapter to spinner
        //spinner.setAdapter(dataAdapter);
        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
                            Log.d("Message", "Message form the server : " + jsonResponse.getJSONArray("message"));
                                try {
                                    // Parse categories from api.php
                                    JSONArray categoriesArray = jsonResponse.getJSONArray("message");
                                    // Lopping through all categories
                                    for (int i = 0; i < categoriesArray.length(); i++) {
                                        JSONObject c = categoriesArray.getJSONObject(i);
                                        String name = c.getString("name");
                                        categories.add(name);

                                        // Creating adapter for spinner
                                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DisplayMessageActivity.this,android.R.layout.simple_spinner_item, categories);

                                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        // Attaching data adapter to spinner
                                        spinner.setAdapter(dataAdapter);
                                        // Spinner click listener
                                        spinner.setOnItemSelectedListener(DisplayMessageActivity.this);
                                    }
                                } catch (final JSONException e) {
                                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                                }

                        } catch (Exception e) {
                            e.printStackTrace();
                            responseTextLogin.setText("No categories as active state in DB!");
                        }
                    }
                });
            }
        });
    }


    public void btnSTART(View view) {
        TextView editText = findViewById(R.id.textView);
        Spinner select = findViewById(R.id.spinner);
        editText.setText(select.getSelectedItem().toString());
    }

    public void btnBACK(View view) {
        Intent intent = new Intent(this, LoginActivity.class);

    }
}
