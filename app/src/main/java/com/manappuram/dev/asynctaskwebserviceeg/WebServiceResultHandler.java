package com.manappuram.dev.asynctaskwebserviceeg;

import org.json.JSONException;

public interface WebServiceResultHandler {
    void onReceiveResult(String result) throws JSONException;
}
