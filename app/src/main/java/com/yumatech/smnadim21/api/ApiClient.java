package com.yumatech.smnadim21.api;




import com.yumatech.smnadim21.tools.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static final String DEV_URL = "https://labapi.yuma-technology.co.uk:8443";
    private static final String PRODUCTION_URL = "https://labapi.yuma-technology.co.uk:8443";
    public static final String BASE_URL = Constants.DEBUG ? DEV_URL : PRODUCTION_URL;


    public static Routes getGsonInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Routes.class);
    }

    public static Routes connect() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Routes.class);
    }



    public static String getBaseUrl() {
        return BASE_URL;
    }
}