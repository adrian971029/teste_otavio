package com.example.rotatecam.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rotatecam.R;

import java.io.File;
import java.io.FileNotFoundException;

public class ExibeFotoActivity extends AppCompatActivity {

    private ImageView mImage;
    private String path = "";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_foto);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        preferences = getSharedPreferences(Login.SHARED_PREFERENCES_DEFAULT, MODE_PRIVATE);

        mImage = findViewById(R.id.ivFoto);

        if (!preferences.getString(Login.LAST_FOTO, "").equals("")) {
            path = preferences.getString(Login.LAST_FOTO, "");
        }

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mImage.setImageBitmap(bitmap);
            mImage.setRotation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void actionCancelar(View view) {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) finish();
        }
    }

    public void actionSalvar(View view) {
        Toast.makeText(this, "Imagem salva!", Toast.LENGTH_SHORT).show();
        finish();
    }


    public void actionCompartilhar(View view) {
        try {
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(intent, "Compartilhar Imagem"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
