package com.jet.wlabs.wlabs.interactor;

import com.jet.wlabs.wlabs.model.Product;

import java.util.List;

/**
 * Created by Manish on 1/9/2018.
 */

public interface MainInteractor {
    void updateAdapter(List<Product> productList);

    void showErrorDialog(String errorMessage);
}
