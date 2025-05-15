package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.AuthApi;
import com.example.taskmanagerfrontend.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    public static String UserID;
    public static String UserName;

    private EditText username, password;
    private Button loginBtn;
    private TextView txtSignup;

    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        loginBtn = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignup);

        // Khởi tạo Retrofit API sử dụng baseUrl trong ApiClient (không cần truyền baseUrl)
        authApi = ApiClient.getRetrofitInstance().create(AuthApi.class);  // Không cần truyền baseUrl

        loginBtn.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString();
            String enteredPassword = password.getText().toString();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập cả tên người dùng và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            User loginUser = new User(enteredUsername, enteredPassword);

            Call<Map<String, String>> call = authApi.loginUser(loginUser);
            call.enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Map<String, String> res = response.body();
                        Toast.makeText(Login.this, res.get("message"), Toast.LENGTH_SHORT).show();

                        if ("Login successful".equals(res.get("message"))) {
                            String token = res.get("token");
                            UserID = res.get("userId"); // Giả sử API trả về userId

                            // Lưu id người dùng và token vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", UserID);  // Lưu id người dùng
                            editor.putString("token", token);// Lưu token
                            editor.putString("username", UserName);  // Lưu username")
                            editor.apply();  // Lưu thay đổi

                            // Chuyển sang màn hình Home
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(Login.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(Login.this, "Lỗi mạng hoặc server", Toast.LENGTH_SHORT).show();
                }
            });
        });

        txtSignup.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });
    }
}
