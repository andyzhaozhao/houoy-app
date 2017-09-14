package gov.smart.health.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import gov.smart.health.R;
import gov.smart.health.activity.vr.BlueToothActivity;
import gov.smart.health.activity.vr.ShareActivity;
import gov.smart.health.activity.vr.VRPlayerActivity;

public class SportFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        View btnLink = rootView.findViewById(R.id.btn_link);
        View btnDefault = rootView.findViewById(R.id.btn_default);
        View btnDefaultLink = rootView.findViewById(R.id.btn_default_link);
        View btnClearDefaultLink = rootView.findViewById(R.id.btn_clear_default_link);

        mTv = (TextView) rootView.findViewById(R.id.et_link);

        btnDefaultLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTv.setText("http://cloud-mobile.360heros.com/producers/4630608605686575/9740409902296961/video/video_945afd9cea26e109bc91111d6401b23c_output_2k.mp4");
                //tv.setText("http://mediadelivery.mlbcontrol.net/2016/internal_tests/mlb/vr/SomeDayBaby.mp4");
                mTv.setText("https://raw.githubusercontent.com/googlevr/gvr-ios-sdk/master/Samples/VideoWidgetDemo/resources/congo.mp4");
            }
        });

        btnClearDefaultLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTv.setText("");
            }
        });

        btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), VRPlayerActivity.class);
                startActivity(intent);
            }
        });

        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String url = mTv.getText().toString();
                    if (url.isEmpty()){
                        Toast.makeText(getActivity(), "Please input Link", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Uri uri = Uri.parse(url);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setClass(getActivity(), VRPlayerActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Input Error Link", Toast.LENGTH_LONG).show();
                }
            }
        });

        rootView.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ShareActivity.class);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.start_bluetooth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), BlueToothActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

}
