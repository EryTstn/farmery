package com.example.ciftci_projesi_1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TarlaDuzenleActivity extends AppCompatActivity {

    EditText edtIsim, edtAlan, edtEkimTarihi, edtHasatTarihi, edtMiktar;
    Spinner spinnerUrun;
    Button btnGuncelle, btnGeriDon;
    Veritabani veritabani;
    int tarlaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarla_duzenle);

        edtIsim = findViewById(R.id.edtIsim);
        edtAlan = findViewById(R.id.edtAlan);
        spinnerUrun = findViewById(R.id.spinnerUrun);
        edtEkimTarihi = findViewById(R.id.edtEkimTarihi);
        edtHasatTarihi = findViewById(R.id.edtHasatTarihi);
        edtMiktar = findViewById(R.id.edtMiktar);
        btnGuncelle = findViewById(R.id.btnGuncelle);
        btnGeriDon = findViewById(R.id.btnGeriDon);

        veritabani = new Veritabani(this);

        // Spinner ürünler
        String[] urunler = {"Arpa", "Buğday", "Çavdar", "Yulaf", "Mısır", "Kanola", "Pancar", "Patates", "Pirinç"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, urunler);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUrun.setAdapter(adapter);

        // Intent'ten gelen veriler
        Intent intent = getIntent();
        tarlaId = intent.getIntExtra("tarlaId", -1);
        String isim = intent.getStringExtra("isim");
        double alan = intent.getDoubleExtra("alan", 0);
        String urun = intent.getStringExtra("urun");
        String ekimTarihi = intent.getStringExtra("ekimTarihi");
        String hasatTarihi = intent.getStringExtra("hasatTarihi");
        double miktar = intent.getDoubleExtra("miktar", 0);

        // Alanlara verileri yaz
        edtIsim.setText(isim);
        edtAlan.setText(String.valueOf(alan));
        edtEkimTarihi.setText(ekimTarihi);
        edtHasatTarihi.setText(hasatTarihi);
        edtMiktar.setText(String.valueOf(miktar));

        // Spinner seçim
        for (int i = 0; i < urunler.length; i++) {
            if (urunler[i].equalsIgnoreCase(urun)) {
                spinnerUrun.setSelection(i);
                break;
            }
        }

        btnGuncelle.setOnClickListener(v -> {
            String yeniIsim = edtIsim.getText().toString().trim();
            String alanStr = edtAlan.getText().toString().trim();
            String yeniUrun = spinnerUrun.getSelectedItem().toString();
            String yeniEkim = edtEkimTarihi.getText().toString().trim();
            String yeniHasat = edtHasatTarihi.getText().toString().trim();
            String miktarStr = edtMiktar.getText().toString().trim();

            if (yeniIsim.isEmpty() || alanStr.isEmpty() || yeniUrun.isEmpty()
                    || yeniEkim.isEmpty() || yeniHasat.isEmpty() || miktarStr.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            double yeniAlan = Double.parseDouble(alanStr);
            double yeniMiktar = Double.parseDouble(miktarStr);

            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Veritabani.COL_TARLA_ISIM, yeniIsim);
            values.put(Veritabani.COL_TARLA_ALAN, yeniAlan);
            values.put(Veritabani.COL_TARLA_URUN, yeniUrun);
            values.put(Veritabani.COL_EKIM_TARIHI, yeniEkim);
            values.put(Veritabani.COL_HASAT_TARIHI, yeniHasat);
            values.put(Veritabani.COL_MIKTAR_TON, yeniMiktar);

            int guncel = db.update(Veritabani.TABLE_TARLA, values, "id=?", new String[]{String.valueOf(tarlaId)});
            if (guncel > 0) {
                Toast.makeText(this, "Tarla güncellendi", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Güncelleme başarısız", Toast.LENGTH_SHORT).show();
            }
        });

        btnGeriDon.setOnClickListener(v -> finish());
    }
}
