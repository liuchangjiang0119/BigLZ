package com.starsutdio.biglz.model.http;



import com.google.gson.JsonObject;
import com.starsutdio.biglz.model.bean.LoginBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by windfall on 16-11-1.
 */

public interface BigLZApi {
    @FormUrlEncoded
//    starstudio
    @POST("user/register/")
    Call<ResponseBody> register(@Header("CheckCode") String check,
                                @Field("un") String username,
                                @Field("email") String email,
                                @Field("pd") String passwd,
                                @Field("pc") String provelCode);
//    userlogin
    @FormUrlEncoded
    @POST("user/login/")
    Observable<LoginBean> login(@Header("CheckCode") String check,
                                @FieldMap Map<String,String> data);

    @Multipart
    @POST("img/upload/")
    Call<ResponseBody> uploadImg (@Part List<MultipartBody.Part> file);

    @POST("user/logout/")
    Call<ResponseBody> logout(@Header("CheckCode") String check);

    @Multipart
    @POST("passages/post/")
    Call<ResponseBody> post(@Header("acckey") String acckey,@Body RequestBody body);
}
