package com.dvt.dvtweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvt.dvtweather.R;
import com.dvt.dvtweather.model.WeatherForecastModel;
import com.dvt.dvtweather.utils.AppHelper;

import java.text.ParseException;
import java.util.List;

public class ForecastAdapter extends BaseAdapter {
    private List<WeatherForecastModel.ForcastList> mForcastLists;
    private Context mContext;
    private LayoutInflater inflater;

    public ForecastAdapter(Context context, List<WeatherForecastModel.ForcastList> mForcastLists) {
        this.mContext = context;
        this.mForcastLists = mForcastLists;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return mForcastLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        try {
            ViewHolder holder;


            if (view == null) {
                view = inflater.inflate(R.layout.row_forecast, null);
                holder = new ViewHolder();
                holder.tvDay = view.findViewById(R.id.tvDay);
                holder.ivTempIcon = view.findViewById(R.id.ivTempIcon);
                holder.tvDayTemp = view.findViewById(R.id.tvDayTemp);
                view.setTag(view);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            WeatherForecastModel.ForcastList mForcastList = mForcastLists.get(pos);

            int date = mForcastList.getDt();
            try {
                String day = AppHelper.getInstance().getDateFormated(date);
                holder.tvDay.setText(day);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (mForcastList.getWeather().get(0).getMain().contains("Clear")) {
                holder.ivTempIcon.setImageResource(R.drawable.clear);
            } else if (mForcastList.getWeather().get(0).getMain().contains("Cloud")) {
                holder.ivTempIcon.setImageResource(R.drawable.partlysunny);
            } else if (mForcastList.getWeather().get(0).getMain().contains("rain")) {
                holder.ivTempIcon.setImageResource(R.drawable.rain);
            }

            holder.tvDayTemp.setText("" + mForcastList.getMain().getTemp());
            return view;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static class ViewHolder {
        private TextView tvDay;
        private TextView tvDayTemp;
        private ImageView ivTempIcon;
    }
}
