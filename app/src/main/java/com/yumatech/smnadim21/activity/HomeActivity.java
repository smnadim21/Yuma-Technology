package com.yumatech.smnadim21.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.smnadim21.nadx.tools.NandX;
import com.smnadim21.nadx.tools.json.JsonHandler;
import com.yumatech.smnadim21.R;
import com.yumatech.smnadim21.api.ApiClient;
import com.yumatech.smnadim21.api.GetResponse;
import com.yumatech.smnadim21.api.NetworkCall;
import com.yumatech.smnadim21.databinding.ActivityHomeBinding;
import com.yumatech.smnadim21.db.entity.Products;
import com.yumatech.smnadim21.item.SpinnerItem;
import com.yumatech.smnadim21.tools.JsonParser;
import com.yumatech.smnadim21.viewmodels.ProductsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements JsonParser {
    ActivityHomeBinding binding;
    List<SpinnerItem> shortNames = new ArrayList<>();
    ProductsModel viewModel;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        shortNames.add(new SpinnerItem().setName("Select Products").setId("0"));

        viewModel = new ViewModelProvider(HomeActivity.this).get(ProductsModel.class);

        //fetching data from end point
        fetchData();

        //setting viewmodel to trigger data
        viewModel.getProductsData().observe(HomeActivity.this, jsonObjects -> {
            Toast.makeText(HomeActivity.this, "Data Fetched or updated!", Toast.LENGTH_SHORT).show();
            shortNames.clear();
            binding.tvTest.setText("");
            shortNames.add(new SpinnerItem().setName("Select Products").setId("0"));
            //adding items to spinner
            for (JSONObject obj : jsonObjects) {
                try {
                    // saving data to Sqlite Using room.
                    new Products()
                            .setProduct_uuid(obj.optString(Product.product_uuid))
                            .setShort_name(obj.optString(Product.short_name))
                            .setPrice(obj.getJSONObject(Product.price).optString(Product.Price.price))
                            .setPrint_order(obj.getJSONObject(Product.properties).optString(Product.Properties.print_order))
                            .saveOrUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                    NandX.showError(HomeActivity.this, e.getMessage());
                }
                shortNames.add(new SpinnerItem().setName(obj.optString(Product.short_name)).setId("o"));
            }

            //setting spinner adapter
            ArrayAdapter<SpinnerItem> pnameAdapter = new ArrayAdapter<SpinnerItem>(HomeActivity.this,
                    R.layout.item_spinner,
                    shortNames
            );
            binding.spProducts.setAdapter(pnameAdapter);
            binding.spProducts.setSelection(pos);

            binding.spProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pos = position;

                    if (position != 0) {
                        // patch print_order
                        binding.product.btPatch.setOnClickListener(
                                v -> {
                                    NandX.hideKeyboard(HomeActivity.this, v);
                                    if (TextUtils.isEmpty(Objects.requireNonNull(binding.product.printOrders.getText()).toString())) {
                                        binding.product.printOrders.setError("Empty!");
                                        return;
                                    }
                                    patch(binding.product.printOrders.getText().toString(),
                                            jsonObjects.get(position - 1).optString(Product.product_uuid));
                                }
                        );

                        try {
                            Glide.with(HomeActivity.this)
                                    .load(generateImageUrl(jsonObjects.get(position - 1)))
                                    .placeholder(R.drawable.ic_burgu)
                                    .into(binding.product.itemImage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "Image URL ERROR", Toast.LENGTH_SHORT).show();
                        }

                        // printing full json
                        binding.tvTest.setText(NandX.getPrettyJson(jsonObjects.get(position - 1).toString()));
                        // print short name
                        binding.product.itemName.setText(jsonObjects.get(position - 1).optString(Product.short_name));
                        //print fullname
                        binding.product.itemDesc.setText(jsonObjects.get(position - 1).optString(Product.description));

                        try {
                            //print print_orders
                            binding.product.printOrders.setText(jsonObjects.get(position - 1).getJSONObject(Product.properties).optString(Product.Properties.print_order));
                            //print product_price
                            binding.product.userCash.setText(jsonObjects.get(position - 1).getJSONObject(Product.price).optString(Product.Price.price));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        //if no product selected
                        binding.product.btPatch.setOnClickListener(
                                v -> {
                                    NandX.showError(HomeActivity.this, "Select a Product");
                                    NandX.hideKeyboard(HomeActivity.this, v);
                                }
                        );
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });


        binding.llHead.bBack.setOnClickListener(v -> {
            onBackPressed();
        });


    }

    private void patch(String print_order, String product_uuid) {
        showProgress();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Product.product_type, "ITEM");
        jsonObject.addProperty(Product.product_uuid, product_uuid);
        JsonObject jproperties = new JsonObject();
        jproperties.addProperty(Product.Properties.print_order, print_order);
        jsonObject.add(Product.properties, jproperties);

        ApiClient
                .connect()
                .changePrintOrder(
                        product_uuid,
                        jsonObject,
                        "b2524ade-4dc3-4995-b35e-5f770dae0721"
                ).enqueue(new NetworkCall(HomeActivity.this, new GetResponse() {
            @Override
            public void onResponse(int code, String msg, JSONObject object, Response<String> response) {
                hideProgress();
                if (code == 202) {
                    fetchData();
                    NandX.snackOK(HomeActivity.this,
                            "Change Accepted", v -> {
                            });
                }
            }

            @Override
            public void onResponse(int code, String msg, JSONArray jsonArray, Response<String> response) {
                hideProgress();
            }

            @Override
            public void onErrorResponse(int code, String msg, JSONObject error, Response<String> response) {
                hideProgress();
            }

            @Override
            public void onInternalError(int code, String msg, Response<String> response) {
                hideProgress();
            }

            @Override
            public void onFailure(String msg, Throwable t) {
                hideProgress();
            }
        }));

    }

    private void fetchData() {
        showProgress();
        ApiClient.connect()
                .getProducts(
                        true,
                        "ITEM",
                        "a7033019-3dee-457b-a9da-50df238b3c9k",
                        true


                ).enqueue(new NetworkCall(HomeActivity.this, new GetResponse() {
            @Override
            public void onResponse(int code, String msg, JSONObject object, Response<String> response) {
                hideProgress();

            }

            @Override
            public void onResponse(int code, String msg, JSONArray jsonArray, Response<String> response) {
                hideProgress();
                try {
                    viewModel.setProductsData(new JsonHandler().toList(jsonArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onErrorResponse(int code, String msg, JSONObject error, Response<String> response) {
                hideProgress();

            }

            @Override
            public void onInternalError(int code, String msg, Response<String> response) {
                hideProgress();

            }

            @Override
            public void onFailure(String msg, Throwable t) {
                hideProgress();

            }
        }));
    }

    //show progressbar
    protected void showProgress() {
        binding.llLoading.spinKit.setVisibility(View.VISIBLE);
    }

    //hide progressbar
    protected void hideProgress() {
        binding.llLoading.spinKit.setVisibility(View.GONE);
    }

    protected String generateImageUrl(JSONObject jobj) throws JSONException {
        JSONObject fileObj = jobj.getJSONArray(Product.files).getJSONObject(0);
        return "https://labapi.yuma-technology.co.uk:8443/delivery/product/" + fileObj.optString(Product.Files.product_uuid) + "/file/" + fileObj.optString(Product.Files.file_uuid);
    }
}