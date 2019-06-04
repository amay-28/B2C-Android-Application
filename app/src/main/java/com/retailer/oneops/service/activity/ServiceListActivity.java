package com.retailer.oneops.service.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.retailer.oneops.BaseActivity;
import com.retailer.oneops.R;
import com.retailer.oneops.apiCalling.APIResponse;
import com.retailer.oneops.apiCalling.GenericResponse;
import com.retailer.oneops.apiCalling.Repository;
import com.retailer.oneops.databinding.ActivityServiceListBinding;
import com.retailer.oneops.service.adapter.ServiceListAdapter;
import com.retailer.oneops.settings.model.MServiceResponse;
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

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;

import static com.retailer.oneops.util.Constant.AUTHORIZATION_KEY;

public class ServiceListActivity extends BaseActivity implements APIResponse {

    private Activity mContext;
    private ActivityServiceListBinding binding;
    private ArrayList<MServiceResponse> mListService = new ArrayList<MServiceResponse>();
    private ServiceListAdapter mServiceAdapter;
    private boolean loading = true;
    private LinearLayoutManager mLayoutManager;
    private int mPastVisibleItems, mVisibleItemCount, mTotalItemCount;
    private int mLimit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_service_list);
        mContext = this;
        binding = DataBindingUtil.setContentView(mContext, R.layout.activity_service_list);
        initViews();
    }


    private void initViews() {
        Utils.setUpToolbarWithColor(mContext, binding.incToolbar.toolbarActionbar, binding.incToolbar.toolbarTitle, getString(R.string.label_services));
        mServiceAdapter = new ServiceListAdapter(mContext, mListService);
        mLayoutManager = new LinearLayoutManager(mContext);
        binding.rcService.setLayoutManager(mLayoutManager);
        binding.rcService.setAdapter(mServiceAdapter);
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
        if (Utils.isConnectingToInternet(mContext)) {
            loading = true;
            if (loading) {
                loading = false;
                mListService.clear();
                mServiceAdapter.notifyDataSetChanged();
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

        loading = false;

        Call<JsonElement> call = Repository.getApiInterface(mContext).getService(new Session(mContext).getString(AUTHORIZATION_KEY), hashMap);

        if (mListService.size() == 0)
            MyDialogProgress.open(mContext);
        else
            binding.progressPvLinearColors.start();

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
        binding.incContentError.rlInternet.setVisibility(View.GONE);
        showList(genericResponse, getString(R.string.msg_no_service_list));
    }


    @Override
    public void onFailed(GenericResponse genericResponse, String msg, int ResponseOf) {
        failed(msg == null ? "" : msg, true);
    }

    @Override
    public void onException(boolean type, String msg, int ResponseOf) {
        failed(msg == null ? "" : msg, false);
    }


    private void showList(JSONObject js, String success) {
        try {
            JSONArray jsonServiceArray = js.getJSONArray(Constant.DATA);

            if (jsonServiceArray.length() > 0) {
                Type type = new TypeToken<List<MServiceResponse>>() {
                }.getType();
                List<MServiceResponse> tempListNewsFeeds = new Gson().fromJson(jsonServiceArray.toString(), type);
                mListService.addAll(mListService.size(), tempListNewsFeeds);
                loading = true;
            } else {
                failed(success, false);
            }

            mServiceAdapter.notifyDataSetChanged();


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
        binding.incContentError.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = true;
                mListService.clear();
                getRequest();
            }
        });
    }

}
