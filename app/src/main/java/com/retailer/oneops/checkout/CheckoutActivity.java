package com.retailer.oneops.checkout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retailer.oneops.R;
import com.retailer.oneops.checkout.adapter.CheckoutAdapter;
import com.retailer.oneops.checkout.model.MCart;
import com.retailer.oneops.checkout.model.MCartDetail;
import com.retailer.oneops.checkout.presenter.CheckoutPresenter;
import com.retailer.oneops.checkout.viewinterface.CheckoutViewInterface;
import com.retailer.oneops.dashboard.viewinterface.MyInventViewInterface;
import com.retailer.oneops.databinding.ActivityCheckoutBinding;
import com.retailer.oneops.databinding.MyInventoryFragmentBinding;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.productListing.adapter.ProductListAdapter;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.presenter.ProductListingPresenter;
import com.retailer.oneops.productListing.viewinterface.ProductListingViewInterface;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.DialogUtil;
import com.retailer.oneops.util.OnDialogItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements CheckoutAdapter.CallBack, CheckoutViewInterface, OnDialogItemClickListener {

    private Activity activity;
    private ActivityCheckoutBinding binding;
    private CheckoutAdapter checkoutAdapter;
    private List<MCart> cartList = new ArrayList<>();
    private CheckoutPresenter checkoutPresenter;
    private CheckoutViewInterface checkoutViewInterface;
    private int cartId, deletePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);

        activity = CheckoutActivity.this;
        checkoutViewInterface = this;

        initialization();
        bindRecyclerView();
        listener();
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.Checkout);

        checkoutPresenter = new CheckoutPresenter(checkoutViewInterface, activity);
        checkoutPresenter.getCartDetails();

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.rbStorePickup:
                    //Toast.makeText(activity, R.string.error_coming_soon, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rbHomeDelivery:
                    break;
                case R.id.rbDirectCourier:
                    break;
            }
        });
    }

    public void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        binding.rvCartItems.setLayoutManager(mLayoutManager);
        binding.rvCartItems.setHasFixedSize(true);
        binding.rvCartItems.setItemAnimator(new DefaultItemAnimator());
        checkoutAdapter = new CheckoutAdapter(activity, cartList, this);
        binding.rvCartItems.setAdapter(checkoutAdapter);
    }

    private void listener() {

    }

    public void placeOrderClick() {
        
    }

    @Override
    public void onSuccessfulCartDetails(MCartDetail mCartDetail, String message) {
        cartList.addAll(mCartDetail.getCart_lines());
        checkoutAdapter.notifyDataSetChanged();

        List<Integer> mrpList = new ArrayList<>();
        for (int i = 0; i < mCartDetail.getCart_lines().size(); i++) {
            mrpList.add(Integer.valueOf(mCartDetail.getCart_lines().get(i).getProduct().getPrice()));
        }
        binding.tvTotalMrp.setText("Rs. " + String.valueOf(calculateMrp(mrpList)));
        binding.tvGrandTotal.setText("Rs. " + calculateGrandTotal(calculateMrp(mrpList),
                10, 20));
    }

    @Override
    public void onSuccessfulDeleteItem() {
       /* this.cartList.remove(deletePosition);
        checkoutAdapter.notifyDataSetChanged();*/
        this.cartList.clear();
        checkoutPresenter.getCartDetails();
    }

    @Override
    public void onFailedListing(String errorMessage) {

    }

    @Override
    public void onProductItemClick(int position, MProduct mProduct) {

    }

    @Override
    public void onDeleteItem(int position, int cartId) {
        this.cartId = cartId;
        this.deletePosition = position;
        DialogUtil.showOkCancelDialog(activity, getString(R.string.delete_from_bag), this);
    }

    @Override
    public void onDialogButtonClick(int i, int position) {
        checkoutPresenter.onDeleteProductItem(cartId);
    }

    private int calculateMrp(List<Integer> itemMrpList) {
        int totalPrice = 0;
        for (int i = 0; i < itemMrpList.size(); i++) {
            totalPrice += itemMrpList.get(i);
        }

        return totalPrice;
    }

    private int calculateGrandTotal(int mrp, int taxPrice, int deliveryCharges) {
        return mrp + taxPrice + deliveryCharges;
    }
}
