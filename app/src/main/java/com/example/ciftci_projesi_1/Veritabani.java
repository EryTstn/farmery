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
    public static final int DATABASE_VERSION = 5; // <-- Versiyon artırıldı

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
                COL_MIKTAR_TON + " REAL)";
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KULLANICI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARLA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GELIRGIDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HAYVAN); // 👈 Bu satır EKLENDİ!
        onCreate(db);
    }
}
