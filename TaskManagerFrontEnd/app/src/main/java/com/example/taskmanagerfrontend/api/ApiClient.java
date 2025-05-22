package com.example.taskmanagerfrontend.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static final String BASE_URL = "http://192.168.68.244:8080";  // Địa chỉ IP của backend

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            // Tạo logging interceptor để in log request/response
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // In cả header + body

            // Cấu hình OkHttpClient với logging + timeout
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)  // Gắn interceptor vào client
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            // Cấu hình Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder().setLenient().create())) // JSON lenient
                    .build();
        }
        return retrofit;
    }
}