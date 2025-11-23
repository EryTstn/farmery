package com.example.ciftci_projesi_1;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class GelirGiderActivity extends AppCompatActivity {

    Spinner spinnerGelir, spinnerGider;
    EditText editGelirMiktar, editGiderMiktar;
    Button btnGelirEkle, btnGelirCikar, btnGiderEkle, btnGiderCikar, btnGeriDon;
    TextView txtToplamGelir, txtToplamGider, txtKarZarar, txtSeciliGelir, txtSeciliGider;

    HashMap<String, Double> gelirHaritasi = new HashMap<>();
    HashMap<String, Double> giderHaritasi = new HashMap<>();

    String[] gelirKalemleri = {"Tahıl Geliri", "Hayvansal Ürün Geliri"};
    String[] giderKalemleri = {"Gübre", "İlaç", "Mazot", "Yem"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelir_gider);

        // XML öğelerini bağla
        spinnerGelir = findViewById(R.id.spinnerGelir);
        spinnerGider = findViewById(R.id.spinnerGider);
        editGelirMiktar = findViewById(R.id.editGelirMiktar);
        editGiderMiktar = findViewById(R.id.editGiderMiktar);
        btnGelirEkle = findViewById(R.id.btnGelirEkle);
        btnGelirCikar = findViewById(R.id.btnGelirCikar);
        btnGiderEkle = findViewById(R.id.btnGiderEkle);
        btnGiderCikar = findViewById(R.id.btnGiderCikar);
        btnGeriDon = findViewById(R.id.btnGeriDon);
        txtToplamGelir = findViewById(R.id.txtToplamGelir);
        txtToplamGider = findViewById(R.id.txtToplamGider);
        txtKarZarar = findViewById(R.id.txtKarZarar);
        txtSeciliGelir = findViewById(R.id.txtSeciliGelir);
        txtSeciliGider = findViewById(R.id.txtSeciliGider);

        // Spinner içerikleri
        spinnerGelir.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gelirKalemleri));
        spinnerGider.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, giderKalemleri));

        // Spinner değişimlerini dinle
        spinnerGelir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                guncelleSeciliGelirGorunumu();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerGider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                guncelleSeciliGiderGorunumu();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Buton olayları
        btnGelirEkle.setOnClickListener(v -> {
            gelirDegistir(true);
            editGelirMiktar.setText("");
        });

        btnGelirCikar.setOnClickListener(v -> {
            gelirDegistir(false);
            editGelirMiktar.setText("");
        });

        btnGiderEkle.setOnClickListener(v -> {
            giderDegistir(true);
            editGiderMiktar.setText("");
        });

        btnGiderCikar.setOnClickListener(v -> {
            giderDegistir(false);
            editGiderMiktar.setText("");
        });

        btnGeriDon.setOnClickListener(v -> finish());

        // İlk görünüm
        guncelleGorunum();
    }

    void gelirDegistir(boolean ekle) {
        try {
            double miktar = Double.parseDouble(editGelirMiktar.getText().toString());
            String secili = spinnerGelir.getSelectedItem().toString();
            double mevcut = gelirHaritasi.getOrDefault(secili, 0.0);
            if (!ekle) miktar = -miktar;
            double yeni = mevcut + miktar;
            if (yeni < 0) yeni = 0;
            gelirHaritasi.put(secili, yeni);
            guncelleGorunum();
            guncelleSeciliGelirGorunumu();
        } catch (Exception e) {
            Toast.makeText(this, "Lütfen geçerli gelir miktarı girin", Toast.LENGTH_SHORT).show();
        }
    }

    void giderDegistir(boolean ekle) {
        try {
            double miktar = Double.parseDouble(editGiderMiktar.getText().toString());
            String secili = spinnerGider.getSelectedItem().toString();
            double mevcut = giderHaritasi.getOrDefault(secili, 0.0);
            if (!ekle) miktar = -miktar;
            double yeni = mevcut + miktar;
            if (yeni < 0) yeni = 0;
            giderHaritasi.put(secili, yeni);
            guncelleGorunum();
            guncelleSeciliGiderGorunumu();
        } catch (Exception e) {
            Toast.makeText(this, "Lütfen geçerli gider miktarı girin", Toast.LENGTH_SHORT).show();
        }
    }

    void guncelleGorunum() {
        double toplamGelir = 0;
        double toplamGider = 0;

        for (double val : gelirHaritasi.values()) toplamGelir += val;
        for (double val : giderHaritasi.values()) toplamGider += val;

        txtToplamGelir.setText("Toplam Gelir: " + toplamGelir + " TL");
        txtToplamGider.setText("Toplam Gider: " + toplamGider + " TL");

        double fark = toplamGelir - toplamGider;
        if (fark > 0) {
            txtKarZarar.setText((int) fark + " TL Kar");
        } else if (fark < 0) {
            txtKarZarar.setText((int) (-fark) + " TL Zarar");
        } else {
            txtKarZarar.setText("Kâr/Zarar: 0 TL");
        }
    }

    void guncelleSeciliGelirGorunumu() {
        String secili = spinnerGelir.getSelectedItem().toString();
        double miktar = gelirHaritasi.getOrDefault(secili, 0.0);
        txtSeciliGelir.setText(secili + ": " + miktar + " TL");
    }

    void guncelleSeciliGiderGorunumu() {
        String secili = spinnerGider.getSelectedItem().toString();
        double miktar = giderHaritasi.getOrDefault(secili, 0.0);
        txtSeciliGider.setText(secili + ": " + miktar + " TL");
    }
}
