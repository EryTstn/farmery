package com.example.ciftci_projesi_1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TarlaListeleActivity extends AppCompatActivity {

    ListView lvTarlalar;
    Button btnGeriDon;
    Veritabani veritabani;
    ArrayList<String> tarlaListesi;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarla_listele);

        lvTarlalar = findViewById(R.id.lvTarlalar);
        btnGeriDon = findViewById(R.id.btnGeriDon);

        veritabani = new Veritabani(this);
        tarlaListesi = new ArrayList<>();

        listeleTarlalar();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tarlaListesi);
        lvTarlalar.setAdapter(adapter);

        lvTarlalar.setOnItemClickListener((parent, view, position, id) -> {
            String secilenTarla = tarlaListesi.get(position);
            String[] satirlar = secilenTarla.split("\n");
            String isim = satirlar[0].replace("İsim: ", "").trim();

            AlertDialog.Builder builder = new AlertDialog.Builder(TarlaListeleActivity.this);
            builder.setTitle("Seçim Yap");
            builder.setItems(new CharSequence[]{"Düzenle", "Sil"}, (dialog, which) -> {
                if (which == 0) {
                    Intent intent = new Intent(TarlaListeleActivity.this, TarlaDuzenleActivity.class);
                    intent.putExtra("tarla_adi", isim);
                    startActivity(intent);
                } else if (which == 1) {
                    new AlertDialog.Builder(TarlaListeleActivity.this)
                            .setTitle("Tarla Sil")
                            .setMessage("Bu tarlayı silmek istediğinize emin misiniz?")
                            .setPositiveButton("Evet", (dialog1, which1) -> {
                                SQLiteDatabase db = veritabani.getWritableDatabase();
                                db.delete(Veritabani.TABLE_TARLA, Veritabani.COL_TARLA_ISIM + " = ?", new String[]{isim});
                                tarlaListesi.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(this, "Tarla silindi", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Hayır", null)
                            .show();
                }
            });
            builder.show();
        });

        btnGeriDon.setOnClickListener(v -> {
            Intent intent = new Intent(TarlaListeleActivity.this, TarlalarActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void listeleTarlalar() {
        SQLiteDatabase db = veritabani.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Veritabani.TABLE_TARLA, null);
        tarlaListesi.clear();

        if (cursor.moveToFirst()) {
            do {
                String isim = cursor.getString(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_ISIM));
                double alan = cursor.getDouble(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_ALAN));
                String urun = cursor.getString(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_URUN));
                String ekimTarihi = cursor.getString(cursor.getColumnIndexOrThrow(Veritabani.COL_EKIM_TARIHI));
                String hasatTarihi = cursor.getString(cursor.getColumnIndexOrThrow(Veritabani.COL_HASAT_TARIHI));
                double miktarTon = cursor.getDouble(cursor.getColumnIndexOrThrow(Veritabani.COL_MIKTAR_TON));

                String satir = "İsim: " + isim +
                        "\nAlan: " + alan + " m²" +
                        "\nÜrün: " + urun +
                        "\nEkim Tarihi: " + ekimTarihi +
                        "\nHasat Tarihi: " + hasatTarihi +
                        "\nÜrün Miktarı: " + miktarTon + " ton";

                tarlaListesi.add(satir);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}
