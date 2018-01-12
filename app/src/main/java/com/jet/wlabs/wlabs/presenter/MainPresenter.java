package com.jet.wlabs.wlabs.presenter;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.jet.wlabs.wlabs.interactor.MainInteractor;
import com.jet.wlabs.wlabs.R;
import com.jet.wlabs.wlabs.model.WLabsResponse;
import com.jet.wlabs.wlabs.network.NetworkManager;

/**
 * Created by Manish on 1/9/2018.
 */
//Presenter class to handle the business logic
public class MainPresenter {
    private MainInteractor view;
    private Context context;
    private String url;
    private NetworkManager networkManager;

    public MainPresenter(Context context, MainInteractor view) {
        this.view = view;
        this.context = context;
        networkManager = NetworkManager.getInstance(context.getApplicationContext());
    }

    //make network request
    public void fetchData(int currentPage) {
        networkManager.createRequest(currentPage, new NetworkManager.Callback<WLabsResponse>() {
            @Override
            public void onSuccess(WLabsResponse response) {
                if (response != null && response.isValidResponse()) {
                    view.updateAdapter(response.getProductList());
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                String errorMessage = getErrorString(R.string.generic_error_message);
                if (error instanceof NetworkError) {
                    errorMessage = getErrorString(R.string.no_network_error);
                } else if (error instanceof ServerError) {
                    errorMessage = getErrorString(R.string.server_error);
                } else if (error instanceof NoConnectionError) {
                    errorMessage = getErrorString(R.string.no_network_error);
                } else if (error instanceof TimeoutError) {
                    errorMessage = getErrorString(R.string.timeout_error);
                }

                view.showErrorDialog(errorMessage);
            }
        });
    }

    private String getErrorString(int stringID) {
        return context.getString(stringID);
    }

    //use when want to cancel the network request
    public void cancelAllNetworkRequests() {
        networkManager.cancelRequests();
    }
}
