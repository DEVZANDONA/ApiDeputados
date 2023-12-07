package com.tcc.trab_final.Auth.Auth.System_UI;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcc.trab_final.Auth.Auth.API.ApiResult;
import com.tcc.trab_final.Auth.Auth.API.ApiService;
import com.tcc.trab_final.Auth.Auth.API.RetrofitClient;
import com.tcc.trab_final.Auth.Auth.Adapters.PartidoAdapter;
import com.tcc.trab_final.Auth.Auth.Models.Partido;
import com.tcc.trab_final.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PartidoAdapter partidoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    private void fetchData() {
        ApiService apiService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = apiService.obterPartidos();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);

                        // Processar os dados JSON
                        List<Partido> partidos = parseJson(responseData);

                        // Configurar o RecyclerView com os dados
                        setupRecyclerView(partidos);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Lidar com erros de leitura do corpo da resposta.
                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                handleConnectionError(t);
            }
        });
    }

            // Método para processar os dados JSON
            private List<Partido> parseJson(String jsonString) {
                List<Partido> partidos = new ArrayList<>();

                try {
                    JSONObject json = new JSONObject(jsonString);
                    JSONArray dadosArray = json.getJSONArray("dados");

                    for (int i = 0; i < dadosArray.length(); i++) {
                        JSONObject partidoJson = dadosArray.getJSONObject(i);

                        // Extrair dados do partido
                        int id = partidoJson.getInt("id");
                        String sigla = partidoJson.getString("sigla");
                        String nome = partidoJson.getString("nome");
                        String uri = partidoJson.getString("uri");

                        // Criar objeto Partido e adicioná-lo à lista
                        Partido partido = new Partido(id, sigla, nome, uri);
                        partidos.add(partido);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Lidar com erros de parsing JSON
                }

                return partidos;
            }


    private void setupRecyclerView(List<Partido> partidos) {
        partidoAdapter = new PartidoAdapter(partidos);
        recyclerView.setAdapter(partidoAdapter);
    }

    private void handleApiError(Response<?> response) {
        try {
            Log.e("API", "Código de erro: " + response.code());
            String errorBody = "Corpo da resposta de erro indisponível";

            if (response.errorBody() != null) {
                errorBody = response.errorBody().string();
                Log.e("API", "Corpo da resposta de erro: " + errorBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Toast removido
    }

    private void handleConnectionError(Throwable t) {
        Log.e("API", "Erro de conexão", t);
        // Toast removido
    }
}