package com.tcc.trab_final.Auth.Auth.System_UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

                        List<Partido> partidos = parseJson(responseData);

                        setupRecyclerView(partidos);
                    } catch (IOException e) {
                        e.printStackTrace();
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

            private List<Partido> parseJson(String jsonString) {
                List<Partido> partidos = new ArrayList<>();

                try {
                    JSONObject json = new JSONObject(jsonString);
                    JSONArray dadosArray = json.getJSONArray("dados");

                    for (int i = 0; i < dadosArray.length(); i++) {
                        JSONObject partidoJson = dadosArray.getJSONObject(i);

                        int id = partidoJson.getInt("id");
                        String sigla = partidoJson.getString("sigla");
                        String nome = partidoJson.getString("nome");
                        String uri = partidoJson.getString("uri");

                        Partido partido = new Partido(id, sigla, nome, uri);
                        partidos.add(partido);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return partidos;
            }


    private void setupRecyclerView(List<Partido> partidos) {
        partidoAdapter = new PartidoAdapter(partidos, new PartidoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Partido partido) {
                Intent intent = new Intent(HomePage.this, ProfilePartido.class);
                intent.putExtra("PARTIDO_ID", partido.getId());
                Log.d("RecyclerView", "ID do Partido clicado: " + partido.getId()); // Adicione este log
                startActivity(intent);
            }
        });
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
    }

    private void handleConnectionError(Throwable t) {
        Log.e("API", "Erro de conexão", t);
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
