package gov.smart.health.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.vr.SportAreaActivity;
import gov.smart.health.adapter.SportRefreshRecyclerAdapter;
import gov.smart.health.model.SportModel;

public class SportFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SportRefreshRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private int mLastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mTv;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SportFragment newInstance(String param1, String param2) {
        SportFragment fragment = new SportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sport, container, false);
        Button selectButton = (Button)rootView.findViewById(R.id.select_park);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SportFragment.this.getActivity(), SportAreaActivity.class);
                startActivityForResult(intent,0);
            }
        });

        mSwiperefreshlayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_vr_main);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_vr_main);

        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this.getActivity()));

        List<SportModel> list = new ArrayList<>();
        SportModel model = new SportModel(R.mipmap.sport_bg, "上肢伸展运动", "5分钟",true);
        list.add(model);

        recyclerView.setAdapter(mAdapter = new SportRefreshRecyclerAdapter(this.getActivity(), list));

        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<SportModel> list = new ArrayList<>();
                        SportModel model = new SportModel(R.mipmap.sport_bg, "上肢伸展运动", "5分钟",true);
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
                            List<SportModel> list = new ArrayList<>();
                            SportModel model = new SportModel(R.mipmap.sport_bg, "上肢伸展运动", "5分钟",false);
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
        return rootView;
    }
}
