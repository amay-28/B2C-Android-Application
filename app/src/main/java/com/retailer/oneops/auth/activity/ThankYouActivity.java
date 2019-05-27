package com.retailer.oneops.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ActivityThankYouBinding;

public class ThankYouActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityThankYouBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_thank_you);
        listner();
        initialization();
    }

    private void listner() {
        binding.tvContactUS.setOnClickListener(view -> Toast.makeText(activity, "comming soon...", Toast.LENGTH_SHORT).show());
    }

    private void initialization(){
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        },3000);
    }
}
