package com.remygratwohl.taskery;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.remygratwohl.taskery.models.ApiError;
import com.remygratwohl.taskery.models.Character;
import com.remygratwohl.taskery.models.Quest;
import com.remygratwohl.taskery.models.User;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Remy on 3/1/2017.
 */

public interface TaskeryAPI {

    @FormUrlEncoded
    @POST("user/login")
    Call<User> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user/register")
    Call<JsonObject> registerUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("user/character/create")
    Call<JsonObject> sendCharacter(
            @Header("Authorization") String token,
            @Body Character c
    );

    @POST("user/character/quests/create")
    Call<JsonObject> sendQuest(
            @Header("Authorization") String token,
            @Body Quest q
    );

    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("https://enigmatic-castle-92786.herokuapp.com/")
            .baseUrl("http://192.168.0.21:3000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(
                    new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                    .create())
            )
            .build();


}
