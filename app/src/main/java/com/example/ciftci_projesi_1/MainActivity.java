package com.example.ciftci_projesi_1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    CardView cardTarlalar, cardDepolama, cardEkonomi, cardHayvanlar, cardAraclar, cardBilgilerim;
    TextView txtTarlaSayisi, txtHayvanSayisi, txtWelcome;
    Veritabani veritabani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        veritabani = new Veritabani(this);

        // Kartları tanımla
        cardTarlalar = findViewById(R.id.cardTarlalar);
        cardDepolama = findViewById(R.id.cardDepolama);
        cardEkonomi = findViewById(R.id.cardEkonomi);
        cardHayvanlar = findViewById(R.id.cardHayvanlar);
        cardAraclar = findViewById(R.id.cardAraclar);
        cardBilgilerim = findViewById(R.id.cardBilgilerim);

        // İstatistik textview'ları
        txtTarlaSayisi = findViewById(R.id.txtTarlaSayisi);
        txtHayvanSayisi = findViewById(R.id.txtHayvanSayisi);
        txtWelcome = findViewById(R.id.txtWelcome);

        // İstatistikleri güncelle
        guncelleIstatistikler();

        // Tıklama olayları
        cardTarlalar.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, TarlalarActivity.class);
            startActivity(i);
        });

        cardDepolama.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, DepolamaActivity.class);
            startActivity(i);
        });

        cardEkonomi.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, EkonomiActivity.class);
            startActivity(i);
        });

        cardHayvanlar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HayvanlarActivity.class);
            startActivity(intent);
        });

        cardAraclar.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AraclarActivity.class);
            startActivity(i);
        });

        cardBilgilerim.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, BilgilerimActivity.class);
            startActivity(i);
        });
    }

    private void guncelleIstatistikler() {
        // Tarla sayısını al
        SQLiteDatabase db = veritabani.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + Veritabani.TABLE_TARLA, null);
        if (cursor.moveToFirst()) {
            int tarlaSayisi = cursor.getInt(0);
            txtTarlaSayisi.setText(String.valueOf(tarlaSayisi));
        }
        cursor.close();

        // Toplam hayvan sayısını al
        int toplamHayvan = veritabani.getHayvanAdet("İnek") + 
                          veritabani.getHayvanAdet("Koyun") + 
                          veritabani.getHayvanAdet("Tavuk");
        txtHayvanSayisi.setText(String.valueOf(toplamHayvan));

        // Kullanıcı adını al
        Cursor kullaniciCursor = db.rawQuery("SELECT " + Veritabani.COL_ADSOYAD + 
                " FROM " + Veritabani.TABLE_KULLANICI + " LIMIT 1", null);
        if (kullaniciCursor.moveToFirst()) {
            String adSoyad = kullaniciCursor.getString(0);
            txtWelcome.setText("Hoş Geldiniz!");
        }
        kullaniciCursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Sayfa her açıldığında istatistikleri güncelle
        guncelleIstatistikler();
    }
}
