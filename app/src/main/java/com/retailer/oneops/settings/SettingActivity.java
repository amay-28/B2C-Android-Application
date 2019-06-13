package com.retailer.oneops.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.retailer.oneops.R;
import com.retailer.oneops.activity.WebViewActivity;
import com.retailer.oneops.agent.AgentListActivity;
import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.APIResponse;
import com.retailer.oneops.apiCalling.GenericResponse;
import com.retailer.oneops.apiCalling.Repository;
import com.retailer.oneops.auth.activity.LoginActivity;
import com.retailer.oneops.databinding.ActivitySettingsBinding;
import com.retailer.oneops.product.AddProductActivity;
import com.retailer.oneops.service.activity.AddServiceActivity;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.DialogUtil;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.OnDialogItemClickListener;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;

import static com.retailer.oneops.util.Constant.AUTHORIZATION_KEY;
import static com.retailer.oneops.util.Constant.DATA;
import static com.retailer.oneops.util.Constant.IS_LOGIN;
import static com.retailer.oneops.util.Constant.IS_TWO_DAY;
import static com.retailer.oneops.util.Constant.NO;
import static com.retailer.oneops.util.Constant.REFUND_POLICY;

public class SettingActivity extends AppCompatActivity implements OnDialogItemClickListener, APIResponse {
    private Activity mContext;
    private ActivitySettingsBinding binding;
    private Session session;
    private boolean sessionIsTwoDay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        binding = DataBindingUtil.setContentView(mContext, R.layout.activity_settings);
        session = new Session(mContext);
        sessionIsTwoDay = session.getSharedPreferencesBoolean(mContext, REFUND_POLICY);
        initialization();
        listeners();
    }

    private void listeners() {
        binding.rlAddservice.setOnClickListener(v -> {
            startActivity(new Intent(mContext, AddServiceActivity.class));
        });

        binding.rlAddproducts.setOnClickListener(v -> startActivity(new Intent(mContext, AddProductActivity.class).putExtra(Constant.IS_SETTING, true)));

        /*binding.rlLogout.setOnClickListener(v -> {
            DialogUtil.showOkCancelDialog(mContext, getString(R.string.logout_popup), null);
        });*/

        binding.rlAgents.setOnClickListener(v -> startActivity(new Intent(mContext, AgentListActivity.class)));
        binding.tvLogout.setOnClickListener(v -> performLogout());
        binding.rangeBar.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.tvMinValue.setText(String.valueOf(minValue) + " KM");
            binding.tvMaxValue.setText(String.valueOf(maxValue) + " KM");
        });

        binding.rlSetyourcancellationpolicy.setOnClickListener(v -> startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", "https://www.google.com")));
        binding.tvTc.setOnClickListener(v -> startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", "https://www.google.com")));
        binding.rlPrivacy.setOnClickListener(v -> {
            startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", "https://www.google.com"));
        });
        binding.rlContact.setOnClickListener(v -> {
            startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", "https://www.google.com"));
        });
        binding.rlRefundReturn.setOnClickListener(v -> openRefundDialog());
        binding.rlMinimumOrder.setOnClickListener(v -> startActivity(new Intent(mContext,SetAmountActivity.class)));
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(mContext));
        binding.header.tvMainHeading.setText(R.string.settings);
    }

    private void performLogout() {
        DialogUtil.showOkCancelDialog(mContext, getString(R.string.logout_popup), this);
    }

    @Override
    public void onDialogButtonClick(int i, int position) {
        Session session = new Session(mContext);
        session.setUserProfile(null);
        session.setString(IS_LOGIN, NO);

        Intent in = new Intent(mContext, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        (mContext).startActivity(in);
    }

    public void openRefundDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog_No_Border);

        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        lp.copyFrom(window.getAttributes());

        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialog.setContentView(R.layout.custom_refund_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Button buttonPositive = (Button) dialog.findViewById(R.id.buttonPositive);
        Button buttonNegative = (Button) dialog.findViewById(R.id.buttonNegative);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        RadioButton rbTwo = dialog.findViewById(R.id.rbTwo);
        RadioButton rbSeven = dialog.findViewById(R.id.rbSeven);


        if (sessionIsTwoDay) {
            rbTwo.setChecked(true);
        } else {
            rbSeven.setChecked(true);
        }

        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = radioGroup.getCheckedRadioButtonId();
                boolean isTwoDay = false;
                if (checkedId == R.id.rbTwo) {
                    isTwoDay = true;
                } else {
                    isTwoDay = false;

                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(IS_TWO_DAY, isTwoDay);
                refundReturnServiceCall(jsonObject);
                dialog.dismiss();
            }
        });

        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void refundReturnServiceCall(JsonObject jsonObject) {
        Call<JsonElement> call = APIClient.getClient(mContext).create(APIInterface.class).setRefundPolicy(new Session(mContext).getString(AUTHORIZATION_KEY), jsonObject);

        MyDialogProgress.open(mContext);
        try {
            new Repository(this).callBaseURL(this, call, 0);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToastWentWrong(mContext);
            MyDialogProgress.close(mContext);
        }
    }

    @Override
    public void onSuccess(JSONObject genericResponse, String msg, int ResponseOf) {
        boolean isTwoDay = false;
        try {
            JSONObject jsonObject = genericResponse.getJSONObject(DATA);
            if (jsonObject.has("isTwoDay"))
                isTwoDay = jsonObject.getBoolean("isTwoDay");

            Utils.showToast(mContext, getString(R.string.refund_policy_selected));
            session.setSharedPreferenceBoolean(mContext, REFUND_POLICY, isTwoDay);
            MyDialogProgress.close(mContext);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(GenericResponse genericResponse, String msg, int ResponseOf) {
        MyDialogProgress.close(mContext);
        Utils.showToast(mContext, msg);
    }

    @Override
    public void onException(boolean type, String msg, int ResponseOf) {
        MyDialogProgress.close(mContext);
        Utils.showToast(mContext, msg);
    }
}

