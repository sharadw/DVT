package com.dvt.dvtweather.interfaces;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dvt.dvtweather.model.BaseAPIResponse;

/**
 * Created by SharadW .
 */
public interface RequestResponseListener<T> extends Response.Listener, Response.ErrorListener {
    void onRequestResponse(BaseAPIResponse response, int taskId);

    void onRequestError(VolleyError error, int taskId);
}
