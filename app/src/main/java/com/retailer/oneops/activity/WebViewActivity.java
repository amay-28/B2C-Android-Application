package com.retailer.oneops.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.retailer.oneops.BaseActivity;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ActivityWebViewBinding;

import androidx.databinding.DataBindingUtil;

public class WebViewActivity extends BaseActivity {

    private static Intent intent;
    private Activity activity;
    private ActivityWebViewBinding binding;
    private Bundle bundle;
    String url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_web_view);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("url")) {
                url = bundle.getString("url");
            }
        }

        binding.webView.loadUrl(url);
    }


}
