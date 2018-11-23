package com.manappuram.dev.asynctaskwebserviceeg;

import org.json.JSONException;

public interface WebServiceResultHandler {
    void onReceiveResult(int who, String result) throws JSONException;
}
