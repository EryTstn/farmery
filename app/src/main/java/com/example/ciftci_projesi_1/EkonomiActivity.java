package com.example.ciftci_projesi_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EkonomiActivity extends AppCompatActivity {

    Button btnGelirGider, btnRaporlar, btnAnasayfa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekonomi);

        btnGelirGider = findViewById(R.id.btnGelirGider);
        btnRaporlar = findViewById(R.id.btnRaporlar);
        btnAnasayfa = findViewById(R.id.btnAnasayfa);

        btnGelirGider.setOnClickListener(v -> {
            Intent intent = new Intent(EkonomiActivity.this, GelirGiderActivity.class);
            startActivity(intent);
        });


        btnRaporlar.setOnClickListener(v -> {
            Intent intent = new Intent(EkonomiActivity.this, EkonomiRaporActivity.class);
            startActivity(intent);
        });


        btnAnasayfa.setOnClickListener(v -> {
            Intent i = new Intent(EkonomiActivity.this, MainActivity.class);
            startActivity(i);
        });
    }
}
