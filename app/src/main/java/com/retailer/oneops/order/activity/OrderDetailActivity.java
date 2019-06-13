package com.retailer.oneops.order.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ActivityOrderDetailsBinding;
import com.retailer.oneops.order.model.MOrders;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class OrderDetailActivity extends AppCompatActivity {

    private MOrders mOrders;
    private ActivityOrderDetailsBinding binding;
    private Context context;
    private Activity activity;
    private static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        activity = OrderDetailActivity.this;
        initialization();
    }


    public void initialization() {
        binding.toolbar1.setHandler(new CommonClickHandler(activity));
        binding.toolbar1.tvMainHeading.setText(R.string.Order_Detail);
        if (intent != null && !getIntent().hasExtra(Constant.IS_SETTING))
            getIntentData();

    }

    private void getIntentData() {
        if (intent.hasExtra("mOrders")) {
            mOrders = intent.getParcelableExtra("mOrders");
            if (mOrders != null) {
                setData();
            }
        }
    }


    public static Intent getIntent(Context mContext, MOrders orderDetailModel) {
        intent = null;
        intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra("mOrders", (Parcelable) orderDetailModel);
        return intent;
    }


    public void setData() {
        List<Integer> mrpList = new ArrayList<>();
        binding.tvPaymentMode.setText(mOrders.getPayment_mode().toString());
        binding.tvmrpValue.setText("Rs. " + mOrders.getOrder_lines().get(0).getProduct().getCost_price());
        binding.tvOrderDiscountValue.setText("Rs. " + mOrders.getOrder_lines().get(0).getTotal_discount());
        binding.tvSellingPriceValue.setText("Rs. " + mOrders.getOrder_lines().get(0).getProduct().getPrice());
        binding.tvTotalValue.setText("Rs. " + (calculateGrandTotal(mrp, 10)));
        binding.tvCustomerName.setText(mOrders.getCustomer_address().getName());
        binding.orderNoValue.setText("" + mOrders.getOrder_lines().get(0).getOrderId());
        binding.orderPlacedDate.setText(Utils.displayDateReview(mOrders.getOrder_lines().get(0).getCreated_at()));
        if (mOrders.getOrder_lines().get(0).getProduct().getImages() != null && mOrders.getOrder_lines().get(0).getProduct().getImages().size() > 0) {
            Glide.with(activity).load(mOrders.getOrder_lines().get(0).getProduct().getImages().get(0).getUrl()).apply(new RequestOptions().placeholder(R.drawable.ic_default_product).error(R.drawable.ic_default_product)).into(binding.ivProductImage);
        }

        binding.tvAddress.setText(mOrders.getCustomer_address().toString());
        binding.tvProductName.setText(mOrders.getOrder_lines().get(0).getProduct().getName());
        binding.tvProductDescription.setText(mOrders.getOrder_lines().get(0).getProduct().getDescription());
        binding.tvProductQuantity.setText(" " + mOrders.getOrder_lines().get(0).getQuantity());
        binding.priceAfterDiscount.setText(mOrders.getOrder_lines().get(0).getProduct().getPrice());
        binding.priceBeforeDiscount.setText(mOrders.getOrder_lines().get(0).getProduct().getCost_price());
        binding.tvOrderStatusValue.setText(mOrders.getStatus().toString());


    }

    private int calculateMrp(List<Integer> itemMrpList) {
        int totalPrice = 0;
        for (int i = 0; i < itemMrpList.size(); i++) {
            totalPrice += itemMrpList.get(i);
        }

        return totalPrice;
    }

    int mrp;

    private int calculateGrandTotal(int mrp, int taxPrice) {
        mrp = Integer.parseInt(mOrders.getOrder_lines().get(0).getProduct().getPrice());

        return mrp + taxPrice;
    }

}
