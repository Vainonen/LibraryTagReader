package com.kirja.xxx.reader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

// Loads library web page using isbn
public class HTMLActivity extends Activity {

    WebView webview;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String author = getIntent().getStringExtra("Author");
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("http://haku.helmet.fi/iii/mobile/search/C__S"+author);
    }
}