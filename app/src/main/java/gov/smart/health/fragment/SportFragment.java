package gov.smart.health.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import gov.smart.health.activity.vr.SportAreaActivity;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.activity.vr.model.SportVideoModel;
import gov.smart.health.activity.vr.model.VideoFolderListModel;
import gov.smart.health.activity.vr.adapter.SportRefreshRecyclerAdapter;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class SportFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SportRefreshRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private int mLastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;
    private Button selectButton;

    private int page;
    private SportVideoModel jsonModel = new SportVideoModel();
    private List<SportVideoListModel> modelLists = new ArrayList<>();

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
        selectButton = (Button)rootView.findViewById(R.id.select_park);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SportFragment.this.getActivity(), SportAreaActivity.class);
                startActivityForResult(intent,0);
            }
        });

        page = 0;
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

        recyclerView.setAdapter(mAdapter = new SportRefreshRecyclerAdapter(this.getActivity(), modelLists));

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
        this.loadData();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        modelLists.clear();
        mAdapter.notifyDataSetChanged();
        loadData(data);
    }

    public void loadData() {
        loadData(null);
    }
    public void loadData(Intent data) {
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");

        HashMap<String,Object> map = new HashMap<>();
        map.put(SHConstants.CommonStart, SHConstants.EssayStart);
        map.put(SHConstants.CommonLength, SHConstants.EssayLength);
        map.put(SHConstants.CommonOrderColumnName, SHConstants.Video_List_OrderColumnName_Value);
        map.put(SHConstants.CommonOrderDir, SHConstants.CommonOrderDir_Desc);

        if(data != null && data.getSerializableExtra(SHConstants.Video_Floder_Key) != null){
          VideoFolderListModel model =  (VideoFolderListModel)data.getSerializableExtra(SHConstants.Video_Floder_Key);
            map.put(SHConstants.Video_Floder_Pk_Folder, model.pk_folder);
            selectButton.setText(model.folder_name);
        }

        AndroidNetworking.get(SHConstants.VideoRetrieve)
                .addQueryParameter(map)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        jsonModel = gson.fromJson(response, SportVideoModel.class);
                        if (jsonModel.success) {
                            modelLists.addAll(jsonModel.resultData);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "信息获取失败", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("", "response error" + anError.getErrorDetail());
                        Toast.makeText(getContext(), "信息获取失败", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
