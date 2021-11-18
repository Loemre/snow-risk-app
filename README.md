# The snow risk app

This app shows cities as customers and there risk for heavy snow in the next 48 hours. The risk is displayed as text and a snowflake in different colors. See the screenshot for more. You can add more customers to the application and reload the database. The database comes prefilled with 20 example cities around the world, big cities and examples for colder cities to show the functionality of the risk calculator.

## Used frameworks and tools

* volley for the api requests
* room for the sqlite database
* lifecycle components to hande live data from the database
* gson to handle the json response as java objects

Usages are documented in the code with comments.

# Explain the risk factor

There are 5 steps of risk or no risk. If the weather API responds with no snow at all in the textual description of the weather in the next 48 hours and the chance of snow is 0 as well, there will be displayed a grey snowflake and the text "0% risk of snow". Heavy snow or 81 or more of chance of snow will result in 100% risk of snow and a dark red snowflake. These are all the steps:

* Risk -1: Calculation error, displays a black error symbol and an error message
* Risk 0: No risk of snow at all (no snow in text, chance of snow 0), displays grey snowflake and the text "0% risk of snow"
* Risk 1: A low risk of snow (light snow in text or chance of snow between 1 and 20), displays green snowflake and the text "20% risk of snow"
* Risk 2: A small risk of snow (chance of snow between 21 and 40), displays yellow snowflake and the text "40% risk of snow"
* Risk 3: A medium risk of snow (medium snow in text or chance of snow between 41 and 60), displays orange snowflake and the text "60% risk of snow"
* Risk 4: A risk of snow (chance of snow between 61 and 80 or blow snow), displays light red snowflake and the text "80% risk of snow"
    * special case moderate or high snow will result in Risk 4 (between moderate and high)  
* Risk 5: A high risk of snow (heavy snow or chance of snow betwenn 81 and 100, display dark red snowflake and the text "100% risk of snow"

The texts may hold more information like patchy or shower snow. These information are not important for the risk calculation in this case.

# The UI

The app displays all costumers as an own card view which displays the Name, the current weather as text and icon on the left side with the current temperatur and the current risk on the right side (see screenshot). Two Floating Action Buttons allow to add a new customer or to refresh the list. The list is sorted descending by the risk. So all customers with a high risk are displayed at the top. 

<img src="/images/screenshot-snow-risk-app.png" alt="Screenshot of the app" width="25%">

# The used API

For this application I used the Weather API, which provides a lot of free calls which helps to test the application and the api. The query works like following:

* https://api.weatherapi.com/v1/forecast.json?key=key&q=city&days=days
  * where key is the api key
  * city is the city for the forecast
  * days are the days forward

For this application I could choose two days as a fixed value, because it should check the next 48 hours (or 2 days). The response is a very detailed json with lots of useful information. A small JSON example is found below (I cut out some information).

## A JSON response example

```
{
    "location": {
        "name": "Oslo",
        "region": "Oslo",
        "country": "Norway",
        "lat": 59.92,
        "lon": 10.75,
        "tz_id": "Europe/Oslo",
        "localtime_epoch": 1637246619,
        "localtime": "2021-11-18 15:43"
    },
    "current": {
        "last_updated_epoch": 1637245800,
        "last_updated": "2021-11-18 15:30",
        "temp_c": 2.0,
        "temp_f": 35.6,
        "is_day": 1,
        "condition": {
            "text": "Mist",
            "icon": "//cdn.weatherapi.com/weather/64x64/day/143.png",
            "code": 1030
        },
        "wind_mph": 3.8,
        "wind_kph": 6.1,
        "wind_degree": 340,
        "wind_dir": "NNW",
        "pressure_mb": 1001.0,
        "pressure_in": 29.56,
        "precip_mm": 0.0,
        "precip_in": 0.0,
        "humidity": 93,
        "cloud": 75,
        "feelslike_c": 2.0,
        "feelslike_f": 35.6,
        "vis_km": 10.0,
        "vis_miles": 6.0,
        "uv": 2.0,
        "gust_mph": 2.2,
        "gust_kph": 3.6
    },
    "forecast": {
        "forecastday": [
            {
                "date": "2021-11-18",
                "date_epoch": 1637193600,
                "day": {
                    "maxtemp_c": 2.4,
                    "maxtemp_f": 36.3,
                    "mintemp_c": -0.1,
                    "mintemp_f": 31.8,
                    "avgtemp_c": 1.2,
                    "avgtemp_f": 34.1,
                    "maxwind_mph": 3.6,
                    "maxwind_kph": 5.8,
                    "totalprecip_mm": 0.0,
                    "totalprecip_in": 0.0,
                    "avgvis_km": 10.0,
                    "avgvis_miles": 6.0,
                    "avghumidity": 81.0,
                    "daily_will_it_rain": 0,
                    "daily_chance_of_rain": 0,
                    "daily_will_it_snow": 0,
                    "daily_chance_of_snow": 0,
                    "condition": {
                        "text": "Partly cloudy",
                        "icon": "//cdn.weatherapi.com/weather/64x64/day/116.png",
                        "code": 1003
                    },
                    "uv": 1.0
                },
                "astro": {
                    "sunrise": "08:23 AM",
                    "sunset": "03:42 PM",
                    "moonrise": "03:32 PM",
                    "moonset": "07:00 AM",
                    "moon_phase": "Waning Gibbous",
                    "moon_illumination": "97"
                },
                "hour": [
                    {
                        "time_epoch": 1637190000,
                        "time": "2021-11-18 00:00",
                        "temp_c": 1.2,
                        "temp_f": 34.2,
                        "is_day": 0,
                        "condition": {
                            "text": "Clear",
                            "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",
                            "code": 1000
                        },
                        ... },
                ],
           },
}
```

<a href="https://www.weatherapi.com/" title="Free Weather API"><img src='https://cdn.weatherapi.com/v4/images/weatherapi_logo.png' alt="Weather data by WeatherAPI.com" border="0"></a> Powered by <a href="https://www.weatherapi.com/" title="Free Weather API">WeatherAPI.com</a>

# Outlook

What could be done next? 
* Clickable cards with more information and the forecast for the next two days
* Make customers deletable
* Alphabetical or sort by risk? Let the user choose
