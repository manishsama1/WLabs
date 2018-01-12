package com.jet.wlabs.wlabs.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jet.wlabs.wlabs.BuildConfig;
import com.jet.wlabs.wlabs.model.WLabsResponse;

import org.json.JSONObject;

/**
 * Created by Manish on 1/9/2018.
 */
//Singleton class to handle network requests
public class NetworkManager {
    private static final int PAGE_SIZE = 30;
    private static final int API_TIMEOUT = 45000;
    private static final int MAX_RETRY = 2;

    private static NetworkManager networkManager;
    private Context context;
    private RequestQueue requestQueue;

    public NetworkManager(Context context) {
        this.context = context;
    }

    public static NetworkManager getInstance(Context context) {
        if (networkManager == null) {
            synchronized (NetworkManager.class) {
                if (networkManager == null) {
                    networkManager = new NetworkManager(context);
                }
            }
        }
        return networkManager;
    }

    private void addRequestToQueue(JsonObjectRequest stringRequest) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }

    //create network request to fetch products list
    public void createRequest(int currentPage, final Callback<WLabsResponse> callback) {
        String params = "/" + currentPage + "/" + PAGE_SIZE;
        String finalURL = BuildConfig.URL_WLABS + BuildConfig.WLABS_API_KEY + params;
        System.out.println("Manish ->  " + finalURL);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, finalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new GsonBuilder().create();
                        WLabsResponse wLabsResponse = gson.fromJson(response.toString(), WLabsResponse.class);
                        callback.onSuccess(wLabsResponse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error);
            }
        });

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return API_TIMEOUT;
            }

            @Override
            public int getCurrentRetryCount() {
                return MAX_RETRY;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        addRequestToQueue(request);
    }

    public void cancelRequests() {
        if (requestQueue != null) {
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    public interface Callback<T> {
        void onSuccess(T response);

        void onFailure(VolleyError error);
    }
}

