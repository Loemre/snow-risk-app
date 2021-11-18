package de.johannesbock.snow_risk_app.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.johannesbock.snow_risk_app.models.gson.Forecastday;
import de.johannesbock.snow_risk_app.models.gson.Hour;
import de.johannesbock.snow_risk_app.models.gson.WeatherForecast;
import de.johannesbock.snow_risk_app.services.WeatherAPIService;


/**
 * Customer Repository to handle the data from the database and the network
 * Helps to read data from the database or write to database on a different thread
 */
public class CustomerRepository {

    private static final String TAG = CustomerRepository.class.getName();

    // make sure only there is only one CustomerRepository
    private static CustomerRepository INSTANCE;
    private final CustomerDatabase database;

    private CustomerDAO customerDAO;
    private LiveData<List<Customer>> allCustomers;

    private List<Customer> allCurrentCustomers;

    // Gson Instance to convert json to a java object WeatherForecast
    Gson gson = new Gson();

    private CustomerRepository(Application application) {
        database = CustomerDatabase.getInstance(application);
        customerDAO = database.customerDAO();
        allCustomers = customerDAO.getCustomersSortedByRisk();
    }

    /**
     * In a given application there should only be on repository
     * Makes sure all operations on a database are handled and not overwritten by a second repository
     * @param application The application context where the function gets called
     * @return returns the instance of the repository
     */
    public static CustomerRepository getInstance(Application application) {
        if(INSTANCE == null) {
            synchronized (CustomerRepository.class) {
                if(INSTANCE == null) {
                    INSTANCE = new CustomerRepository(application);
                }
            }
        }

        return INSTANCE;

    }

    /**
     * Updates the whole database from the Weather API
     * Calls the Api for every customer in the database
     * @param application needed for the request singleton
     */
    public void updateDatabaseFromAPI(Application application) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                allCurrentCustomers = customerDAO.getAllCustomers();

                for(Customer customer : allCurrentCustomers) {
                    callApi(application, customer);
                }

            }
        });

    }

    /**
     * Updates only one customer from the table
     * For example initialises the information for a newly added customer
     * @param application needed for the request singleton
     * @param customerName the name is needed to find the customer in the database and for the
     *                     api search string
     */
    public void updateOneCustomerFromAPI(Application application, String customerName) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Customer customer = customerDAO.getCustomerByName(customerName);
                callApi(application, customer);
            }
        });


    }

    /**
     * Calls the api for a given customer
     * @param application needed for the request singleton
     * @param customer Provides the name for the api request query and the customer that should be
     *                 updated with the response
     */
    private void callApi(Application application, Customer customer) {

        WeatherAPIService weatherAPIService =
                new WeatherAPIService(application.getApplicationContext());

        weatherAPIService.getWeatherForecast(customer.getName(), 2,
                new WeatherAPIService.WeatherForecastListener() {
                    @Override
                    public void onError(String message) {
                        Log.e(TAG, "onError: " + message);
                    }

                    @Override
                    public void onResponse(JSONObject weatherForecast) {
                        try {
                            // call the update function after successful request with the response
                            // makes sure the data was fetched, before the update function is called
                            updateCustomerWeather(customer.getCustomerId(), customer.getName(), weatherForecast);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }
                    }
                });

    }


    // database functions
    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public void insert(Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(() -> {
            customerDAO.insert(customer);
        });
    }

    public void update(Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(() -> {
            customerDAO.update(customer);
        });
    }

    /**
     * Updates the customer in the database with the current weather
     * Creates a new customer with the new data and the name and id from existing customer
     * The function does not check the validity of the response data
     * @param customerID an int value that holds the customer id which is needed for the update function,
     *                   will be applied to the customer
     * @param name a string that holds the name which is needed to create a new customer object,
     *             that will send to the database
     * @param currentWeather a JSONObject that holds the response from the api service
     */
    private void updateCustomerWeather(int customerID, String name, JSONObject currentWeather) {
        Customer customer = new Customer(name);

        // convert the json response to a java class object
        String jsonForecast = currentWeather.toString();

        // use gson to convert the json string into classes
        // this helps to access the data to apply to the customer
        WeatherForecast weatherForecast = gson.fromJson(jsonForecast, WeatherForecast.class);

        customer.setCustomerId(customerID);
        customer.setLatitude(weatherForecast.getLocation().getLat());
        customer.setLongitude(weatherForecast.getLocation().getLon());
        customer.setDayTmp(weatherForecast.getForecast().getForecastday().get(0).getDay()
                .getAvgtempC());
        customer.setTmpTomorrow(weatherForecast.getForecast().getForecastday().get(1).getDay()
                .getAvgtempC());
        customer.setCurrentTmp(weatherForecast.getCurrent().getTempC());
        customer.setWeather(weatherForecast.getCurrent().getCondition().getText());
        customer.setWeatherDay(weatherForecast.getForecast().getForecastday().get(0).getDay()
                .getCondition().getText());
        customer.setWeatherTomorrow(weatherForecast.getForecast().getForecastday().get(1).getDay()
                .getCondition().getText());

        // change it to a readable URL for JAVA
        String iconUrl = weatherForecast.getCurrent().getCondition().getIcon().replace("//", "https://");
        customer.setIconURL(iconUrl);

        // for the risk the calculateRisk function is called
        customer.setRisk(calculateRisk(weatherForecast));

        // update the customer inside the database
        update(customer);
    }


    /**
     *  The function calculates the risk of possible snow
     *  Risk can have 7 values.
     *  The description of the weather always wins, when chance_of_snow is less and chance_of_snow wins when it is higher
     *  It depends always on the highest value in the data.
     * @param weatherForecast A JSONObject of the response from the Weather API
     * @return risk -1   =   error in calculation
     *         0    =   0 % chance of snow (Weather API chance_of_snow always 0 in JSON Object)
     *         1    =   20 % chance of snow (Weather API chance_of_snow between 1 and 20 or light snow in JSON Object)
     *         2    =   40 % chance of snow (Weather API chance_of_snow between 21 and 40 in JSON Object)
     *         3    =   60 % chance of snow (Weather API chance_of_snow between 41 and 60 or medium snow in JSON Object)
     *         4    =   80 % chance of snow (Weather API chance_of_snow between 61 and 80 in JSON Object)
     *         5    =   100 % chance of snow (Weather API chance_of_snow between 81 and 100 or heavy snow in JSON Object)
     */
    public int calculateRisk(WeatherForecast weatherForecast) {
        int risk = -1;

        // search all weather texts for snow
        List<String> weatherText = new ArrayList<>();

        // the current weather text only exists once
        weatherText.add(weatherForecast.getCurrent().getCondition().getText());

        // every forecastday has its own condition text
        for (Forecastday forecastday : weatherForecast.getForecast().getForecastday()) {

            weatherText.add(forecastday.getDay().getCondition().getText());

            // every hour has its own condition text
            for(Hour hour : forecastday.getHour()) {
                weatherText.add(hour.getCondition().getText());
            }

        }

        // check all texts on snow appearances and apply the correct risk level
        // always keep the highest value
        for(String text : weatherText) {

            if(text.toLowerCase().contains("heavy snow")) {
                risk = 5;
                if(text.toLowerCase().contains("moderate")){
                    // special case, sometimes it is moderate or high, then choose risk factor 4
                    risk = 4;
                }
            } else if(text.toLowerCase().contains("blowing snow") && risk < 4) {
                // special case, lots of snow and wind, bit less than heavy
                risk = 4;
            }
            else if(text.toLowerCase().contains("moderate snow") && risk < 3) {
                // if there was heavy snow already the risk stays higher
                risk = 3;
            } else if(text.toLowerCase().contains("light snow") && risk < 1) {
                // if there was already medium or higher the risk stays higher
                risk = 1;
            } else if(risk < 0) {
                risk = 0;
            }
        }

        // now search chance_of_snow for higher risk of snow

        List<Integer> chanceOfSnow = new ArrayList<>();

        for(Forecastday forecastday : weatherForecast.getForecast().getForecastday()) {

            chanceOfSnow.add(forecastday.getDay().getDailyChanceOfSnow());

            for(Hour hour : forecastday.getHour()) {
                chanceOfSnow.add(hour.getChanceOfSnow());
            }

        }

        for(int chance : chanceOfSnow) {

            // check the ranges and the current risk
            // only if one risk is lower than the range suggests it will get adjusted

            if(0 < chance && chance <= 20 && risk < 1) {
                risk = 1;
            } else if (20 < chance && chance <= 40 && risk < 2) {
                risk = 2;
            } else if(40 < chance && chance <= 60 && risk < 3) {
                risk = 3;
            } else if(60 < chance && chance <= 80 && risk < 4) {
                risk = 4;
            } else if(80 < chance && chance <= 100 && risk < 5) {
                risk = 5;
            }

        }

        return risk;
    }

}
