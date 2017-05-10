package com.homechart.app.commont.utils;

import android.util.Log;

import com.android.volley.VolleyError;

/**
 * @author   jainar .
 * @version  v1.0 .
 * @date       2016-6-13 .
 * @file          MPNetworkUtils.java .
 * @brief        .
 */
public class MPNetworkUtils
{
    public static void logError(String tag, VolleyError error)
    {
        MPNetworkUtils.logError(tag, error, false);
    }

    public static void logError(String tag, String errorMessage)
    {
        String tagString = getTrimmedString(tag, kTAG_LIMIT);

        Log.e(tagString, errorMessage);
    }

    public static void logError(String tag, VolleyError error, boolean shouldLogData)
    {
        String logString = getLogString(error, shouldLogData);

        String tagString = getTrimmedString(tag, kTAG_LIMIT);

        Log.e(tagString, logString);
    }

    private static String getLogString(VolleyError error, boolean shouldLogData)
    {
        assert (error != null);

        String logString = null;

        if (error.networkResponse == null)
            logString = "Error response";
        else
        {
            if (shouldLogData)
            {
                try
                {
                    logString = "Error response with status code: " + error.networkResponse.statusCode +
                            " and data: " + new String(error.networkResponse.data);
                }
                catch (Exception ignored)
                {
                    // In case the data can't be casted into a string
                    logString = "Error response with status code: " + error.networkResponse.statusCode;
                }
            }
            else
                logString = "Error response with status code: " + error.networkResponse.statusCode;
        }

        String message = error.getMessage();

        if (message != null && !message.isEmpty())
            logString = logString + " with message: " + message;

        return logString;
    }


    private static String getTrimmedString(String inString, int maxLength)
    {
        if (maxLength < inString.length())
            return inString.substring(0, maxLength - 1);
        else
            return inString;
    }

    // If tag is greater than 23 characters, Log.isLoggable() throws an IllegalStateException
    private static final int kTAG_LIMIT = 23;
}
