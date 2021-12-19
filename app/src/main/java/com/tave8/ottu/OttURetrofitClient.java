package com.tave8.ottu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OttURetrofitClient {
    private static final String BASE_URL = "http://192.168.35.170:8080/";
    //private static final String BASE_URL = "http://ec2-13-124-80-193.ap-northeast-2.compute.amazonaws.com/";

    public static OttURetrofitAPI getApiService() {
        return getInstance().create(OttURetrofitAPI.class);
    }

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
