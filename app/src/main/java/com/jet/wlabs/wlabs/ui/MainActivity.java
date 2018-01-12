package com.jet.wlabs.wlabs.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.jet.wlabs.wlabs.interactor.MainInteractor;
import com.jet.wlabs.wlabs.presenter.MainPresenter;
import com.jet.wlabs.wlabs.model.Product;
import com.jet.wlabs.wlabs.adapter.ProductAdapter;
import com.jet.wlabs.wlabs.R;

import java.lang.ref.WeakReference;
import java.util.List;

//Main activity to fetch product list. A basic version of MVP pattern is used to develop this feature
public class MainActivity extends AppCompatActivity implements MainInteractor {
    public static final int MAX_PAGE_SIZE = 10;//this can be modified. for this exercise max number of pages is set to 10

    private RecyclerView productListView;
    private GridLayoutManager gridLayoutManager;
    private MainPresenter presenter;
    private boolean isLoading;
    private boolean isLastPage = false;
    private int currentPage = 0;
    private ProductAdapter newsListAdapter;
    private List<Product> productList;
    private ProgressBar loadingBar;
    private ProgressBar mainLoadingBar;
    private String queryString;
    private WeakReference<Context> weakContext;
    private static final String TAG_MAIN_FRAGMENT = "MainActivityFragment";
    private MainActivityFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new MainPresenter(this, this);
        weakContext = new WeakReference<Context>(this);
        loadingBar = (ProgressBar) findViewById(R.id.loading_bar);
        mainLoadingBar = (ProgressBar) findViewById(R.id.main_progress_bar);
        productListView = (RecyclerView) findViewById(R.id.product_list);
        gridLayoutManager = new GridLayoutManager(productListView.getContext(), 2);
        productListView.setLayoutManager(gridLayoutManager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mainFragment = (MainActivityFragment) fragmentManager.findFragmentByTag(TAG_MAIN_FRAGMENT);

        // create the fragment and data the first time
        if (mainFragment == null) {
            // add the fragment
            mainFragment = new MainActivityFragment();
            fragmentManager.beginTransaction().add(mainFragment, TAG_MAIN_FRAGMENT).commit();
        }

        productList = mainFragment.getData();
        currentPage = mainFragment.getCurrentPage();
        if (currentPage == MAX_PAGE_SIZE) {
            isLastPage = true;
        }

        if (productList != null && !productList.isEmpty()) {
            updateAdapter(productList);
        } else {
            toggleMainProgressBarVisibility(true);
            presenter.fetchData(currentPage);
        }

        productListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    //make the next page call again if user has scrolled to the last item in the list.
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadMoreItems();
                    }
                }
            }
        });
    }

    private void loadMoreItems() {
        isLoading = true;
        currentPage += 1;
        if (currentPage == MAX_PAGE_SIZE) {
            isLastPage = true;
        }
        toggleProgressBarVisibility(true);
        presenter.fetchData(currentPage);
    }

    private void toggleProgressBarVisibility(boolean isVisible) {
        loadingBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void toggleMainProgressBarVisibility(boolean isVisible) {
        mainLoadingBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isFinishing()) {
            FragmentManager fm = getSupportFragmentManager();
            // we will not need this fragment anymore, this may also be a good place to signal
            // to the retained fragment object to perform its own cleanup.
            fm.beginTransaction().remove(mainFragment).commit();
        }
    }

    @Override
    public void onDestroy() {
        mainFragment.setData(productList);
        mainFragment.setCurrentPage(currentPage);
        super.onDestroy();
    }
    @Override
    public void updateAdapter(List<Product> productList) {
        toggleProgressBarVisibility(false);
        toggleMainProgressBarVisibility(false);
        if (newsListAdapter == null) {
            this.productList = productList;
            newsListAdapter = new ProductAdapter(this.productList, this);
            productListView.setAdapter(newsListAdapter);
            DividerItemDecoration itemDecoration = new DividerItemDecoration(productListView.getContext(),
                    gridLayoutManager.getOrientation());
            productListView.addItemDecoration(itemDecoration);
        } else {
            isLoading = false;
            this.productList.addAll(productList);
            newsListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showErrorDialog(String errorMessage) {
        if (weakContext.get() == null) {
            return;
        }
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(weakContext.get());
        builder.setTitle(getString(R.string.generic_error_title))
                .setMessage(errorMessage)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ok) {
                        finish();
                    }
                })
                .show();
    }
}
