package com.example.ciftci_projesi_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnTarlalar, btnDepolama, btnEkonomi,btnHayvanlar, btnAraclar;
    ImageButton btnBilgilerim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Butonları tanımla
        btnTarlalar = findViewById(R.id.btnTarlalar);
        btnDepolama = findViewById(R.id.btnDepolama);
        btnEkonomi = findViewById(R.id.btnEkonomi);
        btnHayvanlar = findViewById(R.id.btnHayvanlar);
        btnAraclar = findViewById(R.id.btnAraclar);
        btnBilgilerim = findViewById(R.id.btnBilgilerim);

        // Tıklama olayları
        btnTarlalar.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, TarlalarActivity.class);
            startActivity(i);
        });

        btnDepolama.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, DepolamaActivity.class);
            startActivity(i);
        });

        btnEkonomi.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, EkonomiActivity.class);
            startActivity(i);
        });

        btnHayvanlar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HayvanlarActivity.class);
            startActivity(intent);
        });

        btnAraclar.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, AraclarActivity.class);
            startActivity(i);
        });


        btnBilgilerim.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, BilgilerimActivity.class);
            startActivity(i);
        });
    }
}