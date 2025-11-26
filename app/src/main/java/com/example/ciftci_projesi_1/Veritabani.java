package com.example.ciftci_projesi_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.HashMap;
import java.util.ArrayList;

public class Veritabani extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CiftciDB.db";
    public static final int DATABASE_VERSION = 6; // <-- Harita özelliği için versiyon artırıldı

    // Kullanici tablosu
    public static final String TABLE_KULLANICI = "Kullanici";
    public static final String COL_ID = "id";
    public static final String COL_ADSOYAD = "ad_soyad";
    public static final String COL_TELEFON = "telefon";
    public static final String COL_ADRES = "adres";

    // Tarla tablosu
    public static final String TABLE_TARLA = "Tarla";
    public static final String COL_TARLA_ID = "id";
    public static final String COL_TARLA_ISIM = "isim";
    public static final String COL_TARLA_ALAN = "alan";
    public static final String COL_TARLA_URUN = "urun";
    public static final String COL_EKIM_TARIHI = "ekim_tarihi";
    public static final String COL_HASAT_TARIHI = "hasat_tarihi";
    public static final String COL_MIKTAR_TON = "miktar_ton";
    public static final String COL_TARLA_ENLEM = "enlem";
    public static final String COL_TARLA_BOYLAM = "boylam";
    public static final String COL_TARLA_POLYGON = "polygon"; // JSON formatında koordinatlar

    // Depo tablosu
    public static final String TABLE_DEPO = "Depo";
    public static final String COL_DEPO_ID = "id";
    public static final String COL_URUN_ADI = "urun_adi";
    public static final String COL_MIKTAR = "miktar";
    public static final String COL_BIRIM = "birim";

    // GelirGider tablosu
    public static final String TABLE_GELIRGIDER = "GelirGider";
    public static final String COL_GG_ID = "id";
    public static final String COL_GG_TUR = "tur";
    public static final String COL_GG_KATEGORI = "kategori";
    public static final String COL_GG_MIKTAR = "miktar";

    // Hayvan tablosu
    public static final String TABLE_HAYVAN = "Hayvan";
    public static final String COL_HAYVAN_ID = "id";
    public static final String COL_HAYVAN_TUR = "tur";
    public static final String COL_HAYVAN_ADET = "adet";

    public Veritabani(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createKullanici = "CREATE TABLE " + TABLE_KULLANICI + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ADSOYAD + " TEXT, " +
                COL_TELEFON + " TEXT, " +
                COL_ADRES + " TEXT)";
        db.execSQL(createKullanici);

        String createTarla = "CREATE TABLE " + TABLE_TARLA + " (" +
                COL_TARLA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TARLA_ISIM + " TEXT, " +
                COL_TARLA_ALAN + " REAL, " +
                COL_TARLA_URUN + " TEXT, " +
                COL_EKIM_TARIHI + " TEXT, " +
                COL_HASAT_TARIHI + " TEXT, " +
                COL_MIKTAR_TON + " REAL, " +
                COL_TARLA_ENLEM + " REAL, " +
                COL_TARLA_BOYLAM + " REAL, " +
                COL_TARLA_POLYGON + " TEXT)";
        db.execSQL(createTarla);

        String createDepo = "CREATE TABLE " + TABLE_DEPO + " (" +
                COL_DEPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_URUN_ADI + " TEXT, " +
                COL_MIKTAR + " REAL, " +
                COL_BIRIM + " TEXT)";
        db.execSQL(createDepo);

        String createGelirGider = "CREATE TABLE " + TABLE_GELIRGIDER + " (" +
                COL_GG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_GG_TUR + " TEXT, " +
                COL_GG_KATEGORI + " TEXT, " +
                COL_GG_MIKTAR + " REAL)";
        db.execSQL(createGelirGider);

        String createHayvan = "CREATE TABLE " + TABLE_HAYVAN + " (" +
                COL_HAYVAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_HAYVAN_TUR + " TEXT UNIQUE, " +
                COL_HAYVAN_ADET + " INTEGER DEFAULT 0)";
        db.execSQL(createHayvan);

        // Örnek kullanıcı verisi
        db.execSQL("INSERT INTO " + TABLE_KULLANICI +
                " (" + COL_ADSOYAD + ", " + COL_TELEFON + ", " + COL_ADRES + ") VALUES " +
                "('Ali Yılmaz', '05551234567', 'Ankara / Türkiye')");

        kontrolEtVeGerekirseUrunEkle(db);
        kontrolEtVeGerekirseHayvanEkle(db);
        ornekTarlaVerileriEkle(db);
    }

    private void ornekTarlaVerileriEkle(SQLiteDatabase db) {
        // Kayseri Pınarbaşı Mamalık köyü koordinatları: ~38.7°N, 36.4°E
        // Örnek tarla koordinatları (Mamalık köyü çevresinde)
        
        // Tarla 1: Merkez tarla
        ContentValues tarla1 = new ContentValues();
        tarla1.put(COL_TARLA_ISIM, "Merkez Tarla");
        tarla1.put(COL_TARLA_ALAN, 15.5);
        tarla1.put(COL_TARLA_URUN, "Buğday");
        tarla1.put(COL_EKIM_TARIHI, "01/10/2024");
        tarla1.put(COL_HASAT_TARIHI, "15/07/2025");
        tarla1.put(COL_MIKTAR_TON, 12.5);
        tarla1.put(COL_TARLA_ENLEM, 38.7123);
        tarla1.put(COL_TARLA_BOYLAM, 36.4123);
        // Polygon: Dikdörtgen alan (4 köşe)
        tarla1.put(COL_TARLA_POLYGON, "38.7123,36.4123|38.7130,36.4123|38.7130,36.4130|38.7123,36.4130");
        db.insert(TABLE_TARLA, null, tarla1);

        // Tarla 2: Kuzey tarla
        ContentValues tarla2 = new ContentValues();
        tarla2.put(COL_TARLA_ISIM, "Kuzey Tarla");
        tarla2.put(COL_TARLA_ALAN, 22.0);
        tarla2.put(COL_TARLA_URUN, "Arpa");
        tarla2.put(COL_EKIM_TARIHI, "15/09/2024");
        tarla2.put(COL_HASAT_TARIHI, "20/06/2025");
        tarla2.put(COL_MIKTAR_TON, 18.0);
        tarla2.put(COL_TARLA_ENLEM, 38.7150);
        tarla2.put(COL_TARLA_BOYLAM, 36.4100);
        tarla2.put(COL_TARLA_POLYGON, "38.7150,36.4100|38.7160,36.4100|38.7160,36.4110|38.7150,36.4110");
        db.insert(TABLE_TARLA, null, tarla2);

        // Tarla 3: Güney tarla
        ContentValues tarla3 = new ContentValues();
        tarla3.put(COL_TARLA_ISIM, "Güney Tarla");
        tarla3.put(COL_TARLA_ALAN, 18.5);
        tarla3.put(COL_TARLA_URUN, "Mısır");
        tarla3.put(COL_EKIM_TARIHI, "01/05/2024");
        tarla3.put(COL_HASAT_TARIHI, "15/09/2024");
        tarla3.put(COL_MIKTAR_TON, 25.0);
        tarla3.put(COL_TARLA_ENLEM, 38.7100);
        tarla3.put(COL_TARLA_BOYLAM, 36.4140);
        tarla3.put(COL_TARLA_POLYGON, "38.7100,36.4140|38.7110,36.4140|38.7110,36.4150|38.7100,36.4150");
        db.insert(TABLE_TARLA, null, tarla3);

        // Tarla 4: Doğu tarla
        ContentValues tarla4 = new ContentValues();
        tarla4.put(COL_TARLA_ISIM, "Doğu Tarla");
        tarla4.put(COL_TARLA_ALAN, 12.0);
        tarla4.put(COL_TARLA_URUN, "Pancar");
        tarla4.put(COL_EKIM_TARIHI, "10/04/2024");
        tarla4.put(COL_HASAT_TARIHI, "30/10/2024");
        tarla4.put(COL_MIKTAR_TON, 30.0);
        tarla4.put(COL_TARLA_ENLEM, 38.7135);
        tarla4.put(COL_TARLA_BOYLAM, 36.4150);
        tarla4.put(COL_TARLA_POLYGON, "38.7135,36.4150|38.7140,36.4150|38.7140,36.4155|38.7135,36.4155");
        db.insert(TABLE_TARLA, null, tarla4);
    }

    public void kontrolEtVeGerekirseUrunEkle(SQLiteDatabase db) {
        String[][] urunler = {
                {"Arpa", "0", "ton"}, {"Buğday", "0", "ton"}, {"Çavdar", "0", "ton"},
                {"Yulaf", "0", "ton"}, {"Mısır", "0", "ton"}, {"Kanola", "0", "ton"},
                {"Pancar", "0", "ton"}, {"Patates", "0", "ton"}, {"Pirinç", "0", "ton"},
                {"Mazot", "0", "litre"}, {"İlaç", "0", "litre"}, {"Gübre", "0", "ton"}
        };

        for (String[] urun : urunler) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DEPO + " WHERE " + COL_URUN_ADI + " = ?", new String[]{urun[0]});
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(COL_URUN_ADI, urun[0]);
                values.put(COL_MIKTAR, Double.parseDouble(urun[1]));
                values.put(COL_BIRIM, urun[2]);
                db.insert(TABLE_DEPO, null, values);
            }
            cursor.close();
        }
    }

    public void kontrolEtVeGerekirseHayvanEkle(SQLiteDatabase db) {
        String[] hayvanlar = {"İnek", "Koyun", "Tavuk"};
        for (String tur : hayvanlar) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HAYVAN + " WHERE " + COL_HAYVAN_TUR + " = ?", new String[]{tur});
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(COL_HAYVAN_TUR, tur);
                values.put(COL_HAYVAN_ADET, 0);
                db.insert(TABLE_HAYVAN, null, values);
            }
            cursor.close();
        }
    }

    public int getHayvanAdet(String tur) {
        int adet = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_HAYVAN_ADET + " FROM " + TABLE_HAYVAN + " WHERE " + COL_HAYVAN_TUR + " = ?", new String[]{tur});
        if (cursor.moveToFirst()) {
            adet = cursor.getInt(0);
        }
        cursor.close();
        return adet;
    }

    public void guncelleHayvanAdet(String tur, int yeniAdet) {
        if (yeniAdet < 0) yeniAdet = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HAYVAN_ADET, yeniAdet);
        db.update(TABLE_HAYVAN, values, COL_HAYVAN_TUR + " = ?", new String[]{tur});
        db.close();
    }

    public double getDepoMiktar(String urunAdi) {
        double miktar = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_MIKTAR + " FROM " + TABLE_DEPO + " WHERE " + COL_URUN_ADI + " = ?", new String[]{urunAdi});
        if (cursor.moveToFirst()) {
            miktar = cursor.getDouble(0);
        }
        cursor.close();
        return miktar;
    }

    public void guncelleDepoMiktar(String urunAdi, double yeniMiktar) {
        if (yeniMiktar < 0) yeniMiktar = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MIKTAR, yeniMiktar);
        db.update(TABLE_DEPO, values, COL_URUN_ADI + " = ?", new String[]{urunAdi});
        db.close();
    }

    public String getUrunBirimi(String urunAdi) {
        String birim = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_BIRIM + " FROM " + TABLE_DEPO + " WHERE " + COL_URUN_ADI + " = ?", new String[]{urunAdi});
        if (cursor.moveToFirst()) {
            birim = cursor.getString(0);
        }
        cursor.close();
        return birim;
    }

    public ArrayList<String> getUrunAdlari() {
        ArrayList<String> urunler = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_URUN_ADI + ", " + COL_MIKTAR + ", " + COL_BIRIM + " FROM " + TABLE_DEPO, null);
        if (cursor.moveToFirst()) {
            do {
                String ad = cursor.getString(0);
                double miktar = cursor.getDouble(1);
                String birim = cursor.getString(2);
                urunler.add(ad + " (" + miktar + " " + birim + ")");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return urunler;
    }

    public void gelirGiderEkle(String tur, String kategori, double miktar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_GG_TUR, tur);
        values.put(COL_GG_KATEGORI, kategori);
        values.put(COL_GG_MIKTAR, miktar);
        db.insert(TABLE_GELIRGIDER, null, values);
        db.close();
    }

    public double getToplam(String tur) {
        double toplam = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_GG_MIKTAR + ") FROM " + TABLE_GELIRGIDER + " WHERE " + COL_GG_TUR + " = ?", new String[]{tur});
        if (cursor.moveToFirst()) {
            toplam = cursor.getDouble(0);
        }
        cursor.close();
        return toplam;
    }

    public HashMap<String, Double> getUrunOzet() {
        HashMap<String, Double> urunToplamlari = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_TARLA_URUN + ", SUM(" + COL_MIKTAR_TON + ") as toplam FROM " + TABLE_TARLA + " GROUP BY " + COL_TARLA_URUN, null);

        if (cursor.moveToFirst()) {
            do {
                String urun = cursor.getString(cursor.getColumnIndexOrThrow(COL_TARLA_URUN));
                double toplam = cursor.getDouble(cursor.getColumnIndexOrThrow("toplam"));
                urunToplamlari.put(urun, toplam);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return urunToplamlari;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            // Harita özelliği için koordinat kolonlarını ekle
            try {
                db.execSQL("ALTER TABLE " + TABLE_TARLA + " ADD COLUMN " + COL_TARLA_ENLEM + " REAL");
                db.execSQL("ALTER TABLE " + TABLE_TARLA + " ADD COLUMN " + COL_TARLA_BOYLAM + " REAL");
                db.execSQL("ALTER TABLE " + TABLE_TARLA + " ADD COLUMN " + COL_TARLA_POLYGON + " TEXT");
                // Örnek tarla verilerini ekle
                ornekTarlaVerileriEkle(db);
            } catch (Exception e) {
                // Kolonlar zaten varsa hata verme
            }
        }
        if (oldVersion < 5) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KULLANICI);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARLA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GELIRGIDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HAYVAN);
            onCreate(db);
        }
    }
}
