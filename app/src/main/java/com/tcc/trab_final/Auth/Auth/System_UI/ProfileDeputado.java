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
import com.tcc.trab_final.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDeputado extends AppCompatActivity {

    private TextView nomeDeputadoTextView;
    private TextView partidoTextView;
    private TextView emailTextView;
    private TextView cpfTextView;
    private TextView dataNascimentoTextView;
    private TextView condicaoEleitoralTextView;
    private ImageView imagemDeputado;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_deputado);

        nomeDeputadoTextView = findViewById(R.id.nomeDeputado);
        partidoTextView = findViewById(R.id.partido);
        emailTextView = findViewById(R.id.nomeUser);
        cpfTextView = findViewById(R.id.cpf);
        dataNascimentoTextView = findViewById(R.id.dataNascimento);
        condicaoEleitoralTextView = findViewById(R.id.condicaoEleitoral);
        imagemDeputado = findViewById(R.id.imagemDeputado);

        int deputadoId = getIntent().getIntExtra("DEPUTADO_ID", 0);

        obterDetalhesDeputado(deputadoId);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return itemSelecionadoNavegacao(item);
            }
        });
    }

    private void obterDetalhesDeputado(int deputadoId) {
        ApiService apiService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = apiService.getDeputadoDetails(deputadoId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        preencherDetalhesDeputado(responseData);
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

    private void preencherDetalhesDeputado(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject dadosDeputado = json.getJSONObject("dados");
            JSONObject ultimoStatus = dadosDeputado.getJSONObject("ultimoStatus");
            JSONObject gabinete = ultimoStatus.getJSONObject("gabinete");

            String nome = dadosDeputado.getString("nomeCivil");
            String siglaPartido = ultimoStatus.getString("siglaPartido");
            String email = ultimoStatus.getString("email");
            String cpf = dadosDeputado.getString("cpf");
            String dataNascimentoString = dadosDeputado.getString("dataNascimento");
            String condicaoEleitoral = ultimoStatus.getString("condicaoEleitoral");
            String urlFoto = ultimoStatus.getString("urlFoto");

            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoDesejado = new SimpleDateFormat("dd/MM/yyyy");
            Date dataNascimento = formatoOriginal.parse(dataNascimentoString);
            String dataNascimentoFormatada = formatoDesejado.format(dataNascimento);

            nomeDeputadoTextView.setText(nome);
            partidoTextView.setText("Partido: " + siglaPartido);
            emailTextView.setText(email);
            cpfTextView.setText(cpf);
            dataNascimentoTextView.setText(dataNascimentoFormatada);
            condicaoEleitoralTextView.setText(condicaoEleitoral);

            Picasso.get().load(urlFoto).into(imagemDeputado);

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void handleApiError(Response<?> response) {
        Log.e("API", "Código de erro: " + response.code());
    }

    private void handleConnectionError(Throwable t) {
        Log.e("API", "Erro de conexão", t);

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
