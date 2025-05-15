package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagerfrontend.api.ApiClient;
import com.example.taskmanagerfrontend.api.AuthApi;
import com.example.taskmanagerfrontend.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private EditText username, email, firstname, lastname, password;
    private Button signupBtn;
    private ImageView avatar, setimgAvatar;

    private AuthApi authApi;
    private Uri selectedImageUri;

    // Dùng ActivityResultLauncher để chọn ảnh
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        try {
                            Bitmap bitmap;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), selectedImageUri);
                                bitmap = ImageDecoder.decodeBitmap(source);
                            } else {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            }
                            avatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Khởi tạo retrofit
        authApi = ApiClient.getRetrofitInstance().create(AuthApi.class);

        // Gán View
        username = findViewById(R.id.txtUsername);
        email = findViewById(R.id.txtEmail);
        firstname = findViewById(R.id.txtFirstName);
        lastname = findViewById(R.id.txtLastName);
        password = findViewById(R.id.txtPassword);
        signupBtn = findViewById(R.id.btnUpdate);
        avatar = findViewById(R.id.imgAvatar);
        setimgAvatar = findViewById(R.id.set_imgAvatar);

        // Click chọn ảnh
        setimgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Click đăng ký
        signupBtn.setOnClickListener(v -> {
            String userName = username.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userFirstName = firstname.getText().toString().trim();
            String userLastName = lastname.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String imgUrl = selectedImageUri != null ? selectedImageUri.toString() : "abc";  // Chuyển Uri thành String

            // Kiểm tra các trường thông tin
            if (userName.isEmpty() || userEmail.isEmpty() || userFirstName.isEmpty() || userLastName.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng User để gửi lên server
            User newUser = new User(userName, userEmail, userPassword, userFirstName, userLastName, imgUrl);

            // Gửi yêu cầu đăng ký đến server backend
            Call<String> call = authApi.registerUser(newUser);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUp.this, "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
