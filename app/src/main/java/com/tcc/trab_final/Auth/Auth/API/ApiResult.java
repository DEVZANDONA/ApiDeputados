package com.tcc.trab_final.Auth.Auth.API;

import android.util.Log;

public class ApiResult {
    private String rawData;


    public ApiResult(String rawData) {
        this.rawData = rawData;
    }


    public void printRawData() {
        Log.d("ApiResult", "Raw API Response: " + rawData);
    }
}
