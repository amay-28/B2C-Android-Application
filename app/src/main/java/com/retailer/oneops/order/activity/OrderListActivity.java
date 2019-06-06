package com.retailer.oneops.order.activity;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.retailer.oneops.BaseActivity;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ActivityOrderListBinding;
import com.retailer.oneops.order.adapter.ViewPagerAdapter;
import com.retailer.oneops.order.fragment.OrderListFragment;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Utils;

public class OrderListActivity extends BaseActivity {
    private Activity mContext;
    private ActivityOrderListBinding binding;
    private OrderListFragment mSinglePiece, mWholeSeller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_order_list);
        mContext = this;
        binding = DataBindingUtil.setContentView(mContext, R.layout.activity_order_list);
        initViews();
    }

    private void initViews() {
        Utils.setUpToolbarWithColor(mContext, binding.incToolbar.toolbarActionbar, binding.incToolbar.toolbarTitle, getString(R.string.orders));
        setupViewPager(binding.pagerOrder);
        binding.setActivity(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle newBundle;

        mSinglePiece = new OrderListFragment();
        newBundle = new Bundle();
        newBundle.putInt(Constant.FRAGMENT_TYPE, 1);
        mSinglePiece.setArguments(newBundle);
        adapter.addFrag(mSinglePiece);

       /* mWholeSeller = new OrderListFragment();
        newBundle = new Bundle();
        newBundle.putInt(Constant.FRAGMENT_TYPE, 2);
        mWholeSeller.setArguments(newBundle);
        adapter.addFrag(mWholeSeller);*/

        viewPager.setAdapter(adapter);
    }


    @Override
    public void onClicks(View view) {
        super.onClicks(view);
        switch (view.getId()) {
            case R.id.btnWholesale:
                binding.pagerOrder.setCurrentItem(0, false);
                changeBackground(true);
                break;

            case R.id.btnSinglePiece:
                if (binding.pagerOrder.getAdapter().getCount() > 1) {
                    binding.pagerOrder.setCurrentItem(1, false);
                    changeBackground(false);
                } else
                    Utils.showToast(mContext, "Coming Soon");
                break;
        }
    }

    private void changeBackground(boolean isSingle) {
        if (!isSingle) {
            binding.btnSinglePiece.setBackground(getDrawable(R.drawable.curve_back_left_flat_blue));
            binding.btnWholesale.setBackground(getDrawable(R.drawable.curve_back_right_flat_grey));
            binding.btnSinglePiece.setTextColor(getResources().getColor(R.color.white));
            binding.btnWholesale.setTextColor(getResources().getColor(R.color.color_grey_text));
        } else {
            binding.btnWholesale.setBackground(getDrawable(R.drawable.curve_back_right_flat_blue));
            binding.btnSinglePiece.setBackground(getDrawable(R.drawable.curve_back_left_flat_grey));
            binding.btnSinglePiece.setTextColor(getResources().getColor(R.color.color_grey_text));
            binding.btnWholesale.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
