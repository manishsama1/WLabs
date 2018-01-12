package com.jet.wlabs.wlabs.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Manish on 1/9/2018.
 */

public class WLabsResponse {
    @SerializedName("products")
    private List<Product> productList;

    public List<Product> getProductList() {
        return productList;
    }

    public boolean isValidResponse() {
        return !productList.isEmpty() && productList != null;
    }
}
