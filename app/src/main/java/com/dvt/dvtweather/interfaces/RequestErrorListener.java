package com.dvt.dvtweather.interfaces;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by SharadW.
 */
public interface RequestErrorListener extends Response.ErrorListener
{
    void onRequestError(VolleyError error, int TaskId);
}
