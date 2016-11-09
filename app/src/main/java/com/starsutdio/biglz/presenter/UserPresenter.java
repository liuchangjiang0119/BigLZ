package com.starsutdio.biglz.presenter;

import android.database.Observable;
import android.util.Log;

import com.google.gson.JsonObject;
import com.starsutdio.biglz.contract.UserContract;
import com.starsutdio.biglz.model.bean.LoginBean;
import com.starsutdio.biglz.model.http.BigLZApi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windfall on 16-11-2.
 */

public class UserPresenter implements UserContract.Presenter {
    private UserContract.View mView;
    private RequestBody body;
    public UserPresenter(UserContract.View view){
        mView = view;
    }
    String base_url = "http://10.0.0.15:8081/api/";



    BigLZApi getApi(){
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//
//
//                return null;
//            }
//        }).build();

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .baseUrl(base_url)
                            .build();
        BigLZApi api = retrofit.create(BigLZApi.class);
        return api;
    }


    @Override
    public void login(Map map) {
        getApi().login("userlogin",map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LoginBean loginBean) {

                    }
                });

    }

    @Override
    public void logout() {
        Call<ResponseBody> logoutCall = getApi().logout("userlogout");
        logoutCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void register(String username, String email, String password, String provecode) {
        final Call<ResponseBody> registerCall = getApi().register("starstudio","abcdefg","abcdefg@gmail.com","abcdefg","9999");
        registerCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.d("--------------",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("-------------","failure");
            }
        });
    }

    @Override
    public void imgupload(List<MultipartBody.Part> list) {
        Call<ResponseBody> imgCall = getApi().uploadImg(list);
        imgCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    public void post(String acckey,JsonObject jsonObject) {
        body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonObject.toString());
        Call<ResponseBody> passageCall = getApi().post(acckey,body);
        passageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
