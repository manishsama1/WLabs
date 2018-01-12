package com.jet.wlabs.wlabs.ui;

import android.support.v4.app.Fragment;

import com.jet.wlabs.wlabs.model.Product;

import java.util.List;

/**
 * A placeholder headless fragment to store model data.
 */
public class MainActivityFragment extends Fragment {
    private List<Product> productList;
    private int currentPage;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public MainActivityFragment() {
        setRetainInstance(true);
    }

    public void setData(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getData() {
        return productList;
    }
}
