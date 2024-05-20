package com.example.shinobiplanner_yourninjato_dolist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    EditText Username,Email,Password;

    Button Signup;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Username = (EditText) findViewById(R.id.UpUsername);
        Email = (EditText) findViewById(R.id.UpEmail);
        Password = (EditText) findViewById(R.id.UpPassword);

        Signup = (Button) findViewById(R.id.UpSignUp);



        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });




    }
}