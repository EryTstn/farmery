package com.example.ciftci_projesi_1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TarlaEkleActivity extends AppCompatActivity {

    EditText edtIsim, edtAlan, edtEkimTarihi, edtHasatTarihi, edtMiktarTon;
    Spinner spinnerUrun;
    Button btnTarlaKaydet, btnGeriDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarla_ekle);

        edtIsim = findViewById(R.id.edtIsim);
        edtAlan = findViewById(R.id.edtAlan);
        spinnerUrun = findViewById(R.id.spinnerUrun);
        edtEkimTarihi = findViewById(R.id.edtEkimTarihi);
        edtHasatTarihi = findViewById(R.id.edtHasatTarihi);
        edtMiktarTon = findViewById(R.id.edtMiktarTon);
        btnTarlaKaydet = findViewById(R.id.btnTarlaKaydet);
        btnGeriDon = findViewById(R.id.btnGeriDon);

        btnTarlaKaydet.setOnClickListener(v -> {
            String ad = edtIsim.getText().toString().trim();
            String alan = edtAlan.getText().toString().trim();
            String urun = spinnerUrun.getSelectedItem().toString();
            String ekim = edtEkimTarihi.getText().toString().trim();
            String hasat = edtHasatTarihi.getText().toString().trim();
            String miktar = edtMiktarTon.getText().toString().trim();

            if (ad.isEmpty() || alan.isEmpty() || urun.isEmpty() || ekim.isEmpty() || hasat.isEmpty() || miktar.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            Veritabani dbHelper = new Veritabani(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Veritabani.COL_TARLA_ISIM, ad);
            values.put(Veritabani.COL_TARLA_ALAN, alan);
            values.put(Veritabani.COL_TARLA_URUN, urun);
            values.put(Veritabani.COL_EKIM_TARIHI, ekim);
            values.put(Veritabani.COL_HASAT_TARIHI, hasat);
            values.put(Veritabani.COL_MIKTAR_TON, miktar);

            long result = db.insert(Veritabani.TABLE_TARLA, null, values);
            if (result != -1) {
                Toast.makeText(this, "Tarla eklendi", Toast.LENGTH_SHORT).show();
                edtIsim.setText("");
                edtAlan.setText("");
                edtEkimTarihi.setText("");
                edtHasatTarihi.setText("");
                edtMiktarTon.setText("");
                spinnerUrun.setSelection(0);
            } else {
                Toast.makeText(this, "Hata oluştu", Toast.LENGTH_SHORT).show();
            }

            db.close();
        });

        btnGeriDon.setOnClickListener(v -> {
            Intent intent = new Intent(TarlaEkleActivity.this, TarlalarActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
