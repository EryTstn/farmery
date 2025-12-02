package com.example.ciftci_projesi_1;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.osmdroid.util.GeoPoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TKGMAPIService {
    
    private static final String TAG = "TKGMAPIService";
    private static final String BASE_URL = "https://cbsapi.tkgm.gov.tr/megsiswebapi.v3.1/api";
    private OkHttpClient client;
    private Gson gson;
    
    public TKGMAPIService() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
    }
    
    /**
     * İl listesini getirir
     */
    public List<Il> getIlListesi() {
        try {
            String date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                    .format(new Date());
            String url = BASE_URL + "/maksIdariYapi/illiste?date=" + date + "&x-aspnet-version=tkgm";
            
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", "Farmery/1.0")
                    .build();
            
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return parseIlListesi(jsonResponse);
            } else {
                Log.e(TAG, "API hatası: " + response.code());
            }
        } catch (IOException e) {
            Log.e(TAG, "API isteği hatası: " + e.getMessage(), e);
        }
        return new ArrayList<>();
    }
    
    /**
     * GeoJSON formatındaki veriyi parse eder ve polygon koordinatlarını döndürür
     */
    public List<List<GeoPoint>> parseGeoJSONPolygons(String geoJsonString) {
        List<List<GeoPoint>> polygons = new ArrayList<>();
        
        try {
            JsonElement jsonElement = gson.fromJson(geoJsonString, JsonElement.class);
            
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                
                // Feature veya FeatureCollection olabilir
                if (jsonObject.has("type")) {
                    String type = jsonObject.get("type").getAsString();
                    
                    if ("Feature".equals(type)) {
                        // Tek bir feature
                        List<GeoPoint> polygon = parseFeature(jsonObject);
                        if (!polygon.isEmpty()) {
                            polygons.add(polygon);
                        }
                    } else if ("FeatureCollection".equals(type)) {
                        // Birden fazla feature
                        JsonArray features = jsonObject.getAsJsonArray("features");
                        if (features != null) {
                            for (JsonElement feature : features) {
                                List<GeoPoint> polygon = parseFeature(feature.getAsJsonObject());
                                if (!polygon.isEmpty()) {
                                    polygons.add(polygon);
                                }
                            }
                        }
                    }
                }
            } else if (jsonElement.isJsonArray()) {
                // Direkt array olabilir
                JsonArray features = jsonElement.getAsJsonArray();
                for (JsonElement feature : features) {
                    if (feature.isJsonObject()) {
                        List<GeoPoint> polygon = parseFeature(feature.getAsJsonObject());
                        if (!polygon.isEmpty()) {
                            polygons.add(polygon);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "GeoJSON parse hatası: " + e.getMessage(), e);
        }
        
        return polygons;
    }
    
    private List<GeoPoint> parseFeature(JsonObject feature) {
        List<GeoPoint> points = new ArrayList<>();
        
        try {
            if (feature.has("geometry")) {
                JsonObject geometry = feature.getAsJsonObject("geometry");
                String geometryType = geometry.get("type").getAsString();
                
                if ("Polygon".equals(geometryType) && geometry.has("coordinates")) {
                    JsonArray coordinates = geometry.getAsJsonArray("coordinates");
                    
                    // Polygon'un ilk ring'ini al (dış sınır)
                    if (coordinates.size() > 0) {
                        JsonArray ring = coordinates.get(0).getAsJsonArray();
                        
                        for (JsonElement coord : ring) {
                            JsonArray coordArray = coord.getAsJsonArray();
                            if (coordArray.size() >= 2) {
                                double lon = coordArray.get(0).getAsDouble();
                                double lat = coordArray.get(1).getAsDouble();
                                points.add(new GeoPoint(lat, lon));
                            }
                        }
                    }
                } else if ("MultiPolygon".equals(geometryType) && geometry.has("coordinates")) {
                    // MultiPolygon için ilk polygon'u al
                    JsonArray polygons = geometry.getAsJsonArray("coordinates");
                    if (polygons.size() > 0) {
                        JsonArray firstPolygon = polygons.get(0).getAsJsonArray();
                        if (firstPolygon.size() > 0) {
                            JsonArray ring = firstPolygon.get(0).getAsJsonArray();
                            
                            for (JsonElement coord : ring) {
                                JsonArray coordArray = coord.getAsJsonArray();
                                if (coordArray.size() >= 2) {
                                    double lon = coordArray.get(0).getAsDouble();
                                    double lat = coordArray.get(1).getAsDouble();
                                    points.add(new GeoPoint(lat, lon));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Feature parse hatası: " + e.getMessage(), e);
        }
        
        return points;
    }
    
    private List<Il> parseIlListesi(String jsonResponse) {
        List<Il> iller = new ArrayList<>();
        try {
            JsonElement jsonElement = gson.fromJson(jsonResponse, JsonElement.class);
            if (jsonElement.isJsonArray()) {
                JsonArray array = jsonElement.getAsJsonArray();
                for (JsonElement element : array) {
                    if (element.isJsonObject()) {
                        JsonObject obj = element.getAsJsonObject();
                        Il il = new Il();
                        if (obj.has("adi")) il.setAdi(obj.get("adi").getAsString());
                        if (obj.has("kodu")) il.setKodu(obj.get("kodu").getAsString());
                        iller.add(il);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "İl listesi parse hatası: " + e.getMessage(), e);
        }
        return iller;
    }
    
    /**
     * İl kodu ile parsel bilgilerini getirir (örnek endpoint)
     */
    public String getParselBilgisi(String ilKodu, String ilceKodu, String parselNo) {
        try {
            // Bu endpoint örnek - gerçek endpoint'i TKGM dokümantasyonundan alınmalı
            String url = BASE_URL + "/parsel/" + ilKodu + "/" + ilceKodu + "/" + parselNo;
            
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", "Farmery/1.0")
                    .build();
            
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e(TAG, "Parsel bilgisi hatası: " + e.getMessage(), e);
        }
        return null;
    }
    
    // İl model sınıfı
    public static class Il {
        private String adi;
        private String kodu;
        
        public String getAdi() { return adi; }
        public void setAdi(String adi) { this.adi = adi; }
        public String getKodu() { return kodu; }
        public void setKodu(String kodu) { this.kodu = kodu; }
    }
}



