package com.example.taskmanagerfrontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
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
    private int userId = Integer.parseInt(Login.UserID); // Chuyển đổi từ String sang int
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

        // Khởi tạo Retrofit
        authApi = ApiClient.getRetrofitInstance().create(AuthApi.class);

        // Gán View
        imgAvatar = findViewById(R.id.setImgAvt);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Lấy thông tin người dùng từ backend và hiển thị ảnh đại diện hiện tại
        getUserInfo();

        // Click chọn ảnh
        imgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Click cập nhật chỉ với ảnh đại diện
        btnUpdate.setOnClickListener(v -> {
            // Kiểm tra xem người dùng đã chọn ảnh chưa
            if (selectedImageUri == null) {
                Toast.makeText(UpdateAccount.this, "Please select an avatar", Toast.LENGTH_SHORT).show();
                return;
            }

            String imgUrl = selectedImageUri != null ? selectedImageUri.toString() : "abc";  // Chuyển Uri thành String

            // Gửi yêu cầu cập nhật chỉ ảnh đại diện đến server backend với userId và imageUri
            Call<String> call = authApi.updateAvatar(userId, imgUrl);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(UpdateAccount.this, "Avatar updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateAccount.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(UpdateAccount.this, "Update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Lấy thông tin người dùng từ API
    private void getUserInfo() {
        // Gọi API lấy thông tin người dùng dựa trên userId
        Call<User> call = authApi.getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                        Uri imageUri = Uri.parse(user.getImageUrl());
                        imgAvatar.setImageURI(imageUri);  // Hiển thị ảnh đại diện hiện tại
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
}
