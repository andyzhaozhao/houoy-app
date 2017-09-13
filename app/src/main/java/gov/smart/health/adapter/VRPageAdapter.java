package gov.smart.health.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import gov.smart.health.R;

/**
 * Created by laoniu on 2017/07/23.
 */

public class VRPageAdapter extends PagerAdapter {

    private ViewPager mViewPage;
    private LayoutInflater inflater;
    private Handler mHandler;
    private static int TIME_OUT = 3000;

    private int[] resids = { R.mipmap.img1, R.mipmap.img2, R.mipmap.img3 ,R.mipmap.img4 };
    private List<View> listviews;

    public VRPageAdapter(Activity activity, ViewPager viewPage) {
        mViewPage = viewPage;
        inflater = activity.getLayoutInflater();

        listviews = new ArrayList<View>();
        int length = resids.length;
        for (int i = 0; i < length; i++) {
            View view = inflater.inflate(R.layout.find_page_item, null);
            ImageView img = (ImageView) view.findViewById(R.id.page_img);
            img.setImageResource(resids[i%resids.length]);
            listviews.add(view);
        }
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                mViewPage.setCurrentItem(mViewPage.getCurrentItem() + 1);
                mHandler.sendEmptyMessageDelayed(0, TIME_OUT);
            }
        };
        mHandler.sendEmptyMessageDelayed(0, TIME_OUT);
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(listviews.get(position%resids.length));
        return listviews.get(position%resids.length);
    }
}
