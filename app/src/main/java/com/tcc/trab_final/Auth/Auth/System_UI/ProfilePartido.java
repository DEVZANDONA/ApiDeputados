package com.tcc.trab_final.Auth.Auth.System_UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.tcc.trab_final.Auth.Auth.API.ApiService;
import com.tcc.trab_final.Auth.Auth.API.RetrofitClient;
import com.tcc.trab_final.Auth.Auth.Auth.LoginActivity;
import com.tcc.trab_final.Auth.Auth.Models.Partido;
import com.tcc.trab_final.Auth.Auth.Models.Status;
import com.tcc.trab_final.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePartido extends AppCompatActivity {

    private TextView situacaoTextView, numerosMembrosTextView, numeroLegislaturaTextView, totalPossesTextView, nomeLiderTextView,siglaLiderTextView;
    private ImageView detailImageView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_partido);

        situacaoTextView = findViewById(R.id.situacao);
        numerosMembrosTextView = findViewById(R.id.numerosMembros);
        numeroLegislaturaTextView = findViewById(R.id.numeroLesgislatura);
        totalPossesTextView = findViewById(R.id.totalPosses);
        detailImageView = findViewById(R.id.detailImage);
        nomeLiderTextView = findViewById(R.id.nomeLider);
        siglaLiderTextView = findViewById(R.id.sigla_partido);

        int partidoId = getIntent().getIntExtra("PARTIDO_ID", 0);

        fetchPartidoDetails(partidoId);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return itemSelecionadoNavegacao(item);
            }
        });
    }

    private void fetchPartidoDetails(int partidoId) {
        ApiService apiService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = apiService.getPartidoDetails(partidoId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);

                        Partido partido = parseJson(responseData);

                        updateViews(partido);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("API", "Erro ao processar a resposta da API");
                    }
                } else {
                    Log.e("API", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API", "Falha na requisição: " + t.getMessage());
            }
        });
    }

    private Partido parseJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject dadosObject = json.getJSONObject("dados");

            int id = dadosObject.getInt("id");
            String sigla = dadosObject.getString("sigla");
            String nome = dadosObject.getString("nome");
            String uri = dadosObject.getString("uri");

            Partido partido = new Partido(id, sigla, nome, uri);

            JSONObject statusObject = dadosObject.getJSONObject("status");
            Status status = new Status();
            status.setSituacao(statusObject.getString("situacao"));
            status.setTotalPosse(statusObject.getString("totalPosse"));
            status.setTotalMembros(statusObject.getString("totalMembros"));
            status.setIdLegislatura(statusObject.getString("idLegislatura"));

            if (statusObject.has("lider")) {
                JSONObject liderObject = statusObject.getJSONObject("lider");
                Status.Lider lider = new Status.Lider();
                lider.setNome(liderObject.getString("nome"));
                lider.setUrlFoto(liderObject.getString("urlFoto"));
                lider.setSiglaPartido(liderObject.getString("siglaPartido"));
                status.setLider(lider);
            }

            partido.setStatus(status);

            return partido;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void updateViews(Partido partido) {
        if (partido != null && partido.getStatus() != null) {
            Status status = partido.getStatus();
            situacaoTextView.setText(status.getSituacao());
            numerosMembrosTextView.setText(status.getTotalMembros());
            numeroLegislaturaTextView.setText(status.getIdLegislatura());
            totalPossesTextView.setText(status.getTotalPosse());

            if (status.getLider() != null) {
                String nomeLider = status.getLider().getNome();
                String siglaLider = status.getLider().getSiglaPartido();
                nomeLiderTextView.setText("Lider do Partido: "+nomeLider);
                siglaLiderTextView.setText("Sigla do Partido: "+siglaLider);
            }

            String urlFotoLider = "";
            if (status.getLider() != null) {
                urlFotoLider = status.getLider().getUrlFoto();
            }


            Picasso.get().load(urlFotoLider).into(detailImageView);
        }

    }

    private boolean itemSelecionadoNavegacao(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.sair) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (itemId == R.id.deputados) {
            startActivity(new Intent(this, DeputadoList.class));
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

