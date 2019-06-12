package com.retailer.oneops.agent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.retailer.oneops.R;
import com.retailer.oneops.agent.adapter.AgentListAdapter;
import com.retailer.oneops.agent.model.MAgentResponse;
import com.retailer.oneops.apiCalling.APIResponse;
import com.retailer.oneops.apiCalling.GenericResponse;
import com.retailer.oneops.apiCalling.Repository;
import com.retailer.oneops.databinding.ActivityAgentListBinding;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.retailer.oneops.util.Constant.AUTHORIZATION_KEY;

public class AgentListActivity extends AppCompatActivity implements APIResponse {

    private Activity mContext;
    private AgentListAdapter mAgentAdapter;
    private ActivityAgentListBinding binding;
    private ArrayList<MAgentResponse> mListAgent = new ArrayList<MAgentResponse>();
    private LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    private int OPEN_AGENT_ADD = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        binding = DataBindingUtil.setContentView(mContext, R.layout.activity_agent_list);
        listeners();
        initialization();
        initViews();
    }

    private void initViews() {
        mAgentAdapter = new AgentListAdapter(mContext, mListAgent);
        mLayoutManager = new LinearLayoutManager(mContext);
        binding.rcAgent.setLayoutManager(mLayoutManager);
        binding.rcAgent.setAdapter(mAgentAdapter);
        setListener();

        getRequest();
        pullToRefresh();
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
                mListAgent.clear();
                mAgentAdapter.notifyDataSetChanged();
                getRequest();
            }

        } else {
            Utils.showToastConnection(mContext);
        }

    }

    void onItemsLoadComplete() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }


    private void initialization() {
        Utils.setUpToolbarWithColor(mContext, binding.incToolbar.toolbarActionbar, binding.incToolbar.toolbarTitle, getString(R.string.label_Agent));
    }

    private void listeners() {
        binding.fab.setOnClickListener(v -> {
            startActivityForResult(new Intent(mContext, AddAgentActivity.class), OPEN_AGENT_ADD);
        });
    }

    private void getRequest() {
        HashMap<String, String> hashMap = new HashMap<>();

        Call<JsonElement> call = Repository.getApiInterface(mContext).getAgent(new Session(mContext).getString(AUTHORIZATION_KEY), hashMap);

        if (mListAgent.size() == 0)
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
        showList(genericResponse, getString(R.string.msg_no_agent_list));
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
                Type type = new TypeToken<List<MAgentResponse>>() {
                }.getType();
                List<MAgentResponse> tempListNewsFeeds = new Gson().fromJson(jsonServiceArray.toString(), type);
                mListAgent.addAll(mListAgent.size(), tempListNewsFeeds);
                loading = true;
            } else {
                failed(success, false);
            }

            mAgentAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyDialogProgress.close(mContext);
        binding.progressPvLinearColors.stop();
    }

    private void failed(String failed, boolean status) {
        if (mListAgent.size() == 0) {
            Utils.setMsg(mContext, binding.incContentError.rlInternet, binding.incContentError.tvMsg, failed);
        } else {
            if (status) Utils.showToast(mContext, failed);
        }

        MyDialogProgress.close(mContext);
        binding.progressPvLinearColors.stop();
    }

    private void setListener() {
        binding.incContentError.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListAgent.clear();
                getRequest();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == OPEN_AGENT_ADD) {
                mListAgent.clear();
                getRequest();
            }
        }
    }
}
