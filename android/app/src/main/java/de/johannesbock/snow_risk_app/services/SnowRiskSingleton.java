package de.johannesbock.snow_risk_app.services;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class makes sure, that only one WeatherAPIService class object exists
 * Must be called by the {@link WeatherAPIService}
 */
public class SnowRiskSingleton {

    private static final String TAG = SnowRiskSingleton.class.getName();

    private static SnowRiskSingleton INSTANCE;
    private RequestQueue requestQueue;
    private static Context context;

    private SnowRiskSingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * This function returns the INSTANCE of the singleton and makes sure, only one instance exists
     * @param context the application context, so the singleton knows the current application context
     *                and stops leaking
     * @return INSTANCE of the existing singleton
     */
    public static synchronized SnowRiskSingleton getInstance(Context context) {
        if(INSTANCE == null) {
            context = context.getApplicationContext();
            INSTANCE = new SnowRiskSingleton(context);
        }
        return INSTANCE;
    }


    /**
     * Returns the request queue of the singleton
     * Needs the application context or otherwise the Activity or BroadcastReceiver could leak
     * @return requestQueue of the singleton
     */
    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }

    /**
     * Adds a new request to the queue
     * @param request The request that should be added to the queue
     */
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
