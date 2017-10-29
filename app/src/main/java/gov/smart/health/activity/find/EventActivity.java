package gov.smart.health.activity.find;

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
import gov.smart.health.adapter.EventRefreshRecyclerAdapter;
import gov.smart.health.model.LearningModel;

public class EventActivity extends AppCompatActivity {
    private EventRefreshRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private int mLastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.event_srl);
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.event_list);

        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));

        List<LearningModel> list = new ArrayList<>();
        LearningModel model = new LearningModel(R.mipmap.learning_center, "there is title", "there is context");
        list.add(model);

        recyclerView.setAdapter(mAdapter = new EventRefreshRecyclerAdapter(this, list));

        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<LearningModel> list = new ArrayList<>();
                        LearningModel model = new LearningModel(R.mipmap.learning_center, "there is new title", "there is new context");
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
                            List<LearningModel> list = new ArrayList<>();
                            LearningModel model = new LearningModel(R.mipmap.learning_center, "there is old title", "there is old context");
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
