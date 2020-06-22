package com.example.myfirstapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    public static final String postURILive = "https://mgr.ljack.com.pl/api/api.php";
    public static final String postURIStage = "https://stage.mgr.ljack.com.pl/api/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_login_ui);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
    }
/*
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
 */
}