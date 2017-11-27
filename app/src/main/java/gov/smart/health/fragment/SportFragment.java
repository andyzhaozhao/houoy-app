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
import com.fitpolo.support.bluetooth.BluetoothModule;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.self.DeviceSettingActivity;
import gov.smart.health.activity.vr.SportAreaActivity;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.activity.vr.model.SportVideoListModelEx;
import gov.smart.health.activity.vr.model.SportVideoModel;
import gov.smart.health.activity.vr.model.VideoFolderListModel;
import gov.smart.health.activity.vr.adapter.SportRefreshRecyclerAdapter;
import gov.smart.health.activity.vr.model.VideoFolderModel;
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
    private TextView selectButton;
    private boolean isLoadingApi;
    private static int SELECT_PLACE = 123;
    private int page;
    private SportVideoModel jsonModel = new SportVideoModel();
    private List<SportVideoListModelEx> modelLists = new ArrayList<>();
    private VideoFolderListModel selectModel = new VideoFolderListModel();

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
        selectModel.pk_folder = "2";
        selectModel.folder_name = "朝阳公园";
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sport, container, false);
        mSwiperefreshlayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_vr_main);
        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_vr_main);
        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(mAdapter = new SportRefreshRecyclerAdapter(this.getActivity(), modelLists));

        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetAllData();
                Log.d("","SportFragment setOnRefreshListener");
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
                    Log.d("","SportFragment onScrollStateChanged" + page);
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

        selectButton = (TextView)rootView.findViewById(R.id.select_park);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SportFragment.this.getActivity(), SportAreaActivity.class);
                startActivityForResult(intent,SELECT_PLACE);
            }
        });

        TextView fitpolo =(TextView) rootView.findViewById(R.id.connect_fitpolos);
        fitpolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DeviceSettingActivity.class);
                startActivity(intent);
            }
        });

        BluetoothModule bluetoothModule = BluetoothModule.getInstance();
        if (bluetoothModule.isBluetoothOpen()) {
            String deviceAddress = SharedPreferencesHelper.gettingString(DeviceSettingActivity.AddressKey, null);
            if (deviceAddress != null && bluetoothModule.isConnDevice(getContext(), deviceAddress)) {
                fitpolo.setText("已绑定手环");
            } else {
                fitpolo.setText("未绑定手环");
            }
        } else {
            fitpolo.setText("未绑定手环");
        }
        resetAllData();
        this.loadData();
        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PLACE) {
            resetAllData();
            loadData(data);
        }
    }



    private void resetAllData(){
        page = 0;
        modelLists.clear();
        jsonModel = new SportVideoModel();
        mAdapter.notifyDataSetChanged();
    }

    public void loadData() {
        loadData(null);
    }
    public void loadData(Intent data) {
        if(isLoadingApi){
            return;
        }
        isLoadingApi = true;

        HashMap<String,Object> map = new HashMap<>();
        map.put(SHConstants.CommonStart, String.valueOf(page));
        map.put(SHConstants.CommonLength, SHConstants.EssayLength);
        map.put(SHConstants.CommonOrderColumnName, SHConstants.Video_List_OrderColumnName_Value);
        map.put(SHConstants.CommonOrderDir, SHConstants.CommonOrderDir_Desc);

        if(data != null && data.getSerializableExtra(SHConstants.Video_Floder_Key) != null){
            selectModel =  (VideoFolderListModel)data.getSerializableExtra(SHConstants.Video_Floder_Key);
            map.put(SHConstants.Video_Floder_Pk_Folder, selectModel.pk_folder);
            selectButton.setText(selectModel.folder_name+"▼");
        } if (selectModel != null){
            map.put(SHConstants.Video_Floder_Pk_Folder, selectModel.pk_folder);
            selectButton.setText(selectModel.folder_name+"▼");
        } else {
            selectButton.setText("选择地点▼");
        }

        AndroidNetworking.get(SHConstants.VideoRetrieve)
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
                        jsonModel = gson.fromJson(response, SportVideoModel.class);
                        if (jsonModel.success) {
                            for (int i =0 ; i< jsonModel.resultData.size() ; i++){
                                SportVideoListModelEx modelEx = new SportVideoListModelEx();
                                modelEx.videoModel = jsonModel.resultData.get(i);
                                modelLists.add(modelEx);
                            }
                            Log.d("","SportFragment onResponse" + modelLists.size() + " "+ jsonModel.total);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "信息获取失败", Toast.LENGTH_LONG).show();
                        }
                        isLoadingApi = false;
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("", "response error" + anError.getErrorDetail());
                        Toast.makeText(getContext(), "信息获取失败", Toast.LENGTH_LONG).show();
                        isLoadingApi = false;
                    }
                });
    }
}
