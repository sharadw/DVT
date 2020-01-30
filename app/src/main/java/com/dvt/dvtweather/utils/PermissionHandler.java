package com.dvt.dvtweather.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dvt.dvtweather.R;
import com.dvt.dvtweather.interfaces.PermissionRequestCallbackListener;

import java.security.Permissions;


public class PermissionHandler {
    @SuppressWarnings("WeakerAccess")
    public static final int REQUEST_RECORD_AUDIO = 1, PERMISSION_REQUEST_CAMERA = 2, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 3,
            PERMISSION_REQUEST_MAKE_CALL = 4, PERMISSION_REQUEST_READ_SMS = 5,PERMISSION_REQUEST_COURSE_LOCATION = 6,PERMISSION_REQUEST_FINE_LOCATION = 7;
    private static PermissionHandler ourInstance;

    public static PermissionHandler getInstance() {
        if (ourInstance == null)
            ourInstance = new PermissionHandler();
        return ourInstance;
    }

    private PermissionHandler() {
    }

    /**
     * Method to request Camera Permission with Write external storage permission
     *
     * @param context                           Context
     * @param requestCode                       Request Code
     * @param permissionRequestCallbackListener Callback Listener
     */
    public void requestCameraPermission(Fragment context, int requestCode, PermissionRequestCallbackListener permissionRequestCallbackListener) {
        requestMultiplePermission(context, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        }, context.getString(R.string.camera_permission_message), requestCode, permissionRequestCallbackListener);
    }

    /**
     * Method to request SMS Permissions
     *
     * @param context                           Context
     * @param requestCode                       Permission request code
     * @param permissionRequestCallbackListener Listener
     */
    public void requestSMSPermission(Fragment context, int requestCode, PermissionRequestCallbackListener permissionRequestCallbackListener) {
        requestMultiplePermission(context, new String[]{
                Manifest.permission.READ_SMS
        }, context.getString(R.string.sms_permission_message), requestCode, permissionRequestCallbackListener);
    }


    /**
     * Method to request COURSE_LOCATION Permissions
     *
     * @param context                           Context
     * @param requestCode                       Permission request code
     * @param permissionRequestCallbackListener Listener
     */
    public void requestCourseLocationPermission(Fragment context, int requestCode, PermissionRequestCallbackListener permissionRequestCallbackListener) {
        requestMultiplePermission(context, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, context.getString(R.string.location_permission_message), requestCode, permissionRequestCallbackListener);
    }


    public void requestFineLocationPermission(Fragment context, int requestCode, PermissionRequestCallbackListener permissionRequestCallbackListener) {
        requestMultiplePermission(context, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, context.getString(R.string.location_permission_message), requestCode, permissionRequestCallbackListener);
    }

    /**
     * Method to request permission for microphone for feedback recording
     *
     * @param context                           Context
     * @param requestCode                       Request Code
     * @param permissionRequestCallbackListener Callback listener
     */
    public void requestMicrophonePermission(Fragment context, int requestCode, PermissionRequestCallbackListener permissionRequestCallbackListener) {
        requestMultiplePermission(context, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
        }, context.getString(R.string.microphone_permission_message), requestCode, permissionRequestCallbackListener);
    }

    /**
     * Method to request Call Permission
     *
     * @param context                           Context
     * @param permissionRequestCallbackListener Callback Listener
     */
    void requestCallPermission(Fragment context, PermissionRequestCallbackListener permissionRequestCallbackListener) {
        requestMultiplePermission(context, new String[]{Manifest.permission.CALL_PHONE}, context.getString(R.string.grant_call_permission_message), PermissionHandler.PERMISSION_REQUEST_MAKE_CALL, permissionRequestCallbackListener);
    }

    /**
     * Method to request Read External Storage Permission
     *
     * @param context                           Context
     * @param requestCode                       Request Code
     * @param permissionRequestCallbackListener Callback Listener
     */
    public void requestReadExternalStoragePermission(Fragment context, int requestCode, PermissionRequestCallbackListener permissionRequestCallbackListener) {
        requestMultiplePermission(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, context.getString(R.string.read_external_storage_permission_message), requestCode, permissionRequestCallbackListener);
    }

    /**
     * Method to request permissions for
     *
     * @param fragment                          Fragment
     * @param requestedPermissions              Permission Array
     * @param strMessage                        Message to display to grant permission
     * @param requestCode                       Request Code
     * @param permissionRequestCallbackListener Callback
     */
    private void requestMultiplePermission(final Fragment fragment, final String[] requestedPermissions, String strMessage, final int requestCode, final PermissionRequestCallbackListener permissionRequestCallbackListener) {
        Context context = fragment.getContext();
        if (context != null) {
            int permissionCheck = PackageManager.PERMISSION_GRANTED;
            boolean shouldShowRequestPermissionRationale = false;
            for (String permission : requestedPermissions) {
                permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(context, permission);
                shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || fragment.shouldShowRequestPermissionRationale(permission);
            }

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale) {
                   //Todo show dialog
                } else
                    fragment.requestPermissions(requestedPermissions, requestCode);
            } else
                permissionRequestCallbackListener.getPermissionData(PermissionRequestCallbackListener.PERMISSION_GRANTED, requestCode);
        } else
            permissionRequestCallbackListener.getPermissionData(PermissionRequestCallbackListener.PERMISSION_CANCELLED, requestCode);
    }

    /**
     * Method to redirect to Setting screen for access app permissions
     *
     * @param context Context
     */
    public void redirectToSettingsScreen(Context context) {
        if (context != null) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
        }
    }
}