package com.example.mausam.eis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Forum extends AppCompatActivity {

    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        webview= (WebView)findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        //webview.loadUrl("http://" + new MainActivity().ip + ":8080/home");

        webview.loadUrl("http://" + MainActivity.ip + ":8080/exam-project-8/");

        WebSettings webSettings= webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()){
            webview.goBack();
        }else{
            super.onBackPressed();
        }

    }
}
