package com.ssh.capstone.safetygohome;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchAddress extends AppCompatActivity {
    private WebView daum_webView;
    private TextView daum_result;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_view);
        daum_result = (TextView) findViewById(R.id.daum_result);

        // WebView 초기화
        init_webView();

        handler = new Handler();
    }

    @SuppressLint("JavascriptInterface")
    public void init_webView(){
        // WebView 설정
        daum_webView = (WebView) findViewById(R.id.daum_webview);

        // JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙임
        daum_webView.addJavascriptInterface(new AndroidBridge(),"TestApp");
        daum_webView.getSettings().setSupportMultipleWindows(false);

        // web client를 크롬으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());

        daum_webView.loadUrl("http://tlgh0623.ivyro.net/daum_address.php");

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    daum_result.setText(String.format("%s %s", arg1, arg2));

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }
}
