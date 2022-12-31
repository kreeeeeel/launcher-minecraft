package com.application.launcher.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.application.launcher.utils.Constant.URL;

public class RetrofitUtils {
    private static Retrofit retrofit;

    public static void generate() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        RetrofitUtils.retrofit = retrofit;
    }
}
