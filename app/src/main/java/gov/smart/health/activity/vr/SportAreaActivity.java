package gov.smart.health.activity.vr;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.vr.model.SportVideoModel;
import gov.smart.health.activity.vr.model.VideoFolderListModel;
import gov.smart.health.activity.vr.model.VideoFolderModel;
import gov.smart.health.activity.vr.adapter.SportAreaRefreshRecyclerAdapter;
import gov.smart.health.utils.SHConstants;

public class SportAreaActivity extends AppCompatActivity {
    private SportAreaRefreshRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private int mLastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoadingApi;
    private int page;
    private VideoFolderModel jsonModel = new VideoFolderModel();
    private List<VideoFolderListModel> modelLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_aera);
        mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.srl_sport_aera);
        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.rv_sport_aera);
        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new SportAreaRefreshRecyclerAdapter(this, modelLists));

        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetAllData();
                loadData();
                mSwiperefreshlayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount()&& modelLists.size() < jsonModel.total - 1) {
                    page = page +1;
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.resetAllData();
        this.loadData();
    }

    private void resetAllData(){
        page = 0;
        modelLists.clear();
        jsonModel = new VideoFolderModel();
        mAdapter.notifyDataSetChanged();
    }

    public void loadData() {
        if(isLoadingApi){
            return;
        }
        isLoadingApi = true;
        HashMap<String,Object> map = new HashMap<>();
        map.put(SHConstants.CommonStart, String.valueOf(page));
        map.put(SHConstants.CommonLength, SHConstants.EssayLength);

        AndroidNetworking.get(SHConstants.FolderVideoRetrieve)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .addQueryParameter(map)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("","response "+response);
                        Gson gson = new Gson();
                        jsonModel = gson.fromJson(response, VideoFolderModel.class);
                        if (jsonModel.success) {
                            if(jsonModel.resultData != null) {
                                for (int i= 0; i<jsonModel.resultData.size() ;i++){
                                    if("root" .equals(jsonModel.resultData.get(i).folder_name)){
                                        jsonModel.resultData.remove(i);
                                    }
                                }

                                modelLists.addAll(jsonModel.resultData);
                                mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getApplication(), "信息获取失败", Toast.LENGTH_LONG).show();
                        }
                        isLoadingApi = false;
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("", "response error" + anError.getErrorDetail());
                        Toast.makeText(getApplication(), "信息获取失败", Toast.LENGTH_LONG).show();
                        isLoadingApi = false;
                    }
                });
    }
}

