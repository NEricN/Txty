package com.example.christophersumnicht.txty;

/**
 * Created by christophersumnicht on 10/4/14.
 */

import android.webkit.JavascriptInterface;

public class JavaToJSInterface {

    @JavascriptInterface
    public String getConsoleLine()
    {
        String consoleLine = ">";
        return consoleLine;
    }
}
