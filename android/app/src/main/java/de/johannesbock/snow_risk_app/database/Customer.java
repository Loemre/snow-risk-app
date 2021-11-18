package de.johannesbock.snow_risk_app.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The entity describes a customer that will be saved into the database
 */
@Entity(tableName = "customer_table")
public class Customer {

    private static final String TAG = Customer.class.getName();

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int customerId;

    // information about the customer (city), information necessary
    @NonNull
    private String name;

    // the request needs long and lat, are necessary
    @NonNull
    private double longitude;
    @NonNull
    private double latitude;

    // store the calculated risk, is necessary
    @NonNull
    private int risk;

    // store temperatures the next two days (applies always to today and the next day)
    private double currentTmp;
    private double dayTmp;
    private double tmpTomorrow;

    // store weather forecast
    private String weather;
    private String weatherDay;
    private String weatherTomorrow;

    // store current weather icon
    private String iconURL;

    // Constructor
    public Customer(@NonNull String name) {
        this.name = name;
    }


    // Getter and Setter for the class

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getRisk() {
        return risk;
    }

    public void setRisk(int risk) {
        this.risk = risk;
    }

    public double getCurrentTmp() {
        return currentTmp;
    }

    public void setCurrentTmp(double currentTmp) {
        this.currentTmp = currentTmp;
    }

    public double getDayTmp() {
        return dayTmp;
    }

    public void setDayTmp(double dayTmp) {
        this.dayTmp = dayTmp;
    }

    public double getTmpTomorrow() {
        return tmpTomorrow;
    }

    public void setTmpTomorrow(double tmpTomorrow) {
        this.tmpTomorrow = tmpTomorrow;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeatherDay() {
        return weatherDay;
    }

    public void setWeatherDay(String weatherDay) {
        this.weatherDay = weatherDay;
    }

    public String getWeatherTomorrow() {
        return weatherTomorrow;
    }

    public void setWeatherTomorrow(String weatherTomorrow) {
        this.weatherTomorrow = weatherTomorrow;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

}
