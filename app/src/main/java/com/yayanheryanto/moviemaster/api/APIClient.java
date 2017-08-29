package com.yayanheryanto.moviemaster.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yayanheryanto.moviemaster.config.EndPoint.BASE_URL;

/**
 * Created by Yayan Heryanto on 8/19/2017.
 */

public class APIClient {
    public static Retrofit retrofit = null;

//    private static OkHttpClient buildClient() {
//        return new OkHttpClient
//                .Builder()
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                .build();
//    }
//
    public static Retrofit getApiClient(){

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
