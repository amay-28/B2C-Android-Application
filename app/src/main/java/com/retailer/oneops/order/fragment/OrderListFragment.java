package com.retailer.oneops.order.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.retailer.oneops.R;

import com.retailer.oneops.apiCalling.APIResponse;
import com.retailer.oneops.apiCalling.GenericResponse;
import com.retailer.oneops.apiCalling.Repository;
import com.retailer.oneops.databinding.FragmentOrderListBinding;
import com.retailer.oneops.order.adapter.MyOrderListAdapter;
import com.retailer.oneops.order.model.MOrders;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

import static com.retailer.oneops.util.Constant.AUTHORIZATION_KEY;


public class OrderListFragment extends Fragment implements APIResponse {
    private View view;
    private int mFragmentType;
    private Context mContext;
    private FragmentOrderListBinding binding;

    private ArrayList<MOrders> mListService = new ArrayList<MOrders>();
    private MyOrderListAdapter myOrderListAdapter;
    private boolean loading = true;
    private LinearLayoutManager mLayoutManager;
    private int mPastVisibleItems, mVisibleItemCount, mTotalItemCount;
    private int mLimit = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_order_list, container, false);
        initView();
        view = binding.getRoot();
        return view;
    }

    private void initView() {
        mFragmentType = getArguments().getInt(Constant.FRAGMENT_TYPE);
        myOrderListAdapter = new MyOrderListAdapter(mContext, mListService);
        mLayoutManager = new LinearLayoutManager(mContext);
        binding.rcService.setLayoutManager(mLayoutManager);
        binding.rcService.setAdapter(myOrderListAdapter);
        setListener();

        getRequest();
        loadMore();
        pullToRefresh();
    }

    private void loadMore() {
        binding.rcService.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                mVisibleItemCount = mLayoutManager.getChildCount();
                mTotalItemCount = mLayoutManager.getItemCount();
                mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((mVisibleItemCount + mPastVisibleItems) >= mTotalItemCount) {
                        //      loading = false;
                        //       getRequest();
                    }
                }
            }
        });
    }

    private void pullToRefresh() {
        //  binding.swipeRefreshLayout.setEnabled(false);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
                onItemsLoadComplete();
            }
        });
    }


    private void refreshItems() {
        if (Utils.isConnectingToInternet(getActivity())) {
            loading = true;
            if (loading) {
                loading = false;
                mListService.clear();
                myOrderListAdapter.notifyDataSetChanged();
                getRequest();
            }
        } else {
            Utils.showToastConnection(mContext);
        }

    }


    void onItemsLoadComplete() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    private void getRequest() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("limit", mLimit + "");
        hashMap.put("offset", mListService.size() + "");
        hashMap.put("eager", "[order_lines.product.category]");

        loading = false;

        Call<JsonElement> call = Repository.getApiInterface(getActivity()).getMyOrders(new Session(mContext).getString(AUTHORIZATION_KEY), hashMap);


        if (mListService.size() == 0)
            if (mFragmentType == 1)
                MyDialogProgress.open(mContext);
            else
                binding.progressPvLinearColors.start();
        else
            binding.progressPvLinearColors.start();

        try {
            new Repository(getActivity()).callBaseURL(this, call, 0);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToastWentWrong(mContext);
            MyDialogProgress.close(mContext);
        }
    }

    @Override
    public void onSuccess(JSONObject genericResponse, String msg, int ResponseOf) {

        try {
            if(getActivity()!=null) {
                binding.incContentError.rlInternet.setVisibility(View.GONE);
                showList(genericResponse, getString(R.string.msg_no_orders_found));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFailed(GenericResponse genericResponse, String msg, int ResponseOf) {
        failed(msg == null ? getString(R.string.error_something_wrong) : msg, true);
    }

    @Override
    public void onException(boolean type, String msg, int ResponseOf) {
        failed(msg == null ? getString(R.string.error_something_wrong) : msg, false);
    }


    private void showList(JSONObject js, String success) {
        try {
            JSONArray jsonServiceArray = js.getJSONArray(Constant.DATA);

            if (jsonServiceArray.length() > 0) {
                Type type = new TypeToken<List<MOrders>>() {
                }.getType();
                List<MOrders> tempListNewsFeeds = new Gson().fromJson(jsonServiceArray.toString(), type);
                mListService.addAll(mListService.size(), tempListNewsFeeds);
                loading = true;
            } else {
                failed(success, false);
            }

            myOrderListAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyDialogProgress.close(mContext);
        binding.progressPvLinearColors.stop();
    }

    private void failed(String failed, boolean status) {
        if (mListService.size() == 0) {
            Utils.setMsg(mContext, binding.incContentError.rlInternet, binding.incContentError.tvMsg, failed);
        } else {
            if (status) Utils.showToast(mContext, failed);
        }

        loading = false;
        MyDialogProgress.close(mContext);
        binding.progressPvLinearColors.stop();
    }

    private void setListener() {
        binding.incContentError.btnRetry.setOnClickListener(v -> {

            loading = true;
            mListService.clear();
            getRequest();
        });
    }

}
