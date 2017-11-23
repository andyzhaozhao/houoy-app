package gov.smart.health.activity.find;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
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
import gov.smart.health.activity.find.model.FindEssayDataModel;
import gov.smart.health.activity.find.model.FindEssayListDataModel;
import gov.smart.health.activity.find.adapter.LearningRefreshRecyclerAdapter;
import gov.smart.health.activity.vr.model.VideoFolderModel;
import gov.smart.health.utils.SHConstants;

public class LearningActivity extends AppCompatActivity {

    private LearningRefreshRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private int mLastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoadingApi;
    private int page;
    private FindEssayDataModel findModel = new FindEssayDataModel();
    private List<FindEssayListDataModel> essayLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        String titleText = getIntent().getStringExtra(SHConstants.CommonTitle);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(titleText);
        mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.learning_srl);
        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.learning_list);
        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new LearningRefreshRecyclerAdapter(this, essayLists));

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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount() && essayLists.size() < findModel.total) {
                    page = page +1;
                    loadData();
                    mSwiperefreshlayout.setRefreshing(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
        this.resetAllData();
        this.loadData();
    }

    private void resetAllData(){
        page = 0;
        essayLists.clear();
        findModel = new FindEssayDataModel();
        mAdapter.notifyDataSetChanged();
    }

    public void loadData() {
        if(isLoadingApi){
            return;
        }
        isLoadingApi = true;
        String type = getIntent().getStringExtra(SHConstants.CommonPkType);
        HashMap<String,Object> map = new HashMap<>();
        map.put(SHConstants.CommonStart, String.valueOf(page));
        map.put(SHConstants.CommonLength, SHConstants.EssayLength);
        map.put(SHConstants.CommonOrderColumnName, SHConstants.EssayOrderColumnName);
        map.put(SHConstants.CommonPkType, type);

        AndroidNetworking.get(SHConstants.EssayRetrieveMobile)
                .addQueryParameter(map)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("","response "+response);
                        Gson gson = new Gson();
                        findModel = gson.fromJson(response, FindEssayDataModel.class);
                        if (findModel.success) {
                            essayLists.addAll(findModel.resultData);
                            mAdapter.notifyDataSetChanged();
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
