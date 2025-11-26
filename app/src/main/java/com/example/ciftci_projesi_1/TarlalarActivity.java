package com.example.ciftci_projesi_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TarlalarActivity extends AppCompatActivity {

    Button btnTarlaEkle, btnTarlalarim, btnHarita, btnTKGM, btnAnasayfa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarlalar);

        btnTarlaEkle = findViewById(R.id.btnTarlaEkle);
        btnTarlalarim = findViewById(R.id.btnTarlalarim);
        btnHarita = findViewById(R.id.btnHarita);
        btnTKGM = findViewById(R.id.btnTKGM);
        btnAnasayfa = findViewById(R.id.btnAnasayfa);

        btnTarlaEkle.setOnClickListener(v -> {
            Intent i = new Intent(TarlalarActivity.this, TarlaEkleActivity.class);
            startActivity(i);
        });

        btnTarlalarim.setOnClickListener(v -> {
            Intent intent = new Intent(TarlalarActivity.this, TarlaListeleActivity.class);
            startActivity(intent);
        });

        btnHarita.setOnClickListener(v -> {
            Intent haritaIntent = new Intent(TarlalarActivity.this, HaritaActivity.class);
            startActivity(haritaIntent);
        });

        btnTKGM.setOnClickListener(v -> {
            Intent tkgmIntent = new Intent(TarlalarActivity.this, TKGMWebViewActivity.class);
            startActivity(tkgmIntent);
        });

        btnAnasayfa.setOnClickListener(v -> {
            Intent intent = new Intent(TarlalarActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
