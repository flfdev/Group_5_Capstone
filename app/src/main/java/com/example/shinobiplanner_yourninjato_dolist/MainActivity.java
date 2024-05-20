package com.example.shinobiplanner_yourninjato_dolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {

    EditText UserName, PassWord;
    Button SignIn, SignUp;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserName = (EditText) findViewById(R.id.UserName);
        PassWord = (EditText) findViewById(R.id.PassWord);

        SignIn = (Button) findViewById(R.id.Signin);
        SignUp = (Button) findViewById(R.id.SignUp);


        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserName.getText().toString().equals("admin") && PassWord.getText().toString().equals("admin")) {
                    Intent intent = new Intent(MainActivity.this, MainAppPage.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"Log In Failed",Toast.LENGTH_SHORT).show();
                }

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });


    }
}