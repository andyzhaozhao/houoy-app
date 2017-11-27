package gov.smart.health.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;

import gov.smart.health.R;
import gov.smart.health.activity.self.MyAttentionActivity;
import gov.smart.health.activity.self.SportHistoryListActivity;
import gov.smart.health.activity.self.UserSettingActivity;
import gov.smart.health.activity.self.UserSettingInfoActivity;
import gov.smart.health.activity.self.model.MyPersonInfoListModel;
import gov.smart.health.activity.self.model.MyPersonInfoModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class SelfFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView name,detail,age;
    private ANImageView selfImg;
    private MyPersonInfoListModel personModel;

    public SelfFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelfFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelfFragment newInstance(String param1, String param2) {
        SelfFragment fragment = new SelfFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_self, container, false);
        ImageView bgImg = (ImageView)rootView.findViewById(R.id.self_bgimg);
        selfImg = (ANImageView)rootView.findViewById(R.id.self_self_img);

        selfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(SHConstants.SettingPersonModelKey,personModel);
                intent.setClass(getContext(), UserSettingInfoActivity.class);
                startActivity(intent);
            }
        });

        name = (TextView)rootView.findViewById(R.id.tv_self_name);
        detail = (TextView)rootView.findViewById(R.id.tv_self_detail);
        age = (TextView)rootView.findViewById(R.id.tv_self_age);

        View setting = rootView.findViewById(R.id.btn_self_setting);
        View history = rootView.findViewById(R.id.rl_self_history);
        View likes = rootView.findViewById(R.id.rl_self_likes);
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(), MyAttentionActivity.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(SHConstants.SettingPersonModelKey,personModel);
                intent.setClass(getContext(), UserSettingActivity.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(), SportHistoryListActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");
        HashMap<String,Object> map = new HashMap<>();
        map.put(SHConstants.CommonUser_PK, pk);

        AndroidNetworking.get(SHConstants.PersonRetrieve)
                .addQueryParameter(map)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        MyPersonInfoModel jsonModel = gson.fromJson(response, MyPersonInfoModel.class);
                        if (jsonModel.success) {
                            if(jsonModel.resultData != null && jsonModel.resultData.size()>0){
                                personModel = jsonModel.resultData.get(0);
                                name.setText(personModel.person_name);
                                detail.setText((personModel.person_alias == null)?"我什么都不想说":personModel.person_alias);
                                age.setText((personModel.age == null)?"秘密":personModel.age+"岁");

                                if(personModel.portraitPath != null && !personModel.portraitPath.isEmpty()) {
                                    selfImg.setDefaultImageResId(R.mipmap.person_default_icon);
                                    selfImg.setErrorImageResId(R.mipmap.person_default_icon);
                                    String path =  SHConstants.BaseUrlPhoto + personModel.portraitPath +"?time="+ System.currentTimeMillis();
                                    selfImg.setImageUrl(path);
                                }
                            }
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
