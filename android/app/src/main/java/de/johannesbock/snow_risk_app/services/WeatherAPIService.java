package de.johannesbock.snow_risk_app.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Class to get data from the weatherapi service provided by https://www.weatherapi.com/
 * Needs an instance of {@link SnowRiskSingleton} for each request
 */
public class WeatherAPIService {

    public static final String TAG = WeatherAPIService.class.getName();

    // URL Query for the request
    // TODO: key is visible in the code, security risk!
    public static final String QUERY_FOR_WEATHER_API = "https://api.weatherapi.com/v1/forecast.json?key=fa67a1a4ca7a410795595805211711";

    Context context;

    /**
     * Constructor for the class that communicates with the weather api service
     * Handles all requests with the REST API
     * @param context The current context is needed to run the Singleton
     */
    public WeatherAPIService(Context context) {
        this.context = context;
    }

    /**
     * This interface handles the response from the api service
     */
    public interface WeatherForecastListener {
        void onError(String message);
        void onResponse(JSONObject weatherForecast);
    }

    /**
     * This function creates a new request to get the Weather Forecast for a specific city and days
     * and adds the request to the request queue of the singleton
     * @param city The name of the city for the forecast
     * @param days The amount of days for the forecast
     * @param weatherForecastListener A Listener that handles the response of the request
     */
    public void getWeatherForecast(String city, int days, WeatherForecastListener weatherForecastListener) {

        // The URL for the request, q is the city and days for how many days forward the forecast should go
        String URL = QUERY_FOR_WEATHER_API + "&q=" + city + "&days=" + days;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            weatherForecastListener.onResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherForecastListener.onError(error.toString());
            }
        });

        SnowRiskSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

}
