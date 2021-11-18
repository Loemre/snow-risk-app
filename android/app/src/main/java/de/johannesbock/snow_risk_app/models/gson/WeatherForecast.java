package de.johannesbock.snow_risk_app.models.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * These package is just a representation of the json response and helps to handle the json response
 * These classes in this package were generated with the help of https://www.jsonschema2pojo.org
 * Tweaked some minor issues, so the representation is right
 *
 * Just simple classes to store every aspect of information as an object with some getters and setters
 *
 */
public class WeatherForecast {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("forecast")
    @Expose
    private Forecast forecast;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

}