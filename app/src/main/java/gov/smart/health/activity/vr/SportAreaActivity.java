package gov.smart.health.activity.vr;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.adapter.SportAreaRefreshRecyclerAdapter;
import gov.smart.health.model.SportAreaModel;

public class SportAreaActivity extends AppCompatActivity {
    private SportAreaRefreshRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private int mLastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_aera);
        mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.srl_sport_aera);
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.rv_sport_aera);

        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));

        List<SportAreaModel> list = new ArrayList<>();
        SportAreaModel model = new SportAreaModel(R.mipmap.learning_center, "朝阳公园", "朝阳公园地址");
        list.add(model);

        recyclerView.setAdapter(mAdapter = new SportAreaRefreshRecyclerAdapter(this, list));

        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<SportAreaModel> list = new ArrayList<>();
                        SportAreaModel model = new SportAreaModel(R.mipmap.learning_center, "朝阳公园", "朝阳公园地址");
                        list.add(model);
                        mAdapter.addNewDataLists(list);
                        mSwiperefreshlayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<SportAreaModel> list = new ArrayList<>();
                            SportAreaModel model = new SportAreaModel(R.mipmap.learning_center, "朝阳公园", "朝阳公园地址");
                            list.add(model);
                            mAdapter.addDataLists(list);
                            mSwiperefreshlayout.setRefreshing(false);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

    }
}

