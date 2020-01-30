package com.dvt.dvtweather.serverrequest;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dvt.dvtweather.interfaces.RequestResponseListener;
import com.dvt.dvtweather.model.BaseAPIResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.Map;

/**
 * Created by SharadW .
 */
public class GetPostRequest<T> extends Request<T> {

    /**
     * class of type of response
     */
    protected final Class<T> clazz;
    /**
     * JSON parsing engine
     */
    protected final Gson gson = null;
    private final int method;
    private final String url;
    /**
     * result listener
     */
    private final RequestResponseListener<T> responseListener;
    int MY_SOCKET_TIMEOUT_MS = 15000;
    String TAG = "GetPostRequest";
    private Map<String, String> headers;
    private Map<String, String> params;
    private int TaskId;

    public GetPostRequest(int method, String url, Map<String, String> headers, Map<String, String> params, RequestResponseListener<T> responseListener, Class<T> clazz, int TaskId) {
        super(method, url, responseListener);
        Log.i(TAG, url);
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.responseListener = responseListener;
        this.clazz = clazz;
        this.TaskId = TaskId;

        //set retry policies
        setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(jsonString, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        responseListener.onRequestResponse((BaseAPIResponse) response, TaskId);
    }

    @Override
    public void deliverError(VolleyError error) {
        responseListener.onRequestError(error, TaskId);
    }
}
