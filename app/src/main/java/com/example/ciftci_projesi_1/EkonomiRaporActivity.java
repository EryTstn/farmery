package com.example.ciftci_projesi_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class EkonomiRaporActivity extends AppCompatActivity {

    private Veritabani veritabani;
    private TextView textEkonomiOzet;
    private Button btnGeriDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekonomi_rapor);

        veritabani = new Veritabani(this);
        textEkonomiOzet = findViewById(R.id.textEkonomiOzet);
        btnGeriDon = findViewById(R.id.btnGeriDon);

        gosterUrunOzet();

        btnGeriDon.setOnClickListener(v -> finish());
    }

    private void gosterUrunOzet() {
        Map<String, Double> urunOzet = veritabani.getUrunOzet();

        if (urunOzet == null || urunOzet.isEmpty()) {
            textEkonomiOzet.setText("Kayıtlı tarla bulunamadı.");
            return;
        }

        StringBuilder sb = new StringBuilder("Tarlalardaki Ürün Özeti:\n\n");
        for (Map.Entry<String, Double> entry : urunOzet.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(" ton\n");
        }

        textEkonomiOzet.setText(sb.toString());
    }
}
