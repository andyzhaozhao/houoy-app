package gov.smart.health.fragment.Splash;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import gov.smart.health.R;

/**
 * Created by laoniu on 11/13/17.
 */
public class SplashFragmentPagerAdapter extends FragmentPagerAdapter {
    public SplashFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SplashFragment.newInstance(R.mipmap.explanation01);
            case 1:
                return SplashFragment.newInstance(R.mipmap.explanation02);
            case 2:
                return SplashFragment.newInstance(R.mipmap.explanation03);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ""+(position + 1);
    }
}