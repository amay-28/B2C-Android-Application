package com.eb.onebandhan.product;

import android.app.Activity;
import android.os.Bundle;
import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivityAddProductBinding;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class AddProductActivity  extends AppCompatActivity {
    private Activity activity;
    private ActivityAddProductBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity, R.layout.activity_add_product);
        initialization();
    }

    private void initialization() {

    }
}
