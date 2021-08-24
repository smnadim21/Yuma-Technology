package com.yumatech.smnadim21.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.List;

public class ProductsModel extends ViewModel {
    public MutableLiveData<List<JSONObject>> productsData = new MutableLiveData<>();

    public ProductsModel() {
    }

    public void setProductsData(List<JSONObject> productsData) {
        this.productsData.postValue(productsData);
    }

    public LiveData<List<JSONObject>> getProductsData() {
        return productsData;
    }
}
