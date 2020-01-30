package com.dvt.dvtweather.interfaces;

/**
 * Interface for API response status
 */

public interface APIResponseStatus {
    int SUCCESS = 200;
    int NETWORK_ERROR = 404;
    int INVALID_RESPONSE = 401;
    int PRECONDITION_FAILED = 412;
    int REDIRECT = 302;
}