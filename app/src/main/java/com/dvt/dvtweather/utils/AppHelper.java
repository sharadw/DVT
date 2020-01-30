package com.dvt.dvtweather.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dvt.dvtweather.R;
import com.dvt.dvtweather.interfaces.AppConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;


/**
 * Created by Sharad .
 * Control Functionality App
 */
public class AppHelper {


    private Gson gson = new Gson();


    private static AppHelper ourInstance = null;

    public static AppHelper getInstance() {
        if (ourInstance == null)
            ourInstance = new AppHelper();
        return ourInstance;
    }

    private AppHelper() {
    }


    /**
     * method to hide soft keypad
     */
    public void hideSoftInputKeypad(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View focusView = activity.getCurrentFocus();
            if (focusView != null) {
                IBinder iBinder = focusView.getWindowToken();
                if (iBinder != null)
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
                    }
            }
        }
    }

    /**
     * Request focus to the View, so Soft keypad can be visible
     */
    public void requestFocusToView(Activity activity, View yourEditText) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }



    /**
     * Method to validate Mobile Number
     *
     * @param strMobileNumber Mobile Number
     * @return true if mobile number valid else return false
     */
    public boolean isMobileNumberValid(String strMobileNumber) {
        return !(TextUtils.isEmpty(strMobileNumber) || Long.parseLong(strMobileNumber) < 1000000000);
    }

    /**
     * method to convert object to string
     */
    public String convertObjectToString(Object object) {
        return gson.toJson(object);
    }

    public <T> T convertStringToObject(String strObjectString, Class<?> type) {
        //noinspection unchecked
        return TextUtils.isEmpty(strObjectString) ? null : (T) gson.fromJson(strObjectString, type);
    }


    /**
     * remove all fragments from Back Stack
     *
     * @param fragmentManager FragmentManager
     */
    public void clearBackStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    public void setLinkToTextView(TextView textView, String strText) {
        SpannableString spannableString = new SpannableString(strText);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        textView.setText(spannableString);
    }

    public void replaceFragment(FragmentActivity activity, Fragment fragment, boolean flagIsAddToBackStack) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_container, fragment, AppConstants.FRAGMENT_TAG);
        if (flagIsAddToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addFragment(FragmentActivity activity, Fragment fragment, boolean flagIsAddToBackStack) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_container, fragment, AppConstants.FRAGMENT_TAG);
        if (flagIsAddToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void removeFragment(Activity activity, Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
        fragmentManager.popBackStack();
    }


    /**
     * Method to replace the child fragment
     *
     * @param parentFragment       Parent Fragment
     * @param fragment             Fragment to be replace
     * @param containerId          Container/Holder View Id
     * @param strFragmentTag       Fragment Tag
     * @param flagIsAddToBackStack flag to add fragment to back stack or not
     */
    public void replaceChildFragment(Fragment parentFragment, Fragment fragment, int containerId, String strFragmentTag, boolean flagIsAddToBackStack) {
        FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment, strFragmentTag);
        if (flagIsAddToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Method to Show ToolBar back button visibility
     *
     * @param actionBar   ActionBar
     * @param flagVisible visible if true else invisible
     */
    @SuppressWarnings("SameParameterValue")
    @SuppressLint("RestrictedApi")
    public void showOrHideActionBarBackButton(ActionBar actionBar, boolean flagVisible) {
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(flagVisible);
            actionBar.setDisplayHomeAsUpEnabled(flagVisible);
            actionBar.setDefaultDisplayHomeAsUpEnabled(flagVisible);
        }
    }

    /**
     * Method to get version name of the app
     *
     * @param context Context
     * @return Version name
     */
    public String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void redirectToPlayStore(Context context, String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * This is used to check weather Internet is on or off
     *
     * @return true if internet is on else return false
     */
    @SuppressWarnings("deprecation")
    public boolean checkInternet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        // check if network is connected or device is in range
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This is used to show String Message
     *
     * @param message message String to show
     */
    public void showMessage(String message, Context context) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * This is used to show String Message
     *
     * @param messageResourceId message String to show
     */
    public void showMessage(int messageResourceId, Context context) {
        if (context != null)
            Toast.makeText(context, context.getString(messageResourceId), Toast.LENGTH_LONG).show();
    }

    /**
     * Method for calculate date difference between two dates
     *
     * @param strEnterDate  Input Date
     * @param strTodayDate  Today Date
     * @param strDateFormat Date Format
     * @return difference between two dates
     */
    public long getDifferenceBetweenTwoDates(String strEnterDate, String strTodayDate, String strDateFormat) {
        try {
            Date dateToday, dateEnter;
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(strDateFormat);// like yyyy-MM-dd HH:mm:ss
            dateToday = formatter.parse(strTodayDate);
            dateEnter = formatter.parse(strEnterDate);
            long timeOne = dateToday.getTime();
            long timeTwo = dateEnter.getTime();
            long oneDay = 1000 * 60 * 60 * 24; // calculate difference in days
            // long oneDay = 1000 * 60 * 60 ; // calculate difference in hours
            // long oneDay = 1000 * 60 ; // calculate difference in minutes
            // long oneDay = 1000; // calculate difference in Seconds

            return (timeTwo - timeOne) / oneDay;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param strInputDate    input date string
     * @param strSourceFormat input date format
     * @param strOutputFormat format of output date string
     * @return formatted date string
     */
    @SuppressLint("SimpleDateFormat")
    public String changeDateFormat(String strInputDate, String strSourceFormat, String strOutputFormat) {
        String desiredDateString = "";
        if (strInputDate != null && !strInputDate.equalsIgnoreCase("")) {
            SimpleDateFormat sourceFormat = new SimpleDateFormat(strSourceFormat);
            SimpleDateFormat desiredFormat = new SimpleDateFormat(strOutputFormat);
            try {
                Date input_date = sourceFormat.parse(strInputDate);
                desiredDateString = desiredFormat.format(input_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return desiredDateString;
    }

    /**
     * Method to get Current date in given pattern
     *
     * @param strDateFormat Date Format
     * @return current date in given format
     */
    @SuppressLint("SimpleDateFormat")
    public String getTodayDate(String strDateFormat) {
        String strDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat(strDateFormat);
        try {
            Calendar calendar = Calendar.getInstance();
            strDate = formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public Date getToday(String strDateFormat) {
        Date strDate = null;
        try {
            Calendar calendar = Calendar.getInstance();
            strDate = calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public String getFirstDayOfTheMonth() {
        int month;
        Calendar cal = Calendar.getInstance();
        int day = cal.getActualMinimum(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return year + "-" + month + "-" + day;
    }

    public String getLastDayOfTheMonth() {
        int month;
        Calendar cal = Calendar.getInstance();
        int day = cal.getActualMaximum(Calendar.DATE);
            month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return year + "-" + month + "-" + day;
    }

    public String getYear() {
        Calendar cal = Calendar.getInstance();
        return "" + cal.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar mCalendar = Calendar.getInstance();
        int month = mCalendar.get(Calendar.MONTH);
        return month;
    }

    public String getMonthAndYear() {
        Calendar mCalendar = Calendar.getInstance();
        String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        return month + " " + getYear();
    }

    public String getDateFormated(long dateInt) throws ParseException {
        String pattern = "EEEE MMMM";

        Timestamp stamp = new Timestamp(dateInt);
        Date date = new Date(stamp.getTime());
        String dateformated = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(date.getTime());
        return dateformated;
    }
    /**
     * Method to get date next or previous, if we pass 100 then it return date
     * after 100days
     *
     * @param next_or_prev 0 for today, -1 for yesterday,1 for tomorrow....
     * @return return desired date
     */
    @SuppressWarnings("SameParameterValue")
    @SuppressLint("SimpleDateFormat")
    public String getNextOrPreviousDateFromToday(int next_or_prev, String strDateFormat) {
        String strDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat(strDateFormat);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, next_or_prev);
            strDate = formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * Method to get date next or previous, if we pass 100 then it return date after 100days of the input date
     *
     * @param strInputDate  Input date
     * @param next_or_prev  next or previous integer value
     * @param strDateFormat Output date format
     * @return return desired date
     */
    @SuppressLint("SimpleDateFormat")
    public String getNextOrPreviousDateOfGivenDate(String strInputDate, int next_or_prev, String strDateFormat) {
        String strDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat(strDateFormat);
        try {
            Date date = new SimpleDateFormat(strDateFormat).parse(strInputDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, next_or_prev);
            strDate = formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * Method for convert hashMap string to HashMap
     *
     * @param strHashMap HashMap String
     * @return HashMap
     */
    public HashMap<String, String> convert_HashMapString_To_HashMap(String strHashMap) {
        TypeToken<HashMap<String, String>> token = new TypeToken<HashMap<String, String>>() {
        };
        return new Gson().fromJson(strHashMap, token.getType());
    }

    /**
     * method to format date from datePicker and change Date format
     *
     * @param year        year
     * @param monthOfYear month of year
     * @param dayOfMonth  day of month
     * @return Formatted date
     */
    private String getFormattedDate(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        String strMonth = monthOfYear + "", strDay = dayOfMonth + "";
        if (strMonth.length() == 1)
            strMonth = "0" + strMonth;
        if (strDay.length() == 1)
            strDay = "0" + strDay;
        return strDay + "-" + strMonth + "-" + year;
    }



    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isEmailValid(CharSequence target) {
//        return (target != null && target.length() == 0) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Method to show Unable to connect view
     *
     * @param vwNoInternetConnection Unable to connect view
     * @param vwMain                 Main Screen View
     * @param isRecordAvailable      show unable to connect view if true else hide the view
     */
    public void showOrHideUnableToConnectScreen(View vwNoInternetConnection, View vwMain, boolean isRecordAvailable) {
        if (isRecordAvailable) {
            vwNoInternetConnection.setVisibility(View.GONE);
            vwMain.setVisibility(View.VISIBLE);
        } else {
            vwNoInternetConnection.setVisibility(View.VISIBLE);
            vwMain.setVisibility(View.GONE);
        }
    }

    /**
     * Method to show Unable to connect view
     *
     * @param vwNoInternetConnection Unable to connect view
     * @param vwMain                 Main Screen View
     */
    public void showUnableToConnectScreen(View vwNoInternetConnection, View vwMain) {
        vwNoInternetConnection.setVisibility(View.VISIBLE);
        vwMain.setVisibility(View.GONE);
    }

    /**
     * Method to hide Unable to connect view
     *
     * @param vwNoInternetConnection Unable to connect view
     * @param vwMain                 Main Screen View
     */
    public void hideUnableToConnectScreen(View vwNoInternetConnection, View vwMain) {
        vwNoInternetConnection.setVisibility(View.GONE);
        vwMain.setVisibility(View.VISIBLE);
    }

    /**
     * Method to remove decimal places string
     *
     * @return String with integer value
     */
    public int convertDecimalStringToInteger(String strInputString) {
        if (!TextUtils.isEmpty(strInputString)) {
            if (strInputString.contains("."))
                return (int) Double.parseDouble(strInputString);
            else
                return Integer.parseInt(strInputString);
        }
        return 0;
    }

    public int getMonthScrolled(String month) {
        LinkedHashMap<String, Integer> mLinkedHashMap = new LinkedHashMap<>();
        mLinkedHashMap.put("January", 1);
        mLinkedHashMap.put("February", 2);
        mLinkedHashMap.put("March", 3);
        mLinkedHashMap.put("April", 4);
        mLinkedHashMap.put("May", 5);
        mLinkedHashMap.put("June", 6);
        mLinkedHashMap.put("July", 7);
        mLinkedHashMap.put("August", 8);
        mLinkedHashMap.put("September", 9);
        mLinkedHashMap.put("October", 10);
        mLinkedHashMap.put("November", 11);
        mLinkedHashMap.put("December", 12);

        return mLinkedHashMap.get(month);
    }


}