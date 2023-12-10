package com.tcc.trab_final.Auth.Auth.System_UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tcc.trab_final.R;

public class ConfigPage extends AppCompatActivity {

    private TextView nomeTextView, emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_page);

        nomeTextView = findViewById(R.id.nome);
        emailTextView = findViewById(R.id.senha);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            String nome = user.getDisplayName();
            String email = user.getEmail();

            nomeTextView.setText(nome);
            emailTextView.setText(email);
        } else {

        }
    }
}
