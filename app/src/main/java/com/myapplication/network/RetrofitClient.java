package com.myapplication.network;

import static com.myapplication.others.Cons.NETWORK_BASE_URL;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myapplication.base.App;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static int count = 0;
    private static Retrofit retrofitUser = null;

    public static Retrofit getRegisterRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NETWORK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getRegister())
                .build();
        return retrofit;
    }

    public static Retrofit getUpdateProfileRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NETWORK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getRegister())
                .build();
        return retrofit;
    }

    private static OkHttpClient getRegister() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("content-type", "application/json")
                        .addHeader("Accept", "application/json").build();
//                        .addHeader("content-type", "application/x-www-form-urlencoded").build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    public static Retrofit getUserDetails() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofitUser = new Retrofit.Builder()
                .baseUrl(NETWORK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUser())
                .build();
        return retrofitUser;
    }

    public static Retrofit getDFDdata() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofitUser = new Retrofit.Builder()
                .baseUrl(NETWORK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUser())
                .build();
        return retrofitUser;
    }

    public static Retrofit getFormData() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofitUser = new Retrofit.Builder()
                .baseUrl(NETWORK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(formData())
                .build();

        return retrofitUser;
    }

    private static OkHttpClient formData() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + App.get().getToken())
                        //.addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Content-Type", "text/plain").build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    private static OkHttpClient getUser() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES).addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + App.get().getToken())
                            .addHeader("Accept", "application/json").build();
                    Log.e("Token ", App.get().getToken());
                    Response res = chain
                            .proceed(request);
                    return res;
                });
        return httpClient.build();
    }

    private static RetrofitApi apiClient;

    public static RetrofitApi getRequest() {
        count++;
        Log.d("apiHit", "---" + count);
        if (apiClient == null) {
            apiClient = RetrofitClient.getUserDetails().create(RetrofitApi.class);
        }
        return apiClient;
    }
}