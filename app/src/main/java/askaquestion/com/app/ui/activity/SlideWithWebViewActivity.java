package askaquestion.com.app.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import askaquestion.com.app.R;
import askaquestion.com.app.core.constants.ConstantsAPI;
import askaquestion.com.app.databinding.ActivitySlideWithWebViewBinding;
import askaquestion.com.app.core.pojo.adapter_model.DetectConnection;
import askaquestion.com.app.databinding.CustomToastBinding;


public class SlideWithWebViewActivity extends AppCompatActivity {

    ActivitySlideWithWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySlideWithWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!DetectConnection.checkInternetConnection(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            binding.webView.setWebChromeClient(new chromeClient());
            binding.webView.setWebViewClient(new webClient());
            binding.webView.getSettings().setLoadWithOverviewMode(true);
            binding.webView.getSettings().setSupportZoom(true);
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.loadUrl(ConstantsAPI.URL_WEBVIEW);
        }
    }

    public class chromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.progress.setProgress(newProgress);
        }
    }

    public class webClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            binding.progress.setVisibility(View.GONE);
        }
    }

}