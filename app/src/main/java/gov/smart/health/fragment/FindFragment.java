package gov.smart.health.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import gov.smart.health.R;
import gov.smart.health.activity.find.DetailActivity;
import gov.smart.health.activity.find.EventActivity;
import gov.smart.health.activity.find.FindShareActivity;
import gov.smart.health.activity.find.LearningActivity;
import gov.smart.health.activity.vr.ShareActivity;
import gov.smart.health.adapter.HomePageAdapter;

public class FindFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewPager mViewPager;
    private View rootView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FindFragment() {
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_find, container, false);

        rootView.findViewById(R.id.find_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FindFragment.this.getActivity(), FindShareActivity.class);
                startActivity(intent);
            }
        });
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new HomePageAdapter(this,mViewPager));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position%4){
                    case 0:
                        RadioButton r1 = (RadioButton)rootView.findViewById(R.id.page_r1);
                        r1.setChecked(true);
                        break;
                    case 1:
                        RadioButton r2 = (RadioButton)rootView.findViewById(R.id.page_r2);
                        r2.setChecked(true);
                        break;
                    case 2:
                        RadioButton r3 = (RadioButton)rootView.findViewById(R.id.page_r3);
                        r3.setChecked(true);
                        break;
                    case 3:
                        RadioButton r4 = (RadioButton)rootView.findViewById(R.id.page_r4);
                        r4.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2 - (Integer.MAX_VALUE/2)%4);

        View btnLearning = rootView.findViewById(R.id.find_learning);
        View btnEvent = rootView.findViewById(R.id.find_event);

        btnLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), LearningActivity.class);
                startActivity(intent);
            }
        });
        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), EventActivity.class);
                startActivity(intent);
            }
        });

        View findFirst = rootView.findViewById(R.id.find_item_first);
        View findSecond = rootView.findViewById(R.id.find_item_second);
        findFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DetailActivity.class);
                startActivity(intent);
            }
        });
        findSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DetailActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
