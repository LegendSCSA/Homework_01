package com.shafie.homework

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        // Find WebView by ID
        val webView: WebView = findViewById(R.id.webview)

        // Enable JavaScript if needed (optional)
        webView.settings.javaScriptEnabled = true

        // Open links within the WebView, not in external browser
        webView.webViewClient = WebViewClient()

        // Load a URL (example: Google)
        webView.loadUrl("https://androidatc.com/")
    }

    // Override the back button behavior to allow navigating back within WebView history
    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.webview)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
