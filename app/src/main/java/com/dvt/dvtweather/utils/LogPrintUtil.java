package com.dvt.dvtweather.utils;

public class LogPrintUtil {
    private static LogPrintUtil ourInstance;

    public static LogPrintUtil getInstance() {
        if (ourInstance == null)
            ourInstance = new LogPrintUtil();
        return ourInstance;
    }

    private LogPrintUtil() {
    }

    public void printSysLog(String strMessage) {
        System.out.println("===" + strMessage);
    }
}
