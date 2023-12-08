package com.tcc.trab_final.Auth.Auth.API;

import com.tcc.trab_final.Auth.Auth.Models.Partido;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("partidos")
    Call<ResponseBody> obterPartidos();

    @GET("partidos/{id}")
    Call<ResponseBody> getPartidoDetails(@Path("id") int partidoId);

    @GET("deputados")
    Call<ResponseBody> obterDeputados();

    @GET("deputados/{id}")
    Call<ResponseBody> getDeputadoDetails(@Path("id") int deputadoId);

}
