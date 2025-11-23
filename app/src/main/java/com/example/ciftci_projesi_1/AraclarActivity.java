package com.example.ciftci_projesi_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AraclarActivity extends AppCompatActivity {

    LinearLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_araclar);

        layoutRoot = findViewById(R.id.layoutRoot); // root LinearLayout

        // Fendt "Sat" butonu
        Button btnSat1 = findViewById(R.id.btnSat1);
        btnSat1.setOnClickListener(v -> {
            View arac1Layout = findViewById(R.id.arac1Layout);
            layoutRoot.removeView(arac1Layout);
        });

        // Massey "Sat" butonu
        Button btnSat2 = findViewById(R.id.btnSat2);
        btnSat2.setOnClickListener(v -> {
            View arac2Layout = findViewById(R.id.arac2Layout);
            layoutRoot.removeView(arac2Layout);
        });

        // Ekle butonu: Hilux ekle
        Button btnEkle = findViewById(R.id.btnAracEkle);
        btnEkle.setOnClickListener(v -> hiluxEkle());

        // Ana sayfa
        TextView btnAnaSayfa = findViewById(R.id.btnAnaSayfa);
        btnAnaSayfa.setOnClickListener(v -> finish());
    }

    void hiluxEkle() {
        LinearLayout yeniLayout = new LinearLayout(this);
        yeniLayout.setOrientation(LinearLayout.HORIZONTAL);
        yeniLayout.setPadding(10, 10, 10, 10);
        yeniLayout.setBackgroundColor(0xFFFFFFFF);
        yeniLayout.setElevation(2f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        yeniLayout.setLayoutParams(params);

        ImageView image = new ImageView(this);
        image.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
        image.setImageResource(R.drawable.hilux); // resmin adı "hilux.png" olacak
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(20, 0, 0, 0);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textLayout.setLayoutParams(textParams);

        TextView txt1 = new TextView(this);
        txt1.setText("Toyota Hilux");
        txt1.setTextSize(18f);
        txt1.setTextColor(0xFF000000);
        txt1.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView txt2 = new TextView(this);
        txt2.setText("Yıl: 2023");
        txt2.setTextColor(0xFF555555);
        txt2.setTextSize(16f);

        TextView txt3 = new TextView(this);
        txt3.setText("Fiyat: 1.700.000 TL");
        txt3.setTextColor(0xFF555555);
        txt3.setTextSize(16f);

        Button btnSat = new Button(this);
        btnSat.setText("Sat");
        btnSat.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_orange_dark));
        btnSat.setTextColor(0xFFFFFFFF);
        btnSat.setOnClickListener(v -> layoutRoot.removeView(yeniLayout));

        textLayout.addView(txt1);
        textLayout.addView(txt2);
        textLayout.addView(txt3);

        yeniLayout.addView(image);
        yeniLayout.addView(textLayout);
        yeniLayout.addView(btnSat);

        layoutRoot.addView(yeniLayout, layoutRoot.getChildCount() - 2); // Ekle butonundan önce ekle
    }
}
