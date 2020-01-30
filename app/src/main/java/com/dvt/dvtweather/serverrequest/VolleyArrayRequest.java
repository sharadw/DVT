package com.dvt.dvtweather.serverrequest;

import android.content.Context;
import android.view.WindowManager;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.dvt.dvtweather.R;
import com.dvt.dvtweather.interfaces.APIResponseStatus;
import com.dvt.dvtweather.interfaces.AppConstants;
import com.dvt.dvtweather.interfaces.RequestErrorListener;
import com.dvt.dvtweather.interfaces.RequestResponseListener;
import com.dvt.dvtweather.model.BaseAPIResponse;
import com.dvt.dvtweather.utils.AppHelper;
import com.dvt.dvtweather.utils.LogPrintUtil;
import com.dvt.dvtweather.utils.ProgressDialogHelper;

import org.json.JSONArray;


/**
 * Created by SharadW .
 */
class VolleyArrayRequest<T> extends JsonRequest<T> {

    private final RequestErrorListener errorListener;

    private final RequestResponseListener<T> responseListener;
    private Class clazz;
    private int taskId;
    private boolean flagShowProgressDialog = false;
    private Context context;
    private String strUserName, strPassword;
    private ProgressDialogHelper mProgressDialogHelper;

    //Method for Post API Request
    VolleyArrayRequest(boolean flagShowProgressDialog, Context context, URLManager urlManager, String jsonObject, RequestErrorListener errorListener, RequestResponseListener<T> responseListener) {
        //noinspection unchecked
        super(urlManager.getRequestMethod(), urlManager.toString(), jsonObject, responseListener, errorListener);
        LogPrintUtil.getInstance().printSysLog("URL:" + urlManager.toString());
        LogPrintUtil.getInstance().printSysLog("PARAMETERS:" + jsonObject);
        LogPrintUtil.getInstance().printSysLog("TASK_ID:" + urlManager.getTaskId());
        this.errorListener = errorListener;
        this.responseListener = responseListener;
        this.flagShowProgressDialog = flagShowProgressDialog;
        this.context = context;
        clazz = urlManager.getClazz();
        taskId = urlManager.getTaskId();

        if (flagShowProgressDialog)
            showProgressDialog(context, AppConstants.EMPTY, AppConstants.EMPTY);
    }

    //Method for Post API Request with String URL
    VolleyArrayRequest(boolean flagShowProgressDialog, Context context, String URL, URLManager urlManager, String jsonObject, RequestErrorListener errorListener, RequestResponseListener<T> responseListener) {
        //noinspection unchecked
        super(urlManager.getRequestMethod(), URL, jsonObject, responseListener, errorListener);
        LogPrintUtil.getInstance().printSysLog("URL:" + urlManager.toString());
        LogPrintUtil.getInstance().printSysLog("PARAMETERS:" + jsonObject);
        LogPrintUtil.getInstance().printSysLog("TASK_ID:" + urlManager.getTaskId());
        this.errorListener = errorListener;
        this.responseListener = responseListener;
        this.flagShowProgressDialog = flagShowProgressDialog;
        this.context = context;
        clazz = urlManager.getClazz();
        taskId = urlManager.getTaskId();
        if (flagShowProgressDialog)
            showProgressDialog(context, AppConstants.EMPTY, AppConstants.EMPTY);
    }

    //Method For Get API Request
    VolleyArrayRequest(boolean flagShowProgressDialog, Context context, String strURL, URLManager urlManager, RequestErrorListener errorListener, RequestResponseListener<T> responseListener) {
        //noinspection unchecked
        super(urlManager.getRequestMethod(), strURL, null, responseListener, errorListener);
        LogPrintUtil.getInstance().printSysLog("URL:" + strURL);
        LogPrintUtil.getInstance().printSysLog("TASK_ID:" + urlManager.getTaskId());
        this.errorListener = errorListener;
        this.responseListener = responseListener;
        this.flagShowProgressDialog = flagShowProgressDialog;
        this.context = context;
        clazz = urlManager.getClazz();
        taskId = urlManager.getTaskId();
        if (flagShowProgressDialog)
            showProgressDialog(context, AppConstants.EMPTY, AppConstants.EMPTY);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LogPrintUtil.getInstance().printSysLog("Response:" + json);

            BaseAPIResponse baseAPIResponse = new BaseAPIResponse();
            JSONArray jsonObject = null;
            try {
                if (json != null && json.length() > 0 && "{".equalsIgnoreCase(json.substring(0, 1))) {
                    json = "[" + json + "]";
                    jsonObject = new JSONArray(json);
                } else if (json != null && json.length() > 0) {
                    jsonObject = new JSONArray(json);
                }


                if (jsonObject != null && clazz != null) {
                    baseAPIResponse.setRecords(AppHelper.getInstance().convertStringToObject(jsonObject.toString(), clazz));
                }

                //noinspection unchecked
                return Response.success((T) baseAPIResponse, HttpHeaderParser.parseCacheHeaders(response));
            } catch (Exception e) {
                e.printStackTrace();
                return Response.error(new ParseError(e));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        dismissProgressDialog();
        responseListener.onRequestResponse((BaseAPIResponse) response, taskId);
    }

    @Override
    public void deliverError(VolleyError error) {
        dismissProgressDialog();
        BaseAPIResponse baseAPIResponse = new BaseAPIResponse();
        baseAPIResponse.setStatus(APIResponseStatus.INVALID_RESPONSE);
        baseAPIResponse.setStatusDescription(context.getString(R.string.server_error_message_no_contact_no));
        responseListener.onRequestResponse(baseAPIResponse, taskId);
//        if (flagShowProgressDialog)
//            AppHelper.getInstance().showMessage(R.string.Unable_to_connect_please_try_again_later, context);
//        errorListener.onRequestError(error, taskId);
    }

    private void showProgressDialog(Context context, String title, String message) {
        try {
            mProgressDialogHelper = new ProgressDialogHelper(context);
            mProgressDialogHelper.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    private void dismissProgressDialog() {
        try {
            if (mProgressDialogHelper != null)
                mProgressDialogHelper.hide();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap();
        // add headers <key,value>
        String credentials = strUserName + ":" + strPassword;
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        headers.put("Authorization", auth);
        return headers;
    }*/
}