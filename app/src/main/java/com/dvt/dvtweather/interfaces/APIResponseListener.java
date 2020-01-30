package com.dvt.dvtweather.interfaces;


import com.dvt.dvtweather.model.BaseAPIResponse;

/**
 * Created by SharadW.
 * Interface listener to listen API response
 */
public interface APIResponseListener
{
    void getAPIResponse(BaseAPIResponse baseEntity);
}
