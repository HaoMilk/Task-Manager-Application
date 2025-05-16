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

public class UpdateAccount extends AppCompatActivity {

    private ImageView imgAvatar;
    private Button btnUpdate;

    private EditText txtUsername, txtEmail, txtFirstName, txtLastName, txtPassword;

    private int userId = Integer.parseInt(Login.UserID);
    private AuthApi authApi;
    private Uri selectedImageUri;
    private String currentImageUrl; // Lưu url ảnh hiện tại (nếu không đổi ảnh thì giữ nguyên)

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
                            imgAvatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        authApi = ApiClient.getRetrofitInstance().create(AuthApi.class);

        imgAvatar = findViewById(R.id.setImgAvt);
        btnUpdate = findViewById(R.id.btnUpdate);

        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtPassword = findViewById(R.id.txtPassword);

        getUserInfo();

        imgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(v -> {
            updateUserInfo();
        });
    }

    private void getUserInfo() {
        Call<User> call = authApi.getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    txtUsername.setText(user.getUsername());
                    txtEmail.setText(user.getEmail());
                    txtFirstName.setText(user.getFirstName());
                    txtLastName.setText(user.getLastName());
                    txtPassword.setText(user.getPassword()); // Cân nhắc: không nên hiển thị mật khẩu thật trong app
                    currentImageUrl = user.getImageUrl();
                    if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                        imgAvatar.setImageURI(Uri.parse(currentImageUrl));
                    }
                } else {
                    Toast.makeText(UpdateAccount.this, "Failed to fetch user info: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UpdateAccount.this, "Failed to fetch user info: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo() {
        String username = txtUsername.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String firstName = txtFirstName.getText().toString().trim();
        String lastName = txtLastName.getText().toString().trim();
        String password = txtPassword.getText().toString();

        // Xử lý URL ảnh: nếu người dùng chọn ảnh mới thì lấy selectedImageUri, ngược lại giữ ảnh cũ
        String imgUrl = currentImageUrl;
        if (selectedImageUri != null) {
            imgUrl = selectedImageUri.toString();
        }

        User updatedUser = new User(
                userId * 1L, // Long id
                username,
                email,
                password,
                firstName,
                lastName,
                imgUrl
        );

        Call<User> call = authApi.updateUser(userId, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UpdateAccount.this, "Update successful", Toast.LENGTH_SHORT).show();
                    User updatedUser = response.body();
                    // Bạn có thể cập nhật UI, lưu user mới nếu cần
                } else {
                    Toast.makeText(UpdateAccount.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UpdateAccount.this, "Update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

