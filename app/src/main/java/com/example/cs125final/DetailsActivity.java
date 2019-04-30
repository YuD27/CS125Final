package com.example.cs125final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class DetailsActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar load;
    String url = "";
    //Set up WebView and loader. Open web w/ Chrome.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Create Intent
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        load = (ProgressBar) findViewById(R.id.loader);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(url);
        // Call Chrome
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Finish loading -> Hide loader
                // Else -> Show loader
                if (progress == 100) {
                    load.setVisibility(View.GONE);
                } else {
                    load.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}