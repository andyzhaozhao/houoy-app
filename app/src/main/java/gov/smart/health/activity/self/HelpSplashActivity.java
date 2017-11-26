package gov.smart.health.activity.self;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.fitpolo.support.bluetooth.BluetoothModule;

import gov.smart.health.R;
import gov.smart.health.activity.HomeActivity;
import gov.smart.health.activity.login.LoginActivity;
import gov.smart.health.fragment.Splash.SplashFragmentPagerAdapter;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class HelpSplashActivity extends AppCompatActivity {

    private View btnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_splash);
        setViews();
    }

    private void setViews() {
        RadioButton r1 = (RadioButton)findViewById(R.id.splash_page_r1);
        r1.setChecked(true);
        btnStart = findViewById(R.id.btn_start);
        btnStart.setVisibility(View.GONE);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        SplashFragmentPagerAdapter adapter = new SplashFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        RadioButton r1 = (RadioButton)findViewById(R.id.splash_page_r1);
                        r1.setChecked(true);
                        break;
                    case 1:
                        RadioButton r2 = (RadioButton)findViewById(R.id.splash_page_r2);
                        r2.setChecked(true);
                        break;
                    case 2:
                        RadioButton r3 = (RadioButton)findViewById(R.id.splash_page_r3);
                        r3.setChecked(true);
                        break;
                }

                if(position == 2){
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
