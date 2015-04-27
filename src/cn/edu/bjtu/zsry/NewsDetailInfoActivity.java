package cn.edu.bjtu.zsry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class NewsDetailInfoActivity extends Activity {
	private WebView webview;
	private LinearLayout ll_detail_loading;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		ll_detail_loading = (LinearLayout) findViewById(R.id.ll_detail_loading);
		Intent intent = getIntent();
		String newsId = intent.getStringExtra("newsId");
		String flag = intent.getStringExtra("flag");
		String baseUrl = intent.getStringExtra("baseUrl");
		webview = (WebView) findViewById(R.id.webview);
		ll_detail_loading.setVisibility(View.VISIBLE);
		webview.loadUrl(baseUrl + newsId + "&flag=" + flag);
		ll_detail_loading.setVisibility(View.GONE);
		webview.setWebViewClient(new WebViewClient());
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
	}

}
