package com.tcc.trab_final.Auth.Auth.API;

import android.util.Log;

public class ApiResult {
    private String rawData; // Para armazenar a resposta bruta da API

    // Construtor
    public ApiResult(String rawData) {
        this.rawData = rawData;
    }

    // Método para imprimir a resposta bruta
    public void printRawData() {
        Log.d("ApiResult", "Raw API Response: " + rawData);
    }
}
