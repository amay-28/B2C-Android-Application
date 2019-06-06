package com.retailer.oneops.checkout;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MAddress;
import com.retailer.oneops.checkout.adapter.CheckoutAdapter;
import com.retailer.oneops.checkout.model.MCart;
import com.retailer.oneops.checkout.model.MCartDetail;
import com.retailer.oneops.checkout.model.MOrder;
import com.retailer.oneops.checkout.model.MOrderLines;
import com.retailer.oneops.checkout.model.MOrderLinesRequest;
import com.retailer.oneops.checkout.model.MOrderRequest;
import com.retailer.oneops.checkout.presenter.CheckoutPresenter;
import com.retailer.oneops.checkout.viewinterface.CheckoutViewInterface;
import com.retailer.oneops.dashboard.viewinterface.MyInventViewInterface;
import com.retailer.oneops.databinding.ActivityCheckoutBinding;
import com.retailer.oneops.databinding.MyInventoryFragmentBinding;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.productListing.adapter.ProductListAdapter;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.model.MProductVariant;
import com.retailer.oneops.productListing.presenter.ProductListingPresenter;
import com.retailer.oneops.productListing.viewinterface.ProductListingViewInterface;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.DialogUtil;
import com.retailer.oneops.util.OnDialogItemClickListener;
import com.retailer.oneops.util.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.retailer.oneops.util.Constant.CUSTOMER_ADDRESS;
import static com.retailer.oneops.util.Constant.DIRECT_COURIER_TO_CUSTOMER;
import static com.retailer.oneops.util.Constant.HOME_DELIVERY_BY_STORE;
import static com.retailer.oneops.util.Constant.ONLINE;
import static com.retailer.oneops.util.Constant.ORDER_DELIVERY_ADDRESS;
import static com.retailer.oneops.util.Constant.ORDER_LINES;
import static com.retailer.oneops.util.Constant.ORDER_TYPE;
import static com.retailer.oneops.util.Constant.PAYMENT_CONFIRMED;
import static com.retailer.oneops.util.Constant.PAYMENT_MODE;
import static com.retailer.oneops.util.Constant.PHYSICAL_INVENTORY;
import static com.retailer.oneops.util.Constant.PRODUCT_ID;
import static com.retailer.oneops.util.Constant.PRODUCT_VARIANT_ID;
import static com.retailer.oneops.util.Constant.RETAILER_TO_CUSTOMER_PHYSICAL_INVENTORY;
import static com.retailer.oneops.util.Constant.RETAILER_TO_CUSTOMER_VIRTUAL_INVENTORY;
import static com.retailer.oneops.util.Constant.STORE_PICKUP;
import static com.retailer.oneops.util.Constant.VIRTUAL_INVENTORY;

public class CheckoutActivity extends AppCompatActivity implements CheckoutAdapter.CallBack, CheckoutViewInterface, OnDialogItemClickListener {

    private Activity activity;
    private ActivityCheckoutBinding binding;
    private CheckoutAdapter checkoutAdapter;
    private List<MCart> cartList = new ArrayList<>();
    private CheckoutPresenter checkoutPresenter;
    private CheckoutViewInterface checkoutViewInterface;
    private int cartId, deletePosition;
    private List<MOrderLinesRequest> orderLinesList = new ArrayList<>();
    private String deliveryType = DIRECT_COURIER_TO_CUSTOMER;
    private String orderType = "";
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);

        activity = CheckoutActivity.this;
        checkoutViewInterface = this;
        session = new Session(activity);
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
            switch (checkedId) {
                case R.id.rbStorePickup:
                    binding.rlAddress.setVisibility(View.GONE);
                    deliveryType = STORE_PICKUP;
                    //Toast.makeText(activity, R.string.error_coming_soon, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rbHomeDelivery:
                    deliveryType = HOME_DELIVERY_BY_STORE;
                    break;
                case R.id.rbDirectCourier:
                    deliveryType = DIRECT_COURIER_TO_CUSTOMER;
                    break;
            }
        });

        if (session.getInventoryType() != null) {
            if (session.getInventoryType().equalsIgnoreCase(PHYSICAL_INVENTORY)) {
                orderType = RETAILER_TO_CUSTOMER_PHYSICAL_INVENTORY;
            } else if (session.getInventoryType().equalsIgnoreCase(VIRTUAL_INVENTORY)) {
                orderType = RETAILER_TO_CUSTOMER_VIRTUAL_INVENTORY;
            } else {
                Toast.makeText(activity, getString(R.string.choose_product_from_same_inventory), Toast.LENGTH_LONG).show();
            }
        } else {
            session.setInventoryType(null);
        }

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
        //binding.btnPlaceOrder.setOnClickListener(v -> placeOrderClick(orderType, deliveryType, orderLineArray));
        binding.btnPlaceOrder.setOnClickListener(v -> {
            if (binding.rlAddress.getVisibility() == View.VISIBLE && checkValidateForAddress()) {
                placeOrderClick(orderType, deliveryType);
            } else if (binding.rlAddress.getVisibility() == View.GONE) {
                placeOrderClick(orderType, deliveryType);
            }
        });
    }

    public boolean checkValidateForAddress() {
        Resources resources = getResources();
        if (TextUtils.isEmpty(binding.etCustomerName.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_customer_name), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etAddLine1.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etAddLine2.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etCity.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_city), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etState.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_state), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void placeOrderClick(String orderType, String deliveryType) {
        MOrderRequest mOrderRequest = new MOrderRequest();
        mOrderRequest.setAddressId(null);
        mOrderRequest.setPayment_mode(ONLINE);
        mOrderRequest.setPayment_confirmed("true");
        mOrderRequest.setOrder_type(orderType);
        mOrderRequest.setOrder_delivery_address_type(deliveryType);

        mOrderRequest.setOrder_lines(orderLinesList);
        mOrderRequest.setCustomer_address(getCustomerAddressObject());
        checkoutPresenter.onPlaceOrderClick(mOrderRequest);
    }

    public MAddress getCustomerAddressObject() {
        MAddress mAddress = new MAddress();
        mAddress.setName(binding.etCustomerName.getText().toString().trim());
        mAddress.setMobileNumber(binding.etCustomerMobileno.getText().toString().trim());
        mAddress.setPincode(Integer.parseInt(binding.etPincode.getText().toString().trim()));
        mAddress.setAddressLine1(binding.etAddLine1.getText().toString().trim());
        mAddress.setAddressLine2(binding.etAddLine2.getText().toString().trim());
        mAddress.setLocality(binding.etLocality.getText().toString().trim());
        mAddress.setCity(binding.etCity.getText().toString().trim());
        mAddress.setState(binding.etState.getText().toString().trim());
        return mAddress;
    }

    @Override
    public void onSuccessfulCartDetails(MCartDetail mCartDetail, String message) {
        if (mCartDetail != null && mCartDetail.getCart_lines() != null) {
            if (mCartDetail.getCart_lines().size() <= 0) {
                session.setInventoryType(null);
            }

            cartList.addAll(mCartDetail.getCart_lines());
            checkoutAdapter.notifyDataSetChanged();

            List<Integer> mrpList = new ArrayList<>();
            for (int i = 0; i < mCartDetail.getCart_lines().size(); i++) {
                mrpList.add(Integer.valueOf(mCartDetail.getCart_lines().get(i).getProduct().getPrice()));
                orderLinesList.add(setCartListForCheckout(mCartDetail.getCart_lines().get(i).getProductId(),
                        mCartDetail.getCart_lines().get(i).getProductVariantId()));
            }

            binding.tvTotalMrp.setText("Rs. " + String.valueOf(calculateMrp(mrpList)));
            binding.tvGrandTotal.setText("Rs. " + calculateGrandTotal(calculateMrp(mrpList),
                    10, 20));
        } else {
            session.setInventoryType(null);
        }
    }

    @Override
    public void onSuccessfulPlaceOrder(MOrder mOrder, String message) {
        finish();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public MOrderLinesRequest setCartListForCheckout(int productId, int productVariantId) {
        MOrderLinesRequest mOrderLines = new MOrderLinesRequest();
        mOrderLines.setProductId(productId);

        if (productVariantId != 0)
            mOrderLines.setProductVariantId(productVariantId);

        return mOrderLines;
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
