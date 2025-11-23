package com.example.ciftci_projesi_1;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class HayvanlarActivity extends AppCompatActivity {

    Spinner spinnerHayvan;
    TextView txtMevcut, txtHayvanOzet;
    EditText edtMiktar;
    Button btnEkle, btnCikar, btnGeri;
    Veritabani db;

    String secilenHayvan = "Koyun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hayvanlar);

        spinnerHayvan = findViewById(R.id.spinnerHayvan);
        txtMevcut = findViewById(R.id.txtMevcutAdet);
        edtMiktar = findViewById(R.id.edtMiktar);
        btnEkle = findViewById(R.id.btnEkle);
        btnCikar = findViewById(R.id.btnCikar);
        btnGeri = findViewById(R.id.btnGeri);
        txtHayvanOzet = findViewById(R.id.txtHayvanOzet);

        db = new Veritabani(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Koyun", "Tavuk", "İnek"});
        spinnerHayvan.setAdapter(adapter);

        spinnerHayvan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilenHayvan = parent.getItemAtPosition(position).toString();
                guncelleMevcutAdet();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnEkle.setOnClickListener(v -> {
            int adet = Integer.parseInt(edtMiktar.getText().toString().isEmpty() ? "0" : edtMiktar.getText().toString());
            db.guncelleHayvanAdet(secilenHayvan, db.getHayvanAdet(secilenHayvan) + adet);
            edtMiktar.setText("");
            guncelleMevcutAdet();
        });

        btnCikar.setOnClickListener(v -> {
            int adet = Integer.parseInt(edtMiktar.getText().toString().isEmpty() ? "0" : edtMiktar.getText().toString());
            db.guncelleHayvanAdet(secilenHayvan, db.getHayvanAdet(secilenHayvan) - adet);
            edtMiktar.setText("");
            guncelleMevcutAdet();
        });

        btnGeri.setOnClickListener(v -> finish());

        // Sayfa açıldığında da özet güncellensin
        guncelleMevcutAdet();
    }

    void guncelleMevcutAdet() {
        int mevcut = db.getHayvanAdet(secilenHayvan);
        txtMevcut.setText("Eldeki: " + mevcut + " adet");
        guncelleHayvanOzet();
    }

    void guncelleHayvanOzet() {
        StringBuilder sb = new StringBuilder();
        String[] hayvanlar = {"İnek", "Koyun", "Tavuk"};
        for (String hayvan : hayvanlar) {
            int adet = db.getHayvanAdet(hayvan);
            sb.append(hayvan).append(": ").append(adet).append(" adet\n");
        }
        txtHayvanOzet.setText(sb.toString().trim());
    }
}
