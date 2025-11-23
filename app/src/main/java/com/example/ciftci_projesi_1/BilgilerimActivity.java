package com.example.ciftci_projesi_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BilgilerimActivity extends AppCompatActivity {

    Button btnKisiselBilgi, btnSifreDegistir, btnAnasayfa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgilerim);

        btnKisiselBilgi = findViewById(R.id.btnKisiselBilgi);
        btnSifreDegistir = findViewById(R.id.btnSifreDegistir);
        btnAnasayfa = findViewById(R.id.btnAnasayfa);

        btnKisiselBilgi.setOnClickListener(v -> {
            // Kişisel bilgi ekranı
        });

        btnSifreDegistir.setOnClickListener(v -> {
            // Şifre değiştirme ekranı
        });

        btnAnasayfa.setOnClickListener(v -> {
            Intent i = new Intent(BilgilerimActivity.this, MainActivity.class);
            startActivity(i);
        });
    }
}
