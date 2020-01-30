package com.dvt.dvtweather.serverrequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.dvt.dvtweather.R;
import com.dvt.dvtweather.interfaces.APIResponseStatus;
import com.dvt.dvtweather.interfaces.RequestErrorListener;
import com.dvt.dvtweather.interfaces.RequestResponseListener;
import com.dvt.dvtweather.model.BaseAPIResponse;
import com.dvt.dvtweather.utils.AppHelper;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sharad.
 */
public class APIController implements RequestResponseListener, RequestErrorListener {
    private TaskListener mTaskListener;
    private final int MY_SOCKET_TIMEOUT_MS = 90000;// 90 seconds for socket connection timeout

    public APIController(TaskListener taskListener) {
        this.mTaskListener = taskListener;
    }

    @Override
    public void onRequestError(VolleyError error, int taskId) {
        BaseAPIResponse baseAPIResponse = new BaseAPIResponse();
        baseAPIResponse.setStatus(error.hashCode());
        baseAPIResponse.setStatusDescription(error.getMessage());
        baseAPIResponse.setIsError(1);
        mTaskListener.onTaskSuccess(taskId, baseAPIResponse);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("onErrorResponse", error.getMessage());
    }

    @Override
    public void onRequestResponse(BaseAPIResponse responseObject, int taskId) {
        mTaskListener.onTaskSuccess(taskId, responseObject);
    }

    @Override
    public void onResponse(Object response) {
        Log.e("onResponse", response.toString());
    }

    @SuppressWarnings("unchecked")
    public void makeRequest(Context context, boolean flagShowProgressDialog, final URLManager urlManager,
                            HashMap<String, String> params) {
        if (AppHelper.getInstance().checkInternet(context)) {
            try {
                VolleyArrayRequest mVolleyArrayRequest;
                if (urlManager.getRequestMethod() == Request.Method.GET && params != null) {
                    String strURL = urlManager.toString() + "?";
                    for (Map.Entry<String, ?> entry : params.entrySet()) {
                        strURL += entry.getKey() + "=" + String.valueOf(entry.getValue()) + "&";
                    }
                    strURL = strURL.substring(0, strURL.length() - 1);
                    strURL = strURL.replaceAll(" ", "%20");

                    mVolleyArrayRequest = new VolleyArrayRequest(flagShowProgressDialog, context, strURL, urlManager, APIController.this, APIController.this);
                } else
                    mVolleyArrayRequest = new VolleyArrayRequest(flagShowProgressDialog, context, urlManager, params == null ? null : new JSONObject(params).toString(), APIController.this, APIController.this);
                mVolleyArrayRequest.setShouldCache(false);
                mVolleyArrayRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                requestQueue.add(mVolleyArrayRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            returnInternetConnectivityResponse(context, urlManager.getTaskId());
        }
    }

    public void makeRequest(Context context, URLManager urlManager, boolean flagShowProgressDialog, JSONObject params) {
        if (AppHelper.getInstance().checkInternet(context)) {
            try {
                //noinspection unchecked
                VolleyArrayRequest mVolleyArrayRequest = new VolleyArrayRequest(flagShowProgressDialog, context, urlManager, params == null ? null : params.toString(), APIController.this, APIController.this);
                mVolleyArrayRequest.setShouldCache(false);
                mVolleyArrayRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                requestQueue.add(mVolleyArrayRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            returnInternetConnectivityResponse(context, urlManager.getTaskId());
        }
    }

    public void makeRequestPaymino(Context context,String URL, URLManager urlManager, boolean flagShowProgressDialog, JSONObject params) {
        if (AppHelper.getInstance().checkInternet(context)) {
            try {
                //noinspection unchecked
                VolleyArrayRequest mVolleyArrayRequest = new VolleyArrayRequest(flagShowProgressDialog, context,URL, urlManager, params == null ? null : params.toString(), APIController.this, APIController.this);
                mVolleyArrayRequest.setShouldCache(false);
                mVolleyArrayRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                requestQueue.add(mVolleyArrayRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            returnInternetConnectivityResponse(context, urlManager.getTaskId());
        }
    }
    /**
     * Method to return base API Response with status fail and
     *
     * @param context Context
     * @param taskId  Task Id
     */
    private void returnInternetConnectivityResponse(Context context, int taskId) {
        BaseAPIResponse baseAPIResponse = new BaseAPIResponse();
        baseAPIResponse.setStatus(APIResponseStatus.NETWORK_ERROR);
        if (context != null)
            baseAPIResponse.setStatusDescription(context.getString(R.string.server_error_message_no_contact_no));
        mTaskListener.onTaskSuccess(taskId, baseAPIResponse);
    }


}