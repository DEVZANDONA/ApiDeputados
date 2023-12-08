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

import com.tcc.trab_final.Auth.Auth.API.ApiResult;
import com.tcc.trab_final.Auth.Auth.API.ApiService;
import com.tcc.trab_final.Auth.Auth.API.RetrofitClient;
import com.tcc.trab_final.Auth.Auth.Adapters.DeputadoAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deputado_list);

        recyclerView = findViewById(R.id.recyclerViewDeputados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
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

                        // Processar os dados JSON
                        List<Deputado> deputados = parseJson(responseData);

                        // Configurar o RecyclerView com os dados
                        setupRecyclerView(deputados);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Lidar com erros de leitura do corpo da resposta.
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
                // Implemente o que deseja fazer ao clicar em um item da lista
                // Exemplo: abrir detalhes do deputado
                Toast.makeText(DeputadoList.this, "Clicou em " + deputado.getNome(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(deputadoAdapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }

    private static final int SAIR_ID = 1;
    private static final int DEPUTADOS_ID = 2;
    private static final int PARTIDOS_ID = 3;
    private static final int CONFIG_ID = 4;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.sair) {
            // Lógica quando o item "Sair" for selecionado
            return true;
        } else if (itemId == R.id.deputados) {
            startActivity(new Intent(this, DeputadoList.class));
            return true;
        } else if (itemId == R.id.partidos) {
            startActivity(new Intent(this, HomePage.class));
            return true;
        } else if (itemId == R.id.config) {
            // Lógica quando o item "Configurações" for selecionado
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


