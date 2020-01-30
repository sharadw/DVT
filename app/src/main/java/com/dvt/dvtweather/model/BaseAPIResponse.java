package com.dvt.dvtweather.model;


import com.dvt.dvtweather.interfaces.APIResponseStatus;

public class BaseAPIResponse {
    private String statusDescription;

    private int status;

    private int isError;

    private Object records;

    private int recordsCount;

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsError() {
        return isError;
    }

    public void setIsError(int isError) {
        this.isError = isError;
    }

    public <T> T getRecords() {
        //noinspection unchecked
        return (T) records;
    }

    public void setRecords(Object records) {
        this.records = records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(int recordsCount) {
        this.recordsCount = recordsCount;
    }

    @Override
    public String toString() {
        return "ClassPojo [statusDescription = " + statusDescription + ", status = " + status + ", isError = " + isError + ", records = " + records + ", recordsCount = " + recordsCount + "]";
    }

    public boolean isStatusSuccess() {
        return status == APIResponseStatus.SUCCESS;
    }
}