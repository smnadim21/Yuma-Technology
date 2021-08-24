package com.yumatech.smnadim21.api;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.yumatech.smnadim21.R;
import com.yumatech.smnadim21.tools.Constants;
import com.yumatech.smnadim21.tools.InternetCheck;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCall implements Callback<String>, InternetCheck.Consumer, Constants {
    Activity activity;
    String tag = "NetworkCall";
    String okMsg = "";
    GetResponse getResponse;

    public NetworkCall(Activity activity) {
        this.activity = activity;
        tag = activity.getLocalClassName();
    }

    public NetworkCall(Activity activity, GetResponse getResponse) {
        this.activity = activity;
        tag = activity.getLocalClassName();
        this.getResponse = getResponse;
        new InternetCheck(this);
    }

    @Override
    public void onResponse(@NotNull Call<String> call, Response<String> response) {
        Log.e(tag, response.toString());
        if (response.body() != null && (response.code() >= 200 && response.code() < 300)) {
            Log.e(tag, response.body());
            try {
                JSONObject object = new JSONObject(response.body());
                Log.e(tag, getPrettyJson(response.body()));
                String message = object.optString("message", "empty message from server");
                okMsg = message;

                getResponse.onResponse(
                        response.code(),
                        message,
                        object,
                        response);


            } catch (JSONException e) {
                e.printStackTrace();

                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    Log.e(tag, getPrettyJson(response.body()));
                    String message = "empty message from server";
                    getResponse.onResponse(
                            response.code(),
                            message,
                            jsonArray,
                            response);

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();

                    if (response.body() != null && !response.body().isEmpty()) {
                        getResponse.onInternalError(
                                response.code(),
                                jsonException.getMessage(),
                                response);

                        snackErr(activity
                                , e.getMessage(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (DEBUG) {
                                            showError(activity, getPrettyJson(e));
                                        }
                                    }
                                });
                    } else {
                        getResponse.onResponse(
                                response.code(),
                                response.message(),
                                new JSONObject(),
                                response);
                    }

                }


            }

   /*         if (DEBUG)
                snackInfo(activity,
                        "[DEBUG: TRUE] >> " + okMsg,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showError(getPrettyJson(response.body()));
                            }
                        });*/
        } else {
            if (response.errorBody() != null) {
                try {
                    final String err = response.errorBody().string();

                    Log.e("onErrorResponse", err);
                    JSONObject validation = new JSONObject(err);
                    Log.e("onErrorResponse", new Gson().toJson(validation));

                    getResponse.onErrorResponse(
                            response.code(),
                            validation.optString("message", "no message from server"),
                            validation,
                            response);

                    if (response.code() == 401) {
                        /*    if (activity instanceof Splash) {
                         *//*if(!DEBUG) *//*

                        } else*/
                        {
                            Snackbar snackbar = Snackbar.make(
                                    activity.findViewById(android.R.id.content),
                                    "Authentication Error!",
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setActionTextColor(Color.WHITE)
                                    .setAction("LOGIN AGAIN", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.red_900));
                            snackbar.show();
                        }

                    } else {
                        snackErr(activity
                                , (DEBUG ? response.code() + " " : "") + validation.optString("message"),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (DEBUG) {
                                            showError(activity, getPrettyJson(err));
                                        }
                                    }
                                });
                    }


                    if (validation.has("errors")) {
                        JSONObject errors = validation.getJSONObject("errors");

                        Iterator<String> keys = errors.keys();
                        if (keys.hasNext()) {
                            String currentKey = keys.next();
                            if (errors.has(currentKey)) {
                                Log.e("error>>", currentKey);
                                snackErr(activity
                                        , //response.code()
                                        //   + " "
                                        //  + validation.optString("message") +
                                        errors.getJSONArray(currentKey).getString(0)
                                        ,
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (DEBUG) {
                                                    showError(activity, getPrettyJson(err));
                                                }

                                            }
                                        });


                            }
                        }


                    }


                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    getResponse.onInternalError(
                            response.code(),
                            e.getMessage(),
                            response);

                    snackErr(activity
                            , e.getMessage(),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (DEBUG) {
                                        showError(activity, getPrettyJson(e));
                                    }

                                }
                            });
                }
            }
        }


    }


    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.e(tag, t.getMessage());

        getResponse.onFailure(
                t.getMessage(),
                t);
        if (DEBUG) {
            snackErr(activity
                    , t.getMessage(),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (DEBUG) {
                                showError(t.getMessage());
                            }

                        }
                    });
        }


    }

    private String getPrettyJson(String text) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(text));
    }

    private String getPrettyJson(Object obj) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }

    void snackErr(Activity activity,
                  final String mainTextString,
                  View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                mainTextString,
                Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE)
                .setAction((DEBUG) ? "MORE" : "CLOSE", listener);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.red_900));
        snackbar.show();
    }

    void snackOK(Activity activity,
                 final String mainText,
                 View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                mainText,
                Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE)
                .setAction("OK", listener);

        snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.green_900));
        snackbar.show();
    }

    void snackInfo(Activity activity,
                   final String mainText,
                   View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                mainText,
                Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.WHITE)
                .setAction("VIEW RESPONSE", listener);

        snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.blue_800));
        snackbar.show();
    }

    private static void showError(final Activity context, String errString) {
        final Dialog dialog = new Dialog(context, R.style.AppThemeAction);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_error);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final TextView error = dialog.findViewById(R.id.error);
        final AppCompatImageView close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        error.setText(errString);
        dialog.show();
    }

    private void showError(String errString) {
        final Dialog dialog = new Dialog(activity, R.style.AppThemeAction);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_error);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final TextView error = dialog.findViewById(R.id.error);
        error.setText(errString);

        final AppCompatImageView close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void accept(Boolean internet) {
        if (!internet) {
            Throwable t = new Throwable("NO INTERNET");
            getResponse.onFailure(
                    "NO INTERNET",
                    t
            );
            snackErr(activity
                    , "NO INTERNET",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (DEBUG) {
                                showError(t.getMessage());
                            }

                        }
                    });
        }
    }


}
