package com.dvt.dvtweather.serverrequest;


import com.dvt.dvtweather.model.BaseAPIResponse;

/**
 * Interface contains all task id and methods for task status
 * Created by SharadW.
 */
public interface TaskListener {

    int NETWORK_ERROR = 101;

    void onTaskSuccess(int taskId, BaseAPIResponse baseAPIResponse);

    void onTaskFailure(int taskId, int errorCode);

}
