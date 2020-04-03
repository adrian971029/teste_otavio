package com.example.rotatecam.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rotatecam.R;
import com.example.rotatecam.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cadastro extends AppCompatActivity {

    private EditText nome;
    private EditText sobrenome;
    private EditText email;
    private EditText senha;
    private EditText confirmaSenha;
    private Button confirmaCadastro;
    private Usuario usuario;
    private String salvaSenha;
    private FirebaseAuth auth;
    private String usuarioID;
    private DatabaseReference reference;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        preferences = getSharedPreferences(Login.SHARED_PREFERENCES_DEFAULT, MODE_PRIVATE);

        nome = findViewById(R.id.edtNomeCadastro);
        sobrenome = findViewById(R.id.edtSobrenomeCadastro);
        email = findViewById(R.id.edtEmailCadastro);
        senha = findViewById(R.id.edtSenhaCadastro);
        confirmaSenha = findViewById(R.id.edtConfirmaSenha);
        confirmaCadastro = findViewById(R.id.btnConfirmaCadastro);
        usuario = new Usuario();

        reference = FirebaseDatabase.getInstance().getReference();

        confirmaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nome.getText().toString().equals("") || sobrenome.getText().toString().equals("") || email.getText().toString().equals("") || senha.getText().toString().equals("") || confirmaSenha.getText().toString().equals("")) {
                    Toast.makeText(Cadastro.this, "Favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else if (!validaEmail(email.getText().toString())) {
                    Toast.makeText(Cadastro.this, "E-mail inválido", Toast.LENGTH_SHORT).show();
                } else {
                    if (senha.getText().toString().equals(confirmaSenha.getText().toString())) {
                        usuario.setNome(nome.getText().toString());
                        usuario.setSobrenome(sobrenome.getText().toString());
                        usuario.setEmail(email.getText().toString());
                        salvaSenha = senha.getText().toString();

                        cadastraUsuario();
                        Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Cadastro.this, "As senhas não coincidem, verifique!", Toast.LENGTH_SHORT).show();
                        senha.requestFocus();
                        confirmaSenha.requestFocus();
                    }
                }
            }
        });
    }

    public void voltarParaLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    void abreCamera() {
        Intent intent = new Intent(Cadastro.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }

    //Valida Email
    private boolean validaEmail(String mail) {
        if (TextUtils.isEmpty(mail)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches();
        }
    }

    //cadastra o usuario
    private void cadastraUsuario() {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), salvaSenha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    usuarioID = user.getUid();
                    usuario.setId(user.getUid());
                    salvaDados();
                    if (preferences.getBoolean(Login.IS_FIRST_TIME, true)) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Login.IS_FIRST_TIME, false);
                        editor.apply();
                        abreCamera();
                    } else {
                        abreCamera();
                    }
                } else {
                    Toast.makeText(Cadastro.this, "Erro!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //limpa os campos  salva os dados
    private void salvaDados() {
        usuario.Salva(); //salva o usuario
        nome.setText("");
        sobrenome.setText("");
        email.setText("");
        senha.setText("");
        confirmaSenha.setText("");
    }
}
