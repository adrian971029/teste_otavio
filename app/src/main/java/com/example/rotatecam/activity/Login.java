package com.example.rotatecam.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rotatecam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Button login;
    private Button cadastrar;
    private EditText email;
    private EditText senha;
    private FirebaseAuth auth;
    private SharedPreferences preferences;
    public static final String SHARED_PREFERENCES_DEFAULT = "preferencesDefault";
    public static final String IS_FIRST_TIME = "isFirstTime";
    public static final String LAST_FOTO = "lastFoto";
    private final int PERMISSAO_REQUEST = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(SHARED_PREFERENCES_DEFAULT, MODE_PRIVATE);

        login = findViewById(R.id.btnLogin);
        cadastrar = findViewById(R.id.btnCadastrar);
        email = findViewById(R.id.edtEmail);
        senha = findViewById(R.id.edtSenha);

        configuracaoInicial();

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreCadastro();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("") && !senha.getText().toString().equals("")) {
                    auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (preferences.getBoolean(IS_FIRST_TIME, true)) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean(IS_FIRST_TIME, false);
                                    editor.apply();
                                    abreCamera();
                                } else {
                                    abreCamera();
                                }
                                Toast.makeText(Login.this, "Login feito com sucesso!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        String[] permissao = {
                "android.permission.CAMERA",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.RECORD_AUDIO",
                "android.permission.READ_EXTERNAL_STORAGE"
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissao, 0);
        } else {
            ActivityCompat.requestPermissions(this, permissao, PERMISSAO_REQUEST);
        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
//            }
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Você nçao pode usar a câmera sem permissão!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    void abreCadastro() {
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
        finish();
    }

    void abreCamera() {
        Intent intent = new Intent(Login.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }

    void configuracaoInicial() {
        if (!preferences.getBoolean(IS_FIRST_TIME, true)) {
            abreCamera();
        }
    }
}
