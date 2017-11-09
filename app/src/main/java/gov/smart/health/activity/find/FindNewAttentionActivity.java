package gov.smart.health.activity.find;

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
import java.util.HashMap;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.find.adapter.AttentionRefreshRecyclerAdapter;
import gov.smart.health.activity.find.model.FindAttentionDataModel;
import gov.smart.health.activity.find.model.FindAttentionListDataModel;
import gov.smart.health.utils.SHConstants;

public class FindNewAttentionActivity extends AppCompatActivity {

    private AttentionRefreshRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private int mLastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;

    private int page;
    private FindAttentionDataModel findModel = new FindAttentionDataModel();
    private List<FindAttentionListDataModel> essayLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.attention_srl);
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.attention_list);

        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        page = 0;
        this.loadData();
        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new AttentionRefreshRecyclerAdapter(this, essayLists));

        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                essayLists.clear();
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
    }

    public void loadData() {
        HashMap<String,Object> map = new HashMap<>();
        map.put(SHConstants.CommonStart, SHConstants.EssayStart);
        map.put(SHConstants.CommonLength, SHConstants.EssayLength);
        map.put(SHConstants.CommonOrderColumnName, SHConstants.EssayOrderColumnName);

        AndroidNetworking.get(SHConstants.RecordShareRetrieveMobile)
                .addQueryParameter(map)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        findModel = gson.fromJson(response, FindAttentionDataModel.class);
                        if (findModel.success) {
                            essayLists.addAll(findModel.resultData);
                            mAdapter.addDataLists(essayLists);
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
