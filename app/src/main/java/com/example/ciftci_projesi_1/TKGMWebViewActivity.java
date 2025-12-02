package com.example.ciftci_projesi_1;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TKGMWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private Button btnGeriDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tkgm_webview);

        webView = findViewById(R.id.webView);
        btnGeriDon = findViewById(R.id.btnGeriDon);

        // WebView ayarları
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // TKGM Parsel Sorgu sitesini yükle
        webView.loadUrl("https://parselsorgu.tkgm.gov.tr/");

        btnGeriDon.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}



