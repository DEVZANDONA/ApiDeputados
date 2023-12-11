package com.tcc.trab_final.Auth.Auth.System_UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.trab_final.Auth.Auth.Auth.LoginActivity;
import com.tcc.trab_final.R;

public class ConfigPage extends AppCompatActivity {

    private TextView nomeTextView, emailTextView;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_page);

        nomeTextView = findViewById(R.id.nomeUser);
        emailTextView = findViewById(R.id.emailUser);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            carregarUsuarioFirestore(user.getUid());
        }

        CardView cardDelete = findViewById(R.id.cardDelete);
        CardView cardModificarNome = findViewById(R.id.cardModificarNome);
        CardView cardModificarSenha = findViewById(R.id.cardModificarSenha);
        CardView cardModificarEmail = findViewById(R.id.cardModificarEmail);

        cardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletarConta();
            }
        });

        cardModificarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarNome();
            }
        });

        cardModificarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarSenha();
            }
        });

        cardModificarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    modificarEmail();
                } else {
                    showToast("Usuário não está logado.");
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return itemSelecionadoNavegacao(item);
            }
        });
    }

    private void carregarUsuarioFirestore(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Usuarios").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String nome = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");

                            nomeTextView.setText(nome);
                            emailTextView.setText(email);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Erro ao obter dados do usuário", e);
                    }
                });
    }

    private void deletarConta() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        deletarUserDb(user.getUid());
                    } else {
                        showToast("Erro ao excluir conta");
                    }
                }
            });
        }
    }

    private void deletarUserDb(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Usuarios").document(uid)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToast("Conta excluída com sucesso");
                        } else {
                            showToast("Erro ao excluir dados do usuário");
                        }
                    }
                });
    }

    private void modificarNome() {
        mostrarDialog("Modificar Nome", "Novo Nome", new InputDialogCallback() {
            @Override
            public void onInputEntered(String userInput) {
                if (!userInput.isEmpty()) {
                    updateUserName(userInput);
                } else {
                    showToast("Por favor, insira um novo nome válido.");
                }
            }
        });
    }

    private void modificarSenha() {
        mostrarDialog("Modificar Senha", "Nova Senha", new InputDialogCallback() {
            @Override
            public void onInputEntered(String userInput) {
                if (!userInput.isEmpty()) {
                    updateUserPassword(userInput);
                } else {
                    showToast("Por favor, insira uma nova senha válida.");
                }
            }
        });
    }

    private void modificarEmail() {
        mostrarDialog("Modificar Email", "Novo Email", new InputDialogCallback() {
            @Override
            public void onInputEntered(String userInput) {
                if (!userInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userInput).matches()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String oldEmail = user.getEmail();
                        updateUserEmail(userInput, oldEmail);
                    } else {
                        showToast("Erro ao obter informações do usuário");
                    }
                } else {
                    showToast("Por favor, insira um novo email válido.");
                }
            }
        });
    }


    private void mostrarDialog(String title, String hint, final InputDialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(hint);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString().trim();
                callback.onInputEntered(userInput);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private interface InputDialogCallback {
        void onInputEntered(String userInput);
    }

    private void updateUserName(String newUserName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Usuarios").document(user.getUid())
                    .update("name", newUserName)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast("Nome do usuário modificado com sucesso");
                                nomeTextView.setText(newUserName);
                            } else {
                                showToast("Erro ao modificar o nome do usuário no Firestore");
                            }
                        }
                    });
        }
    }

    private void updateUserPassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showToast("Senha do usuário modificada com sucesso");
                    } else {
                        showToast("Erro ao modificar a senha do usuário");
                    }
                }
            });
        }
    }

    private void updateUserEmail(final String newEmail, final String oldEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail().equals(oldEmail)) {
            user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showToast("Email do usuário modificado com sucesso");

                        Log.d("Firestore", "Atualização de e-mail no Firebase Authentication concluída com sucesso");

                        updateEmailInFirestore(newEmail, oldEmail);
                    } else {
                        showToast("Erro ao modificar o email do usuário no Firebase Authentication");

                        Log.e("Firestore", "Erro ao atualizar o e-mail no Firebase Authentication", task.getException());
                    }
                }
            });
        } else {
            showToast("Erro: Usuário nulo ou e-mail alterado por outra ação.");
        }
    }



    private void updateEmailInFirestore(final String newEmail, final String oldEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Usuarios").document(user.getUid())
                    .update("email", newEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast("Email do usuário atualizado no Firestore com sucesso");

                                emailTextView.setText(newEmail);
                            } else {
                                showToast("Erro ao atualizar o email do usuário no Firestore");
                            }
                        }
                    });
        }
    }

    private void showToast(String message) {
        Toast.makeText(ConfigPage.this, message, Toast.LENGTH_SHORT).show();
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
