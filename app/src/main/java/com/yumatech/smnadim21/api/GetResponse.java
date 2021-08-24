package com.yumatech.smnadim21.api;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Response;

public interface GetResponse {
    void onResponse(
            int code,
            String msg,
            JSONObject object,
            Response<String> response
    );

    void onResponse(
            int code,
            String msg,
            JSONArray jsonArray,
            Response<String> response
    );

    void onErrorResponse(
            int code,
            String msg,
            JSONObject error,
            Response<String> response
    );

    void onInternalError(
            int code,
            String msg,
            Response<String> response
    );

    void onFailure(
            String msg,
            Throwable t
    );
}
