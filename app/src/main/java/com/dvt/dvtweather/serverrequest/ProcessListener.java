package com.dvt.dvtweather.serverrequest;


import com.dvt.dvtweather.model.BaseAPIResponse;

/**
 * Created by SharadW.
 */
public interface ProcessListener {

    void onProcessSuccess(int taskID, BaseAPIResponse object);

    void onProcessFailed(int taskID, int errorCode);
}
