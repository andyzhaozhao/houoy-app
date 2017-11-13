package gov.smart.health;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import gov.smart.health.activity.HomeActivity;
import gov.smart.health.activity.login.LoginActivity;
import gov.smart.health.fragment.Splash.SplashFragmentPagerAdapter;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(!SharedPreferencesHelper.gettingBoolean(SHConstants.IsShowSplash,false)) {
            setViews();
        }else {
            toHome();
        }

    }

    private void setViews() {
        FragmentManager manager = getSupportFragmentManager();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        SplashFragmentPagerAdapter adapter = new SplashFragmentPagerAdapter(manager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 2){
                    SharedPreferencesHelper.settingBoolean(SHConstants.IsShowSplash,true);
                    toHome();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void toHome(){
        Intent intent = new Intent();
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");
        if(pk.isEmpty()) {
            intent.setClass(getApplication(), LoginActivity.class);
        } else {
            intent.setClass(getApplication(), HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
