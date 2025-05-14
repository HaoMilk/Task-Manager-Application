package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    // Khai báo các đối tượng EditText và Button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Liên kết các đối tượng trong layout với mã Java
        EditText username = findViewById(R.id.txtUsername);
        EditText password = findViewById(R.id.txtPassword);
        Button loginBtn = findViewById(R.id.btnLogin);
        TextView txtSignup = findViewById(R.id.txtSignup);

        // Thiết lập sự kiện khi người dùng nhấn nút đăng nhập
        loginBtn.setOnClickListener(view -> {
            // Lấy giá trị người dùng nhập vào từ EditText
            String enteredUsername = username.getText().toString();
            String enteredPassword = password.getText().toString();

//            // Kiểm tra nếu tên người dùng hoặc mật khẩu rỗng
//            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
//                // Hiển thị thông báo yêu cầu người dùng nhập đủ thông tin
//                Toast.makeText(Login.this, "Vui lòng nhập cả tên người dùng và mật khẩu", Toast.LENGTH_SHORT).show();
//            } else {
//
//            }
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
        });

        txtSignup.setOnClickListener(view ->{
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
                }
        );


    }
}
