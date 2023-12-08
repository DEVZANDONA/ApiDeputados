package com.tcc.trab_final.Auth.Auth.System_UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tcc.trab_final.Auth.Auth.API.ApiResult;
import com.tcc.trab_final.Auth.Auth.API.ApiService;
import com.tcc.trab_final.Auth.Auth.API.RetrofitClient;
import com.tcc.trab_final.Auth.Auth.Adapters.DeputadoAdapter;
import com.tcc.trab_final.Auth.Auth.Auth.LoginActivity;
import com.tcc.trab_final.Auth.Auth.Models.Deputado;
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

public class DeputadoList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeputadoAdapter deputadoAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deputado_list);

        recyclerView = findViewById(R.id.recyclerViewDeputados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });
    }


    private void fetchData() {
        ApiService apiService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = apiService.obterDeputados();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);


                        List<Deputado> deputados = parseJson(responseData);
                        setupRecyclerView(deputados);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleConnectionError(t);
            }
        });
    }

    private List<Deputado> parseJson(String jsonString) {
        List<Deputado> deputados = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray dadosArray = json.getJSONArray("dados");

            for (int i = 0; i < dadosArray.length(); i++) {
                JSONObject deputadoJson = dadosArray.getJSONObject(i);

                // Extrair dados do deputado
                int id = deputadoJson.getInt("id");
                String uri = deputadoJson.getString("uri");
                String nome = deputadoJson.getString("nome");
                String siglaPartido = deputadoJson.getString("siglaPartido");
                String uriPartido = deputadoJson.getString("uriPartido");
                String siglaUf = deputadoJson.getString("siglaUf");
                int idLegislatura = deputadoJson.getInt("idLegislatura");
                String urlFoto = deputadoJson.getString("urlFoto");
                String email = deputadoJson.getString("email");

                // Criar objeto Deputado e adicioná-lo à lista
                Deputado deputado = new Deputado(id, uri, nome, siglaPartido, uriPartido, siglaUf, idLegislatura, urlFoto, email);
                deputados.add(deputado);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Lidar com erros de parsing JSON
        }

        return deputados;
    }

    private void setupRecyclerView(List<Deputado> deputados) {
        deputadoAdapter = new DeputadoAdapter(deputados, new DeputadoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Deputado deputado) {
                abrirDetalhesDeputado(deputado.getId());
            }
        });
        recyclerView.setAdapter(deputadoAdapter);
    }

    private void abrirDetalhesDeputado(int deputadoId) {
        Intent intent = new Intent(this, ProfileDeputado.class);
        intent.putExtra("DEPUTADO_ID", deputadoId);
        startActivity(intent);
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


    private boolean handleNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.sair) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (itemId == R.id.config) {
            startActivity(new Intent(this, ConfigPage.class));
            return true;
        } else if (itemId == R.id.partidos) {
            startActivity(new Intent(this, HomePage.class));
        }

        return false;
    }

}


