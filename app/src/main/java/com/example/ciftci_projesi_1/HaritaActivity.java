package com.example.ciftci_projesi_1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HaritaActivity extends AppCompatActivity {

    private static final String TAG = "HaritaActivity";
    private MapView mapView;
    private Veritabani veritabani;
    private List<Polygon> tarlaPolygonlari = new ArrayList<>();
    private Button btnGeriDon;
    private TKGMAPIService tkgmAPIService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            // OSMDroid yapılandırması - ÖNCE yapılmalı
            Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));
            Configuration.getInstance().setUserAgentValue("Farmery/1.0");
            
            // Cache dizini ayarla
            File cacheDir = new File(getCacheDir(), "osmdroid");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            Configuration.getInstance().setOsmdroidBasePath(cacheDir);
            Configuration.getInstance().setOsmdroidTileCache(cacheDir);
            
            setContentView(R.layout.activity_harita);

            veritabani = new Veritabani(this);
            tkgmAPIService = new TKGMAPIService();
            mainHandler = new Handler(Looper.getMainLooper());

            btnGeriDon = findViewById(R.id.btnGeriDon);
            btnGeriDon.setOnClickListener(v -> finish());

            // Harita görünümünü al
            mapView = findViewById(R.id.map);
            
            if (mapView == null) {
                Toast.makeText(this, "Harita görünümü bulunamadı!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "MapView null!");
                return;
            }
            
            // OSMDroid yapılandırması
            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setMultiTouchControls(true);
            mapView.setBuiltInZoomControls(true);
            mapView.setClickable(true);
            
            // Zoom seviyesi ayarla
            mapView.getController().setZoom(14.0);

            // Kayseri Pınarbaşı Mamalık köyü merkez koordinatı
            GeoPoint mamalikKoyu = new GeoPoint(38.7123, 36.4123);
            mapView.getController().setCenter(mamalikKoyu);
            
            // Tarlaları haritaya ekle
            tarlalariHaritayaEkle();
            
            // TKGM API'den parsel verilerini yükle (isteğe bağlı)
            // loadTKGMData();
            
            // Haritayı yenile
            mapView.invalidate();
            
            Log.d(TAG, "Harita başarıyla yüklendi");
            
        } catch (Exception e) {
            Log.e(TAG, "Harita yüklenirken hata: " + e.getMessage(), e);
            Toast.makeText(this, "Harita yüklenirken hata: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void tarlalariHaritayaEkle() {
        try {
            SQLiteDatabase db = veritabani.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + Veritabani.TABLE_TARLA + 
                    " WHERE " + Veritabani.COL_TARLA_ENLEM + " IS NOT NULL AND " + 
                    Veritabani.COL_TARLA_BOYLAM + " IS NOT NULL", null);

            if (cursor.moveToFirst()) {
                int tarlaSayisi = 0;
                do {
                    try {
                        String isim = cursor.getString(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_ISIM));
                        double enlem = cursor.getDouble(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_ENLEM));
                        double boylam = cursor.getDouble(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_BOYLAM));
                        String urun = cursor.getString(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_URUN));
                        double alan = cursor.getDouble(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_ALAN));
                        String polygonStr = cursor.getString(cursor.getColumnIndexOrThrow(Veritabani.COL_TARLA_POLYGON));

                        GeoPoint tarlaMerkez = new GeoPoint(enlem, boylam);

                        // Marker ekle
                        Marker marker = new Marker(mapView);
                        marker.setPosition(tarlaMerkez);
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        marker.setTitle(isim);
                        marker.setSnippet("Ürün: " + urun + " | Alan: " + alan + " dönüm");
                        marker.setOnMarkerClickListener((marker1, mapView) -> {
                            Toast.makeText(this, marker1.getTitle() + "\n" + marker1.getSnippet(), 
                                    Toast.LENGTH_LONG).show();
                            return true;
                        });
                        mapView.getOverlays().add(marker);

                        // Polygon çiz (eğer varsa)
                        List<GeoPoint> polygonKoordinatlari;
                        if (polygonStr != null && !polygonStr.isEmpty()) {
                            polygonKoordinatlari = parsePolygonString(polygonStr);
                        } else {
                            // Polygon yoksa, merkez nokta etrafında küçük bir kare çiz
                            double offset = 0.001; // Yaklaşık 100 metre
                            polygonKoordinatlari = new ArrayList<>();
                            polygonKoordinatlari.add(new GeoPoint(enlem - offset, boylam - offset));
                            polygonKoordinatlari.add(new GeoPoint(enlem + offset, boylam - offset));
                            polygonKoordinatlari.add(new GeoPoint(enlem + offset, boylam + offset));
                            polygonKoordinatlari.add(new GeoPoint(enlem - offset, boylam + offset));
                        }

                        if (!polygonKoordinatlari.isEmpty()) {
                            Polygon polygon = new Polygon();
                            polygon.setPoints(polygonKoordinatlari);
                            polygon.setFillColor(Color.argb(80, 76, 175, 80));
                            polygon.setStrokeColor(Color.argb(200, 76, 175, 80));
                            polygon.setStrokeWidth(3.0f);
                            polygon.setOnClickListener((polygon1, mapView, eventPos) -> {
                                Toast.makeText(this, "Tarla: " + isim, Toast.LENGTH_SHORT).show();
                                return true;
                            });
                            mapView.getOverlays().add(polygon);
                            tarlaPolygonlari.add(polygon);
                        }
                        
                        tarlaSayisi++;
                    } catch (Exception e) {
                        Log.e(TAG, "Tarla eklenirken hata: " + e.getMessage(), e);
                    }

                } while (cursor.moveToNext());
                
                Log.d(TAG, tarlaSayisi + " tarla haritaya eklendi");
                Toast.makeText(this, tarlaSayisi + " tarla gösteriliyor", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Haritada gösterilecek tarla bulunamadı");
                Toast.makeText(this, "Henüz tarla eklenmemiş", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
            
        } catch (Exception e) {
            Log.e(TAG, "Tarlalar yüklenirken hata: " + e.getMessage(), e);
            Toast.makeText(this, "Tarlalar yüklenirken hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    private List<GeoPoint> parsePolygonString(String polygonStr) {
        List<GeoPoint> koordinatlar = new ArrayList<>();
        try {
            String[] noktalar = polygonStr.split("\\|");
            for (String nokta : noktalar) {
                String[] latLng = nokta.split(",");
                if (latLng.length == 2) {
                    double lat = Double.parseDouble(latLng[0].trim());
                    double lng = Double.parseDouble(latLng[1].trim());
                    koordinatlar.add(new GeoPoint(lat, lng));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Polygon parse hatası: " + e.getMessage(), e);
        }
        return koordinatlar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    /**
     * TKGM API'den GeoJSON verilerini yükler ve haritada gösterir
     */
    private void loadTKGMData() {
        new Thread(() -> {
            try {
                // İl listesini al
                List<TKGMAPIService.Il> iller = tkgmAPIService.getIlListesi();
                Log.d(TAG, "İl sayısı: " + iller.size());
                
                // Örnek: Kayseri için parsel bilgisi al (il kodu 38)
                // String geoJson = tkgmAPIService.getParselBilgisi("38", "ilceKodu", "parselNo");
                // if (geoJson != null) {
                //     List<List<GeoPoint>> polygons = tkgmAPIService.parseGeoJSONPolygons(geoJson);
                //     mainHandler.post(() -> showTKGM polygons(polygons));
                // }
                
            } catch (Exception e) {
                Log.e(TAG, "TKGM verisi yüklenirken hata: " + e.getMessage(), e);
            }
        }).start();
    }
    
    /**
     * TKGM'den gelen polygon'ları haritada gösterir
     */
    private void showTKGMpolygons(List<List<GeoPoint>> polygons) {
        for (List<GeoPoint> polygonPoints : polygons) {
            if (!polygonPoints.isEmpty()) {
                Polygon polygon = new Polygon();
                polygon.setPoints(polygonPoints);
                polygon.setFillColor(Color.argb(60, 33, 150, 243)); // Mavi, yarı saydam
                polygon.setStrokeColor(Color.argb(200, 33, 150, 243)); // Mavi kenar
                polygon.setStrokeWidth(2.0f);
                polygon.setOnClickListener((polygon1, mapView, eventPos) -> {
                    Toast.makeText(this, "TKGM Parsel", Toast.LENGTH_SHORT).show();
                    return true;
                });
                mapView.getOverlays().add(polygon);
                tarlaPolygonlari.add(polygon);
            }
        }
        mapView.invalidate();
        Toast.makeText(this, polygons.size() + " parsel gösteriliyor", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (veritabani != null) {
            veritabani.close();
        }
    }
}
