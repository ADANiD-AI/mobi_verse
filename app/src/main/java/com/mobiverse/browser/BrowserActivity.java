package com.mobiverse.browser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.mobiverse.MobiVerse.R;

public class BrowserActivity extends AppCompatActivity {

    private WebView webView;
    private EditText etUrl;
    private LinearProgressIndicator progressBar;
    private TextView tvTrackersBlocked;
    private int trackersBlocked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        initializeViews();
        setupWebView();
        setupListeners();
        
        // Load default page
        loadUrl("https://www.google.com");
    }

    private void initializeViews() {
        webView = findViewById(R.id.webview);
        etUrl = findViewById(R.id.et_url);
        progressBar = findViewById(R.id.progress_bar);
        tvTrackersBlocked = findViewById(R.id.tv_trackers_blocked);
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setSafeBrowsingEnabled(true);
        
        // Privacy settings
        settings.setBlockNetworkImage(false); // Set true for ultra privacy
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // No cache for incognito
        settings.setGeolocationEnabled(false);

        webView.setWebViewClient(new PrivacyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupListeners() {
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (webView.canGoBack()) webView.goBack();
        });

        findViewById(R.id.btn_forward).setOnClickListener(v -> {
            if (webView.canGoForward()) webView.goForward();
        });

        findViewById(R.id.btn_refresh).setOnClickListener(v -> webView.reload());

        etUrl.setOnEditorActionListener((v, actionId, event) -> {
            loadUrl(etUrl.getText().toString());
            return true;
        });

        findViewById(R.id.btn_ai_summarize).setOnClickListener(v -> summarizePage());
        findViewById(R.id.btn_ai_speak).setOnClickListener(v -> readPageAloud());
    }

    private void loadUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        webView.loadUrl(url);
        etUrl.setText(url);
    }

    private void summarizePage() {
        // TODO: Extract page text and send to AI for summary
        Toast.makeText(this, "AI Summarizing page...", Toast.LENGTH_SHORT).show();
    }

    private void readPageAloud() {
        // TODO: Extract page text and use TTS
        Toast.makeText(this, "Reading page aloud...", Toast.LENGTH_SHORT).show();
    }

    private class PrivacyWebViewClient extends WebViewClient {
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            
            // Block known tracker domains
            if (isTrackerDomain(url)) {
                trackersBlocked++;
                tvTrackersBlocked.setText(trackersBlocked + " trackers blocked");
                return true; // Block
            }
            
            etUrl.setText(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // Update security icon based on HTTPS
            updateSecurityIcon(url.startsWith("https://"));
        }

        private boolean isTrackerDomain(String url) {
            // Simple check - in production, use a comprehensive blocklist
            String[] trackers = {"doubleclick.net", "googleadservices.com", "facebook.com/tr"};
            for (String tracker : trackers) {
                if (url.contains(tracker)) return true;
            }
            return false;
        }

        private void updateSecurityIcon(boolean isSecure) {
            // TODO: Update lock icon color
        }
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
