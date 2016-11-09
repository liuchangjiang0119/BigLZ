package com.starsutdio.biglz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.starsutdio.biglz.model.http.BigLZApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String url = "http://10.0.0.13:8081/api/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url).build();

        BigLZApi bigLZApi = retrofit.create(BigLZApi.class);
        Call<ResponseBody> registerCall = bigLZApi.register
                ("starstudio", "joker", "evilkjoker.gmail", "password", "9999");
//        registerCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    Log.d("-----------------",response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("-----------------","failure");
//            }
//        });

        Map<String, String> data = new HashMap<>();
        data.put("un", "joker@starstudio.org");
        data.put("pd", "password");
        data.put("pc", "9a3b");


//        Call<ResponseBody> loginCall = bigLZApi.login("userlogin",data);
//        loginCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    Log.d("--------------",response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("-----------------","failure");
//            }
//        });
//
//    }
    }
}
