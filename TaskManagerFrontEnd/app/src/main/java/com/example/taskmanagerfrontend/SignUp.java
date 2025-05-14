package com.example.taskmanagerfrontend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText firstname;
    EditText lastname;
    EditText password;
    Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.txtUsername);
        email = findViewById(R.id.txtEmail);
        firstname = findViewById(R.id.txtFirstName);
        lastname = findViewById(R.id.txtLastName);
        password = findViewById(R.id.txtPassword);
        signupBtn = findViewById(R.id.btnSignUp);

    }
}