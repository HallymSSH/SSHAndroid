package com.ssh.capstone.safetygohome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class daumView extends Activity {

    private WebView daum_webView;
    private Handler handler;
    private TextView daum_result;
    String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.daum_view);
        daum_result = (TextView) findViewById(R.id.daum_result);

        // WebView 초기화
        init_webView();

        handler = new Handler();
    }

    @SuppressLint("JavascriptInterface")
    public void init_webView() {
        // WebView 설정
        daum_webView = (WebView) findViewById(R.id.daum_webview);

        // JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙임
        daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
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
                    result = String.format("%s %s", arg1, arg2);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", result);
                    //daum_result.setText(String.format("%s %s", arg1, arg2));
                    setResult(RESULT_OK, resultIntent);
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                    finish();
                }
            });
        }
    }
}
