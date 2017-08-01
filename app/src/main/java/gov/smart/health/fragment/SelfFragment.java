package gov.smart.health.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gov.smart.health.R;
import gov.smart.health.activity.self.SportHistoryListActivity;
import gov.smart.health.activity.self.UserSettingActivity;

public class SelfFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        ImageView selfImg = (ImageView)rootView.findViewById(R.id.self_self_img);

        TextView name = (TextView)rootView.findViewById(R.id.tv_self_name);
        TextView detail = (TextView)rootView.findViewById(R.id.tv_self_detail);
        TextView age = (TextView)rootView.findViewById(R.id.tv_self_age);
        TextView level = (TextView)rootView.findViewById(R.id.tv_self_level);
        TextView lastTime = (TextView)rootView.findViewById(R.id.tv_self_last_time);

        View setting = rootView.findViewById(R.id.btn_self_setting);
        View history = rootView.findViewById(R.id.rl_self_history);
        View rank = rootView.findViewById(R.id.rl_self_rank);
        View likes = rootView.findViewById(R.id.rl_self_likes);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
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


}
