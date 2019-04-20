package com.eb.onebandhan.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivityThankYouBinding;

public class ThankYouActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityThankYouBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_thank_you);
        listner();
    }

    private void listner() {
        binding.tvContactUS.setOnClickListener(view -> Toast.makeText(activity, "comming soon...", Toast.LENGTH_SHORT).show());
    }
}
