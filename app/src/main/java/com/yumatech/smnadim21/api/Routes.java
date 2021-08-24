package com.yumatech.smnadim21.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Routes {


    @Headers({"Accept: application/json;charset=UTF-8;"})
    @GET("/delivery/product")
    Call<String> getProducts(
            @Query("show_hierarchy") Boolean show_hierarchy,
            @Query("product_type") String product_type,
            @Query("provider_uuid") String provider_uuid,
            @Query("show_files") Boolean show_files

    );

    @Headers({"Accept: application/json;"})
    @PATCH("/delivery/product/{product_uuid}")
    Call<String> changePrintOrder(
            @Path("product_uuid") String product_uuid,
            @Body JsonObject jsonObject,
            @Header("ProviderSession") String providerSession
    );

}


