package com.example.ciftci_projesi_1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DepolamaActivity extends AppCompatActivity {

    private Veritabani veritabani;
    private Spinner urunSpinner;
    private TextView textSecilenUrun, textMevcutMiktar;
    private EditText editMiktar;
    private Button btnEkle, btnAzalt, btnGeriDon;

    private String seciliUrun = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depolama);

        veritabani = new Veritabani(this);

        // Bağlantılar
        urunSpinner = findViewById(R.id.urunSpinner);
        textSecilenUrun = findViewById(R.id.textSecilenUrun);
        textMevcutMiktar = findViewById(R.id.textMevcutMiktar);
        editMiktar = findViewById(R.id.editMiktar);
        btnEkle = findViewById(R.id.btnEkle);
        btnAzalt = findViewById(R.id.btnAzalt);
        btnGeriDon = findViewById(R.id.btnGeriDon);

        String[] urunler = {
                "Arpa", "Buğday", "Çavdar", "Yulaf", "Mısır", "Kanola",
                "Pancar", "Patates", "Pirinç", "Mazot", "İlaç", "Gübre"
        };

        // Spinner'a veri bağla
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, urunler);
        urunSpinner.setAdapter(adapter);

        urunSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                seciliUrun = urunler[position];
                guncelleMevcutMiktar();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // Ekle
        btnEkle.setOnClickListener(v -> {
            String girilen = editMiktar.getText().toString().trim();
            if (!girilen.isEmpty()) {
                try {
                    double eklenecekMiktar = Double.parseDouble(girilen); // Bu, kullanıcının girdiği eklenecek miktar
                    double mevcutMiktar = veritabani.getDepoMiktar(seciliUrun);
                    double yeniToplamMiktar = mevcutMiktar + eklenecekMiktar; // Yeni toplam miktar hesaplanıyor

                    veritabani.guncelleDepoMiktar(seciliUrun, yeniToplamMiktar); // Doğrudan yeni toplam miktarı gönderiyoruz
                    guncelleMevcutMiktar();
                    editMiktar.setText("");
                    Toast.makeText(this, seciliUrun + " miktarı başarıyla eklendi.", Toast.LENGTH_SHORT).show(); // Başarı mesajı
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Geçersiz miktar girdiniz. Lütfen sayı girin.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Lütfen eklenecek miktarı girin.", Toast.LENGTH_SHORT).show();
            }
        });

// Azalt
        btnAzalt.setOnClickListener(v -> {
            String girilen = editMiktar.getText().toString().trim();
            if (!girilen.isEmpty()) {
                try {
                    double cikarilacakMiktar = Double.parseDouble(girilen); // Bu, kullanıcının girdiği çıkarılacak miktar
                    if (cikarilacakMiktar <= 0) {
                        Toast.makeText(this, "Çıkarılacak miktar pozitif olmalıdır.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double mevcutMiktar = veritabani.getDepoMiktar(seciliUrun);
                    if (mevcutMiktar >= cikarilacakMiktar) {
                        double yeniToplamMiktar = mevcutMiktar - cikarilacakMiktar; // Yeni toplam miktar hesaplanıyor
                        veritabani.guncelleDepoMiktar(seciliUrun, yeniToplamMiktar); // Doğrudan yeni toplam miktarı gönderiyoruz
                        guncelleMevcutMiktar();
                        editMiktar.setText("");
                        Toast.makeText(this, seciliUrun + " miktarı başarıyla azaltıldı.", Toast.LENGTH_SHORT).show(); // Başarı mesajı
                    } else {
                        Toast.makeText(this, "Yetersiz stok! Mevcut miktar: " + mevcutMiktar, Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Geçersiz sayı girdiniz. Lütfen sayı girin.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Lütfen çıkarılacak miktarı girin.", Toast.LENGTH_SHORT).show();
            }
        });


        btnGeriDon.setOnClickListener(v -> finish());
    }

    private void guncelleMevcutMiktar() {
        double miktar = veritabani.getDepoMiktar(seciliUrun);
        String birim = veritabani.getUrunBirimi(seciliUrun);
        textSecilenUrun.setText("Ürün: " + seciliUrun);
        textMevcutMiktar.setText("Mevcut Miktar: " + miktar + " " + birim);
    }
}
