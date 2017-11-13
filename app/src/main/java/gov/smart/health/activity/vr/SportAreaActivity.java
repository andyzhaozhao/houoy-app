package gov.smart.health.activity.vr;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.vr.model.VideoFolderListModel;
import gov.smart.health.activity.vr.model.VideoFolderModel;
import gov.smart.health.activity.vr.adapter.SportAreaRefreshRecyclerAdapter;
import gov.smart.health.utils.SHConstants;

public class SportAreaActivity extends AppCompatActivity {
    private SportAreaRefreshRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private int mLastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;

    private int page;
    private VideoFolderModel jsonModel = new VideoFolderModel();
    private List<VideoFolderListModel> modelLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_aera);
        mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.srl_sport_aera);
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.rv_sport_aera);

        page = 0;
        this.loadData();

        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new SportAreaRefreshRecyclerAdapter(this, modelLists));

        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                modelLists.clear();
                loadData();
                mSwiperefreshlayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount()&& modelLists.size() < jsonModel.total) {
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

    }

    public void loadData() {
        AndroidNetworking.get(SHConstants.FolderVideoRetrieve)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
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
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("", "response error" + anError.getErrorDetail());
                        Toast.makeText(getApplication(), "信息获取失败", Toast.LENGTH_LONG).show();
                    }
                });
    }
}

