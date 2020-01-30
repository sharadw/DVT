package com.dvt.dvtweather.serverrequest;

import com.android.volley.Request.Method;
import com.dvt.dvtweather.BuildConfig;
import com.dvt.dvtweather.interfaces.AppConstants;
import com.dvt.dvtweather.model.WeatherForecastModel;
import com.dvt.dvtweather.model.WeatherModel;


/**
 * Enum for API URL Holder
 */

public enum URLManager {



    URL_GET_WEATHER("/weather", AppConstants.WEATHER, Method.GET, WeatherModel[].class),
    URL_GET_WEATHER_FORECAST("/forecast", AppConstants.WEATHER_FORCAST, Method.GET, WeatherForecastModel[].class);


    private final String strURL;
    private final int taskId, requestMethod;
    private final Class clazz;

    URLManager(String strURL, int URLIndex, int requestMethod, Class clazz) {
        this.strURL = strURL;
        this.taskId = URLIndex;
        this.requestMethod = requestMethod;
        this.clazz = clazz;
    }


    public int getTaskId() {
        return taskId;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public Class getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return  BuildConfig.BASE_URL + strURL;
    }
}