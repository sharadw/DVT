package com.dvt.dvtweather.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.dvt.dvtweather.R;
import com.dvt.dvtweather.adapter.ForecastAdapter;
import com.dvt.dvtweather.interfaces.AppConstants;
import com.dvt.dvtweather.interfaces.PermissionRequestCallbackListener;
import com.dvt.dvtweather.model.BaseAPIResponse;
import com.dvt.dvtweather.model.WeatherForecastModel;
import com.dvt.dvtweather.model.WeatherModel;
import com.dvt.dvtweather.serverrequest.APIController;
import com.dvt.dvtweather.serverrequest.TaskListener;
import com.dvt.dvtweather.serverrequest.URLManager;
import com.dvt.dvtweather.utils.LocationHandler;
import com.dvt.dvtweather.utils.PermissionHandler;

import java.util.HashMap;
import java.util.List;


public class DVTWeatherFragment extends Fragment implements PermissionRequestCallbackListener, LocationHandler.LocationListener {

    //@BindView(R.id.clConditionBg)
    private ConstraintLayout clConditionBg;
    // @BindView(R.id.tvMinTemp)
    private TextView tvMinTemp;
    //@BindView(R.id.tvMaxTemp)
    private TextView tvMaxTemp;
    //@BindView(R.id.tvCurrentTemp)
    private TextView tvCurrentTemp;
    // @BindView(R.id.tvTemp)
    private TextView tvTemp;
    // @BindView(R.id.tvTempCondition)
    private TextView tvTempCondition;

    private APIController apiController;
    private ListView mForecastList;
    private LinearLayout llBottomLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionHandler.getInstance().requestCourseLocationPermission(this, PermissionHandler.PERMISSION_REQUEST_COURSE_LOCATION, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        clConditionBg = rootView.findViewById(R.id.clConditionBg);
        tvMinTemp = rootView.findViewById(R.id.tvMinTemp);
        tvMaxTemp = rootView.findViewById(R.id.tvMaxTemp);
        tvCurrentTemp = rootView.findViewById(R.id.tvCurrentTemp);
        tvTemp = rootView.findViewById(R.id.tvTemp);
        tvTempCondition = rootView.findViewById(R.id.tvTempCondition);
        mForecastList = rootView.findViewById(R.id.lvForecast);
        llBottomLayout = rootView.findViewById(R.id.llBottomLayout);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void getPermissionData(int permissionAction, int requestCode) {

        if (permissionAction == PermissionRequestCallbackListener.PERMISSION_GRANTED && requestCode == PermissionHandler.PERMISSION_REQUEST_FINE_LOCATION || requestCode == PermissionHandler.PERMISSION_REQUEST_COURSE_LOCATION) {

            LocationHandler mLocationHandler = LocationHandler.getInstance();
            mLocationHandler.setLocationListener(this);
            mLocationHandler.getLastLocation(getContext());
        }

    }


    @Override
    public void getLatLong(Location mLastLocation) {

        getWeatherData(mLastLocation);
    }

    private void getWeatherData(Location mLastLocation) {

        final HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.TAG_Lat, "" + mLastLocation.getLatitude());
        map.put(AppConstants.TAG_Long, "" + mLastLocation.getLongitude());
        map.put(AppConstants.TAG_appid, "" + AppConstants.APP_ID);

        apiController = new APIController(new TaskListener() {
            @Override
            public void onTaskSuccess(int taskId, BaseAPIResponse baseAPIResponse) {

                if (taskId == AppConstants.WEATHER && baseAPIResponse != null) {
                    WeatherModel[] weatherModel = baseAPIResponse.getRecords();
                    setWeatherData(weatherModel[0]);
                    apiController.makeRequest(getContext(), true, URLManager.URL_GET_WEATHER_FORECAST, map);
                } else if (taskId == AppConstants.WEATHER_FORCAST && baseAPIResponse != null) {
                    WeatherForecastModel[] weatherForecastModels = baseAPIResponse.getRecords();
                    setWeatherForecast(weatherForecastModels[0]);
                }

            }

            @Override
            public void onTaskFailure(int taskId, int errorCode) {

            }
        });
        apiController.makeRequest(getContext(), true, URLManager.URL_GET_WEATHER, map);

    }

    private void setWeatherForecast(WeatherForecastModel weatherForecastModel) {

        List<WeatherForecastModel.ForcastList> mForcastLists =  weatherForecastModel.getList();
        mForecastList.setAdapter(new ForecastAdapter(getContext(),mForcastLists));

    }

    private void setWeatherData(WeatherModel weatherModel) {

        tvTemp.setText("" + weatherModel.getMain().getTemp() + "\u00B0");
        tvTempCondition.setText(weatherModel.getWeather().get(0).getMain());
        tvMinTemp.setText("" + weatherModel.getMain().getTempMin() + "\u00B0");
        tvMaxTemp.setText("" + weatherModel.getMain().getTempMax() + "\u00B0");
        tvCurrentTemp.setText("" + weatherModel.getMain().getTemp() + "\u00B0");

        if (weatherModel.getWeather().get(0).getMain().contains("Clear")) {
            clConditionBg.setBackgroundResource(R.drawable.forest_sunny);
            llBottomLayout.setBackgroundColor(getContext().getResources().getColor(R.color.sunny));
        } else if (weatherModel.getWeather().get(0).getMain().contains("Cloud")) {
            clConditionBg.setBackgroundResource(R.drawable.forest_cloudy);
            llBottomLayout.setBackgroundColor(getContext().getResources().getColor(R.color.cloudy));
        } else if (weatherModel.getWeather().get(0).getMain().contains("rain")) {
            clConditionBg.setBackgroundResource(R.drawable.forest_rainy);
            llBottomLayout.setBackgroundColor(getContext().getResources().getColor(R.color.rainy));
        }

    }
}
