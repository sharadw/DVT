package com.dvt.dvtweather.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dvt.dvtweather.R;
import com.dvt.dvtweather.fragment.DVTWeatherFragment;
import com.dvt.dvtweather.utils.AppHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
       AppHelper.getInstance().addFragment(this,new DVTWeatherFragment(),false);
    }
}
