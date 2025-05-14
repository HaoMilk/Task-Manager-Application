package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        Button btnUpdate = findViewById(R.id.btnUpdateAccount);
        btnUpdate.setOnClickListener(view ->{
            Intent intent = new Intent(Account.this, UpdateAccount.class);
            startActivity(intent);
        });


    }
}