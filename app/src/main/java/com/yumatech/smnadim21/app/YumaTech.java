package com.yumatech.smnadim21.app;

import android.app.Application;

import com.yumatech.smnadim21.db.ProductDB;
import com.yumatech.smnadim21.db.ProductDao;

public class YumaTech extends Application {

    static ProductDao productDao;


    @Override
    public void onCreate() {
        super.onCreate();
        productDao = ProductDB.getInstance(getApplicationContext()).productDao();
    }

    public static ProductDao getProductDao() {
        return YumaTech.productDao;
    }

    public synchronized YumaTech getInstance() {
        return this;
    }
}
