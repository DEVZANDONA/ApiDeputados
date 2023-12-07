package com.tcc.trab_final.Auth.Auth.API;

import com.tcc.trab_final.Auth.Auth.Models.Partido;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("partidos")
    Call<ResponseBody> obterPartidos();
}
