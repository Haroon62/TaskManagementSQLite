package com.example.taskmanagementsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class signup extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnSignup;
    TextView textView;

    private UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
            textView=findViewById(R.id.gologin);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });

        userDAO = new UserDAO(this);
        userDAO.open();

       btnSignup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String username = etUsername.getText().toString();
               String password = etPassword.getText().toString();

               // Perform validation if needed

               // Insert the new user into the database
               User newUser = new User(username, password);
               long result = userDAO.insertUser(newUser);

               if (result != -1) {
                   // User registration successful
                   Intent intent = new Intent(signup.this, login.class);
                   startActivity(intent);
                   finish();
                   Toast.makeText(signup.this, "Signup successful", Toast.LENGTH_SHORT).show();
                   // Add your code to navigate to the login activity or
               }
           }
       });

    }
}